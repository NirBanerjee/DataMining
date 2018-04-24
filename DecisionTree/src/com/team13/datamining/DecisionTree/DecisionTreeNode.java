/**
 * Class to define the decision tree node.
 * @author nirmoho-Mac
 *
 */
package com.team13.datamining.DecisionTree;

import java.util.HashMap;

import com.team13.datamining.datamodels.Feature;

public class DecisionTreeNode {

	private Feature feature;
	private HashMap<String, DecisionTreeNode> children;
	private String targetClass;
	private boolean isLeaf;
	
	public DecisionTreeNode(String targetClass)	{
		this.isLeaf = true;
		this.targetClass = targetClass;
	}
	
	public DecisionTreeNode(Feature feature) {
		this.isLeaf = false;
		this.feature = feature;
		children = new HashMap<String, DecisionTreeNode>();
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public HashMap<String, DecisionTreeNode> getChildren() {
		return children;
	}

	public void setChildren(HashMap<String, DecisionTreeNode> children) {
		this.children = children;
	}

	public String getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	public void addChild(String valueName, DecisionTreeNode child) {
		children.put(valueName, child);
	}
	
	@Override
	public String toString() {
		if (!this.isLeaf) return "Root feature: " + this.feature.getFeatureName() + "; Children: " + this.children;
		else return "Leaf label: " + this.targetClass;
	}
		
}
