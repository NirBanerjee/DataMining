
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
	private double threshold;
	
	public FeatureSelector(List<Feature> featureList, List<Values> valuesList, Feature targetFeature) throws IOException	{
		
		this.bestFeature = null;
		this.informationGain = -1;
		this.subset = null;
		
		for (Feature f : featureList)	{
			double currentInfoGain = 0.0;
			HashMap<String, ArrayList<Values>> currentSubset = null;
			
			if (f.getFeatureType().equals("discrete"))	{
				InformationGainDiscrete igd = new InformationGainDiscrete(f, targetFeature, valuesList);
				
			}	else	{
				
			}
			
			if (currentInfoGain > this.informationGain)	{
				this.informationGain = currentInfoGain;
				this.bestFeature = f;
				this.subset = currentSubset;
			}
		}
	}
}
