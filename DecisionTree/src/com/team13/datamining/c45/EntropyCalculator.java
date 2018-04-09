/**
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

/**
 * @author nirmoho-Mac
 *
 */
public class EntropyCalculator {

	public static double getEntropyValue(List<Values> valuesList, Feature targetFeature) throws IOException	{
		
		List<String> valuesofTarget = new ArrayList<>(targetFeature.getFeatureValues());
		
		String targetName = targetFeature.getFeatureName();
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
		
		int datasetSize = valuesList.size();
		double entropy = 0.0;
		
		for (String key : labelCount.keySet())	{
			int count = labelCount.get(key);
			
			if(count == 0)	{
				continue;
			}
			
			if(count == datasetSize)	{
				return 0;
			}
			
			double prob = (double)count / datasetSize;
			entropy = entropy + prob * Math.log(prob)/Math.log(2);
		}
		
		return -entropy;
		
	}
}
