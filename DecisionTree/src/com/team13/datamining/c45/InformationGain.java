/**
 * @author nirmoho-Mac
 *
 */
package com.team13.datamining.c45;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.team13.datamining.datamodels.Feature;
import com.team13.datamining.datamodels.Values;


public class InformationGain {
	
	private Feature feature;
	private double informationGain;
	private HashMap<String, ArrayList<Values>> valuesSubset;
	private double splitVal;
	
	public InformationGain(Feature feature)	{
		this.feature = feature;
		this.valuesSubset = new HashMap<>();
	}
	
	public void calculateInformationGainDiscrete(List<Values> valuesList, Feature targetFeature) throws IOException	{
		this.splitVal = Integer.MIN_VALUE;
		List<String> featureValues = feature.getFeatureValues();
		
		String featureName = feature.getFeatureName();
		
		for(String val : featureValues)	{
			this.valuesSubset.put(val, new ArrayList<Values>());
		}
		
		for(Values value : valuesList)	{
			Map<String, String> pairs = value.getFeatureValueMap();
			String featureValue = pairs.get(featureName);
			valuesSubset.get(featureValue).add(value);
		}
	
		double H_Y = EntropyCalculator.getEntropyValue(valuesList, targetFeature);
		
		//System.out.println("H_Y = " + H_Y);
		double H_X = 0.0;
		int n = valuesList.size();
		for(String feat : this.valuesSubset.keySet())	{
			ArrayList<Values> subList = new ArrayList<>(valuesSubset.get(feat));
			double H_X_spec = EntropyCalculator.getEntropyValue(subList, targetFeature);
			//System.out.println("H_X (" + feat + ") = " + H_X_spec);
			int m = subList.size();
			H_X = H_X + ((double)m/n) * H_X_spec;
		}
		
		//System.out.println("H_X = " + H_X);
		this.informationGain = H_Y - H_X;
	}
	
	public void calculateInformationGainContinuous(List<Values> valuesList, Feature targetFeature) throws IOException	{

		final String featureName = this.feature.getFeatureName();
		this.informationGain = Integer.MIN_VALUE;
		
		Comparator<Values> comparator = new Comparator<Values>() {
			@Override
			public int compare(Values x, Values y) {
				Map<String, String> xPair = x.getFeatureValueMap();
				String xValue = xPair.get(featureName);
						
				Map<String, String> yPair = y.getFeatureValueMap();
				String yValue = yPair.get(featureName);
				if (Double.parseDouble(xValue) - Double.parseDouble(yValue) > 0) return 1;
				else if (Double.parseDouble(xValue) - Double.parseDouble(yValue) < 0) return -1;
				else return 0;
			}
		};
		Collections.sort(valuesList, comparator);
			 
		int bestPos = 0;
		for (int i = 0; i < valuesList.size() - 1; i++) {
			Map<String, String> valuePair = valuesList.get(i).getFeatureValueMap();
			String val = valuePair.get(featureName);
			Map<String, String> nextValuePair = valuesList.get(i + 1).getFeatureValueMap();
			String nextVal = nextValuePair.get(featureName);
					
			if (!val.equals(nextVal)) {
				double currInfoGain = calculateCont(this.feature, valuesList, targetFeature, i);
				if (currInfoGain - this.informationGain > 0) {
					this.informationGain = currInfoGain;
					bestPos = i;
				}
			}
		}	
		
		// (4) Calculate threshold
		Map<String, String> temp1 = valuesList.get(bestPos).getFeatureValueMap();
		String v1 = temp1.get(featureName);
		Map<String, String> temp2 = valuesList.get(bestPos).getFeatureValueMap();
		String v2 = temp2.get(featureName);			
		this.splitVal = (Double.parseDouble(v1) + Double.parseDouble(v2)) / 2;	
		
		ArrayList<Values> left = new ArrayList<>();
		ArrayList<Values> right = new ArrayList<>();
		for (int i = 0; i < bestPos; i++) {
			left.add(valuesList.get(i));
		}
		for (int i = bestPos + 1; i < valuesList.size(); i++) {
			right.add(valuesList.get(i));
		}
		String leftName = "Less Than " + this.splitVal;
		String rightName = "More Than " + this.splitVal;
		this.valuesSubset.put(leftName, left);
		this.valuesSubset.put(rightName, right);
		
	}
	
	public static double calculateCont(Feature f, List<Values> valuesList, Feature targetFeature, int idx) throws IOException	{
		
		int n = valuesList.size();
		double H_Y = EntropyCalculator.getEntropyValue(valuesList, targetFeature);
		
		int leftIndex = idx + 1;
		int rightIndex = valuesList.size() - idx - 1;
		double H_X_left =  EntropyCalculator.getEntropyValueCont(valuesList, targetFeature, 0, idx);
		double H_X_right = EntropyCalculator.getEntropyValueCont(valuesList, targetFeature, idx + 1, valuesList.size() - 1);
		double H_X = ((double)leftIndex/n) * H_X_left + ((double)rightIndex/n) * H_X_right;
		double infoGain = H_Y - H_X;
		return infoGain;
	}
	
	public double getInformationGain() {
		return informationGain;
	}

	public void setInformationGain(double informationGain) {
		this.informationGain = informationGain;
	}

	public HashMap<String, ArrayList<Values>> getValuesSubset() {
		return valuesSubset;
	}

	public void setValuesSubset(HashMap<String, ArrayList<Values>> valuesSubset) {
		this.valuesSubset = valuesSubset;
	}

	public double getSplitVal() {
		return splitVal;
	}

	public void setSplitVal(double splitVal) {
		this.splitVal = splitVal;
	}
}
