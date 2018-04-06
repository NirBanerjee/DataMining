package com.team13.datamining.datamodels;
import java.io.IOException;
import java.util.ArrayList;

public class Feature {
	
	//Variable to store name of feature
	private String featureName;
	
	//Variable to store type of feature (Continuous or Discrete)
	private String featureType;
	
	//Variable to hold all the values for this feature.
	private ArrayList<String> featureValues;
	
	public Feature(String featureName, String featureValues) throws IOException	{
		
		this.featureName = featureName;
		this.featureValues = new ArrayList<String>();
		if(featureValues == null || featureValues.length() < 4)	{
			throw new IOException("Input File not in proper Format");
		}
		if("real".equals(featureValues))	{
			this.featureType = "real";
			this.featureValues.add("real");
		}	else		{
			this.featureType = "discrete";
			featureValues = featureValues.substring(1, featureValues.length() -1);
			String[] vals = featureValues.split(",");
			for(String val : vals)	{
				this.featureValues.add(val);
			}
		}
	}
	
	public ArrayList<String> getFeatureValues() {
		return featureValues;
	}

	public void setFeatureValues(ArrayList<String> featureValues) {
		this.featureValues = featureValues;
	}

	public String getFeatureName() {
		return featureName;
	}
	
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	
	public String getFeatureType() {
		return featureType;
	}
	
	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}
}
