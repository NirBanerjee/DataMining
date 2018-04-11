/**
 * @author nirmoho-Mac
 *
 */
package com.team13.datamining.DecisionTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.team13.datamining.c45.EntropyCalculator;
import com.team13.datamining.c45.FeatureSelector;
import com.team13.datamining.datamodels.DataSet;
import com.team13.datamining.datamodels.Feature;
import com.team13.datamining.datamodels.Values;
import com.team13.datamining.fileIO.FileIO;

public class DecisionTree {
	
	private DecisionTreeNode rootNode;
	private List<String> treeToPrint;
	
	public void DecisionTreeNode()	{
		this.rootNode = null;
		this.treeToPrint = null;
	}
	
	private String predictRow(Values val, DecisionTreeNode currentNode)	{
		String label = "";
		
		if(currentNode.isLeaf())	{
			return currentNode.getTargetClass();
		}
		
		Feature splitFeature = currentNode.getFeature();
		
		if(splitFeature.getFeatureType().equals("discrete"))	{
			String featVal = val.getFeatureValueMap().get(splitFeature.getFeatureName());
			for (String key : currentNode.getChildren().keySet())	{
				if(featVal.equals(key))	{
					DecisionTreeNode nextNode = currentNode.getChildren().get(key);
					label = predictRow(val, nextNode);
				}
			}
		}	else	{
			String featVal = val.getFeatureValueMap().get(splitFeature.getFeatureName());
			HashMap<String, DecisionTreeNode>hm = currentNode.getChildren();
			String keyS = "";
			for (String key : hm.keySet())	{
				keyS = key;
				break;
			}
			String[] parts = keyS.split(":");
			double splitVal = Double.parseDouble(parts[1]);
			
			if(Double.parseDouble(featVal) > splitVal)	{
				for(String key : hm.keySet())	{
					if(key.startsWith("More"))	{
						DecisionTreeNode nextNode = currentNode.getChildren().get(key);
						label = predictRow(val, nextNode);
					}
				}
			}	else		{
				for(String key : hm.keySet())	{
					if(key.startsWith("Less"))	{
						DecisionTreeNode nextNode = currentNode.getChildren().get(key);
						label = predictRow(val, nextNode);
					}
				}
			}
		}
		
		//System.out.println(label);
		return label;
	}
	private List<String> predict(List<Values> valuesList)	{
		
		List<String> result = new ArrayList<>();
		for (Values val : valuesList)	{
			DecisionTreeNode tempNode = this.rootNode;
			result.add(this.predictRow(val, tempNode));
		}
		return result;
		
	}
	private void printTreeUtil(DecisionTreeNode root, StringBuilder sb, ArrayList<String> finalStr)	{
		
		if(root.isLeaf())	{
			StringBuilder path = new StringBuilder(sb);
			path.append(root.getTargetClass());
			finalStr.add(path.toString());
		}	else	{
			sb.append(root.getFeature().getFeatureName());
			HashMap<String, DecisionTreeNode> children = root.getChildren();
			for (String valueName : children.keySet()) {
				StringBuilder temp = new StringBuilder(sb);
				temp.append("(Value = ");
				temp.append(valueName);
				temp.append(") => ");
				printTreeUtil(children.get(valueName), temp, finalStr);
			}
			
		}
	}

	private void printTree() {
		ArrayList<String> finalStr = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		DecisionTreeNode tempNode = this.rootNode;
		printTreeUtil(tempNode, sb, finalStr);
		this.treeToPrint = new ArrayList<>(finalStr);
	}
	
	private double calculateAccuracy(List<String> L1, List<String> L2)	{
		int total = 0;
		
		for (int i=0; i < L1.size(); i++)	{
			if(L1.get(i).equals(L2.get(i)))	{
				total ++;
			}
		}
		
		double accuracy = 100 * (double)total / L1.size();
		return accuracy;
	}
	
