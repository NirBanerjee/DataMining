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
	private List<String> treeToPrint;
	
	public void DecisionTreeNode()	{
		this.rootNode = null;
		this.treeToPrint = null;
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
				DecisionTreeNode childNode = buildTree(featureList, subSet, targetFeature);
				node.addChild(val, childNode);
			}
		}
		
		featureList.add(feat); 
		
		return node;
	}
	
	public void buildDecisionTree(DataSet dataSet) throws IOException	{
		
		List<Feature> featureList = dataSet.getFeatureList();
		List<Values> valuesList = dataSet.getValueList();
		Feature targetFeature = dataSet.getLabelFeature();
		
		this.rootNode = this.buildTree(featureList, valuesList, targetFeature);
		
		System.out.println("Tree before Pruning");
		this.printTree();
		
		//Add method for prediction and printing to file
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
