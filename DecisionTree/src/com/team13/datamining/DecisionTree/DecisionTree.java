/**
 * @author nirmoho-Mac
 *
 */
package com.team13.datamining.DecisionTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.team13.datamining.c45.EntropyCalculator;
import com.team13.datamining.c45.FeatureSelector;
import com.team13.datamining.datamodels.DataSet;
import com.team13.datamining.datamodels.Feature;
import com.team13.datamining.datamodels.Values;

public class DecisionTree {
	
	private DecisionTreeNode rootNode;
	
	public void DecisionTreeNode()	{
		this.rootNode = null;
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
	
	private DecisionTreeNode buildTree(List<Feature> featureList, List<Values> valuesList, Feature targetFeature) throws IOException	{
		
		if (featureList.size() == 0)	{
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
				DecisionTreeNode childNode = buildTree(featureList, subSet, targetFeature);
				node.addChild(val, childNode);
			}
		}
		
		featureList.add(feat);
		
		return node;
	}
	
	public void buildDecisionTree(DataSet dataSet) throws IOException	{
		
		//generate validation data
		//int foldFactor = 5;
		
		//double validationError = 0.0;
		//int startIndex = 0;
		List<Feature> featureList = dataSet.getFeatureList();
		List<Values> valuesList = dataSet.getValueList();
		Feature targetFeature = dataSet.getLabelFeature();
		
		this.rootNode = this.buildTree(featureList, valuesList, targetFeature);
		
		//Generate Validation and Test Data.
//		int stepSize = dataSet.getDataSize() / foldFactor;
//		DataSet[] validationSets = new DataSet[foldFactor];
//		DataSet[] trainingSets = new DataSet[foldFactor];
//		for (int i = 0; i < foldFactor; i++)	{
//			List<Values> validationDataList = new ArrayList<>(valuesList.subList(startIndex, startIndex + stepSize));
//			DataSet validationSet = new DataSet(validationDataList.size(), featureList, validationDataList);
//			validationSets[i] = validationSet;
//			
//			List<Values> trainingDataList = new ArrayList<>();
//			if(startIndex != 0)	{
//				trainingDataList = new ArrayList<>(valuesList.subList(0, startIndex));	
//			}
//			trainingDataList.addAll(new ArrayList<>(valuesList.subList(startIndex + stepSize, valuesList.size())));
//			DataSet trainingSet = new DataSet(trainingDataList.size(), featureList, trainingDataList);
//			trainingSets[i] = trainingSet;
//		}		
	}
	
	public DecisionTreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(DecisionTreeNode rootNode) {
		this.rootNode = rootNode;
	}

}