	private String getMajorityClass(List<Values> valuesList, Feature feature) throws IOException	{
		
		List<String> valuesofTarget = new ArrayList<>(feature.getFeatureValues());
		String targetName = feature.getFeatureName();
		Map<String, Integer> labelCount = new HashMap<String, Integer>();
		
		for (String label : valuesofTarget)	{
			labelCount.put(label, 0);
		}
		
		for (Values val : valuesList)	{
			Map<String, String> pairs = val.getFeatureValueMap();
			String lab = pairs.get(targetName);
			if(!labelCount.containsKey(lab))	{
				throw new IOException("Invalid data !!");
			}
			labelCount.put(lab, labelCount.get(lab) + 1);
		}
		
		String maxLabel = "";
		int maxCount = 0;
		
		for(String key : labelCount.keySet())	{
			int count = labelCount.get(key);
			if (count > maxCount)	{
				maxLabel = key;
				maxCount = count;
			}
		}
		return maxLabel;
	}
	
	private DecisionTreeNode buildTree(List<Feature> featureList, List<Values> valuesList, Feature targetFeature, int level) throws IOException	{
		
		if (featureList.size() == 0 || level == 1)	{
			String leafClass = this.getMajorityClass(valuesList, targetFeature);
			DecisionTreeNode leafNode = new DecisionTreeNode(leafClass);
			return leafNode;
		}
		
		double H_Y = EntropyCalculator.getEntropyValue(valuesList, targetFeature);
		
		if (H_Y == 0)	{
			String leafClass = valuesList.get(0).getFeatureValueMap().get(targetFeature.getFeatureName());
			DecisionTreeNode leafNode = new DecisionTreeNode(leafClass);
			return leafNode;
		}
		
		FeatureSelector fs = new FeatureSelector(featureList, valuesList, targetFeature);
		Feature feat = fs.getBestFeature();
		//System.out.println("Best Feature = " + feat.getFeatureName());
		
		DecisionTreeNode node = new DecisionTreeNode(feat);
		featureList.remove(feat);
		
		HashMap<String, ArrayList<Values>> valueSets = fs.getSubset();
		for (String val : valueSets.keySet())	{
			ArrayList<Values> subSet = valueSets.get(val);
			if (subSet.size() == 0)	{
				String leafClass = getMajorityClass(valuesList, targetFeature);
				DecisionTreeNode leafNode = new DecisionTreeNode(leafClass);
				node.addChild(val, leafNode);
			}	else	{
				DecisionTreeNode childNode = buildTree(featureList, subSet, targetFeature, level-1);
				node.addChild(val, childNode);
			}
		}
		
		featureList.add(feat); 
		
		return node;
	}
	
