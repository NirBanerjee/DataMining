/**
 * @author nirmoho-Mac
 *
 */
package com.team13.datamining.datamodels;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Values {
	
	private Map<String, String> featureValueMap;
	private int rowIndex;
	
	public Values(int rowIndex, String[] lineParts, List<Feature> featureList)	{
		this.rowIndex = rowIndex;
		featureValueMap = new HashMap<>();
		for(int i = 0; i < lineParts.length; i++)	{
			featureValueMap.put(featureList.get(i).getFeatureName(), lineParts[i]);
		}
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
}
