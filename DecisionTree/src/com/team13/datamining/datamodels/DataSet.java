/**
 * @author nirmoho-Mac
 *
 */
package com.team13.datamining.datamodels;

import java.util.ArrayList;
import java.util.List;
public class DataSet {
	
	private List<Feature> featureList;
	private List<Values> valueList;
	private Feature labelFeature;
	private int dataSize;

	public DataSet(int dataSize, List<Feature> featureList, List<Values> valueList)	{
		this.featureList = featureList;
		this.valueList = valueList;
		this.dataSize = dataSize;
		this.labelFeature = featureList.get(featureList.size()-1);
	}

	public int getDataSize() {
		return dataSize;
	}

	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

	public List<Feature> getFeatureList() {
		featureList.remove(featureList.size() - 1);
		return this.featureList;
	}

	public void setFeatureList(List<Feature> featureList) {
		this.featureList = featureList;
	}

	public List<Values> getValueList() {
		List<Values> retList = new ArrayList<>();
		
		for(Values val : this.valueList)	{
			retList.add(val);
		}
		
		return retList;
	}

	public void setValueList(List<Values> valueList) {
		this.valueList = valueList;
	}
	
	public Feature getLabelFeature() {
		return labelFeature;
	}

	public void setLabelFeature(Feature labelFeature) {
		this.labelFeature = labelFeature;
	}
}
