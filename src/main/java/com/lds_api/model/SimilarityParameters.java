package com.lds_api.model;

import java.util.List;

/**
 * 
 * @author Alexandre DEBEDDES
 *
 */
public class SimilarityParameters {
	
	private LdDatasetMain ldDatasetMain;
	private List<Resources> resources;
	private Options options;
	
	public SimilarityParameters() {}
	
	public void setLdDatasetMain(LdDatasetMain ldDatasetMain) {
		this.ldDatasetMain = ldDatasetMain;
	}
	
	public void setResources(List<Resources> resources) {
		this.resources = resources;
	}
	
	public void setOptions(Options options) {
		this.options =options;
	}
	
	public LdDatasetMain getLdDatasetMain() {
		return ldDatasetMain;
	}
	
	public List<Resources> getResources() {
		return resources;
	}
	
	public Options getOptions() {
		return options;
	}
	
	
}
