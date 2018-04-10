
/**
 * @author nirmoho-Mac
 *
 */
package com.team13.datamining.c45;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.team13.datamining.datamodels.Feature;
import com.team13.datamining.datamodels.Values;


public class FeatureSelector {
	
	private Feature bestFeature;
	private HashMap<String, ArrayList<Values>> subset;
	private double informationGain;
	private double splitVal;
	
	public FeatureSelector(List<Feature> featureList, List<Values> valuesList, Feature targetFeature) throws IOException	{
		
		this.bestFeature = null;
		this.informationGain = -1;
		this.subset = null;
		
		for (Feature f : featureList)	{
			double currentInfoGain = 0.0;
			HashMap<String, ArrayList<Values>> currentSubset = null;
			
			InformationGain ig = new InformationGain(f);
			if (f.getFeatureType().equals("discrete"))	{
				ig.calculateInformationGainDiscrete(valuesList, targetFeature);
				currentInfoGain = ig.getInformationGain();
				currentSubset = ig.getValuesSubset();
				//System.out.println("Infor Gain (" + f.getFeatureName() + ") = " + currentInfoGain );
			}	else	{
				//ig.calculateInformationGainContinuous(valuesList, targetFeature);
				currentInfoGain = ig.getInformationGain();
				currentSubset = ig.getValuesSubset();
				this.splitVal = ig.getSplitVal();
			}
			
			if (currentInfoGain > this.informationGain)	{
				this.informationGain = currentInfoGain;
				this.bestFeature = f;
				this.subset = currentSubset;
			}
		}
	}

	public double getSplitVal() {
		return splitVal;
	}

	public void setSplitVal(double splitVal) {
		this.splitVal = splitVal;
	}

	public Feature getBestFeature() {
		return bestFeature;
	}

	public void setBestFeature(Feature bestFeature) {
		this.bestFeature = bestFeature;
	}

	public HashMap<String, ArrayList<Values>> getSubset() {
		return subset;
	}

	public void setSubset(HashMap<String, ArrayList<Values>> subset) {
		this.subset = subset;
	}

	public double getInformationGain() {
		return informationGain;
	}

	public void setInformationGain(double informationGain) {
		this.informationGain = informationGain;
	}

}