	public void treeOperations(String[] args) throws IOException	{
		
		String trainFile = args[0];
		String testFile = args[1];
		String trainPredictionsFile = args[2];
		String testPredictionsFile = args[3];
		String metricsFile = args[4];
		String printTreeFile = args[5];
		
		DataSet trainSet = FileIO.readFile(trainFile);
		DataSet testSet = FileIO.readFile(testFile);
		
		
		//Shuffle the data
		int foldFactor = 5;
		
		List<Feature> featureList = trainSet.getFeatureList();
		Feature targetFeature = trainSet.getLabelFeature();
		List<Values> valuesList = trainSet.getValueList();
		
		int featureNum = featureList.size();		
		
		int stepSize = trainSet.getDataSize() / foldFactor;
		Collections.shuffle(valuesList);
		
		List<String> metricsList = new ArrayList<>();
		double maxAccuracy = 0.0;
		int optimalLevel = 0;
		for (int i = 0; i < featureNum; i++)	{
			DataSet[] validationSets = new DataSet[foldFactor];	
			DataSet[] trainingSets = new DataSet[foldFactor];
			double validationAccuracy = 0.0;
			int startIndex = 0;
			
			
			for (int j = 0; j < foldFactor; j++)	{
				List<Values> validationDataList = new ArrayList<>(valuesList.subList(startIndex, startIndex + stepSize));
				DataSet validationSet = new DataSet(validationDataList.size(), featureList, validationDataList);
				validationSets[j] = validationSet;
				
				List<Values> trainingDataList = new ArrayList<>();
				if(startIndex != 0)	{
					trainingDataList = new ArrayList<>(valuesList.subList(0, startIndex));
				}
				trainingDataList.addAll(new ArrayList<>(valuesList.subList(startIndex + stepSize, valuesList.size())));
				DataSet trainingSubSet = new DataSet(trainingDataList.size(), featureList, trainingDataList);
				trainingSets[j] = trainingSubSet;
				startIndex = startIndex + stepSize;
			}
			
			for (int j = 0; j < foldFactor; j++)	{
				this.rootNode = null;
				
				List<Values> trainingValueList = trainingSets[j].getValueList();
				List<Values> validationValueList = validationSets[j].getValueList();
				List <String> labelList = new ArrayList<>();
				for (int k = 0; k < validationValueList.size(); k++)	{
					labelList.add(validationValueList.get(k).getTargetValue());
				}
				
				this.rootNode = this.buildTree(featureList, trainingValueList, targetFeature, (i+1));
				List <String> predictList = new ArrayList<>();
				predictList = this.predict(validationValueList);
				
				double accuracy = this.calculateAccuracy(predictList, labelList);
				validationAccuracy = validationAccuracy + accuracy;
			}
			validationAccuracy = validationAccuracy / foldFactor;
			if (validationAccuracy > maxAccuracy)	{
				maxAccuracy = validationAccuracy;
				optimalLevel = i + 1;
			}
		}
		
		System.out.println("Completed Cross Validation.....");
		//Build Tree with optimal height
		this.rootNode = null;
		valuesList = trainSet.getValueList();
		Collections.shuffle(valuesList);
		List<Values> valuesListCopy = new ArrayList<>(valuesList);
		List <String> labelList = new ArrayList<>();
		for (int k = 0; k < valuesList.size(); k++)	{
			labelList.add(valuesList.get(k).getTargetValue());
		}
		
		this.rootNode = this.buildTree(featureList, valuesList, targetFeature, optimalLevel);
		
		String line = "K folds selected => " + foldFactor;
		metricsList.add(line);
		System.out.println("Final Decision Tree Built.....");
		//Predict Training Labels
		List <String> predictList = new ArrayList<>();
		predictList = this.predict(valuesListCopy);
		double fullAccuracy = this.calculateAccuracy(predictList, labelList);
		line = "Training Accuracy => " + fullAccuracy;
		metricsList.add(line);
		line = "Cross Validation Accuracy => " + maxAccuracy;
		metricsList.add(line);
		
		//Predict Test Labels
		List<Values> testValuesList = testSet.getValueList();
		List <String> testPredictList = new ArrayList<>();
		testPredictList = this.predict(testValuesList);
		
		//Print Tree to File
		this.printTree();
		FileIO.printToFile(printTreeFile, this.treeToPrint);
		System.out.println("Decision Tree printed to file - " + printTreeFile);
		
		//Print Prediction Labels to File
		FileIO.printToFile(trainPredictionsFile, predictList);
		System.out.println("Predictions on training data file printed to - " + trainPredictionsFile);
		
		//Print Test prediction labels to File
		FileIO.printToFile(testPredictionsFile, testPredictList);
		System.out.println("Predictions on test data file printed to - " + testPredictionsFile);
		
		//Print metrics to file
		FileIO.printToFile(metricsFile, metricsList);
		System.out.println("Process metric printed to - " + metricsFile);
	}
	
	public DecisionTreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(DecisionTreeNode rootNode) {
		this.rootNode = rootNode;
	}
	
	public List<String> getTreeToPrint() {
		return treeToPrint;
	}

	public void setTreeToPrint(List<String> treeToPrint) {
		this.treeToPrint = treeToPrint;
	}
}
