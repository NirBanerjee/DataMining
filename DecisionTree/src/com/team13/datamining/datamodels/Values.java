/**
 * Object to store each row of data.
 * @author nirmoho-Mac
 *
 */
package com.team13.datamining.datamodels;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Values {
	/**
	 * HashMap to store feature and value pair.
	 */
	private Map<String, String> featureValueMap;
	/**
	 * Label for the row.
	 */
	private String targetValue;
	/**
	 * row index in the file.
	 */
	private int rowIndex;
	
	/**
	 * Getter for feature Value map
	 * @return
	 */
	public Map<String, String> getFeatureValueMap() {
		return featureValueMap;
	}
	
	/**
	 * Setter for feature value map.
	 * @param featureValueMap
	 */
	public void setFeatureValueMap(Map<String, String> featureValueMap) {
		this.featureValueMap = featureValueMap;
	}

	/**
	 * Parameterized constructor.
	 * @param rowIndex
	 * @param lineParts
	 * @param featureList
	 */
	public Values(int rowIndex, String[] lineParts, List<Feature> featureList)	{
		this.rowIndex = rowIndex;
		featureValueMap = new HashMap<>();
		for(int i = 0; i < lineParts.length; i++)	{
			featureValueMap.put(featureList.get(i).getFeatureName(), lineParts[i]);
		}

		this.targetValue = lineParts[lineParts.length - 1];
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	
	@Override
	public String toString()	{
		return "Index = " + this.rowIndex + " Values = " + this.featureValueMap;
	}

}
