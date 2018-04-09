/**
 * @author nirmoho-Mac
 *
 */
package com.team13.datamining.c45;

import java.io.IOException;
import java.util.ArrayList;
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

	public void calculateInformationGainContinuous(List<Values> valuesList, Feature targetFeature)	{
		this.splitVal = -1;
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
