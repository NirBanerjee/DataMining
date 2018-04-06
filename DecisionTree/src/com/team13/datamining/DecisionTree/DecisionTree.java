/**
 * @author nirmoho-Mac
 *
 */
package com.team13.datamining.DecisionTree;

import java.util.List;

import com.team13.datamining.datamodels.DataSet;
import com.team13.datamining.datamodels.Feature;
import com.team13.datamining.datamodels.Values;

public class DecisionTree {
	
	private DecisionTreeNode rootNode;
	
	public void DecisionTreeNode()	{
		this.rootNode = null;
	}
	
	public DecisionTreeNode constructTree(List<Feature> featureList, List<Values> valuesList, Feature targetFeature)	{
		
		return null;
	}
	
	public void buildDecisionTree(DataSet dataSet)	{
		
		//generate validation data
		int foldFactor = 5;
		
		//double validationError = 0.0;
		int startIndex = 0;
		List<Feature> featureList = dataSet.getFeatureList();
		List<Values> valuesList = dataSet.getValueList();
		Feature targetFeature = dataSet.getLabelFeature();
		
		rootNode = this.constructTree(featureList, valuesList, targetFeature);
		
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
}
