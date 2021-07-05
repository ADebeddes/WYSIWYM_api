package com.lds_api.model;

import java.util.List;

/**
 * 
 * @author Alexandre DEBEDDES
 *
 */
public class SimilarityParameters {
	
	private LdDatasetMain ldDatasetMain;
	private List<SimilarityResources> similarityResources;
	private SimilarityOptions similarityOptions;
	
	public SimilarityParameters() {}
	
	public void setLdDatasetMain(LdDatasetMain ldDatasetMain) {
		this.ldDatasetMain = ldDatasetMain;
	}
	
	public void setResources(List<SimilarityResources> similarityResources) {
		this.similarityResources = similarityResources;
	}
	
	public void setOptions(SimilarityOptions similarityOptions) {
		this.similarityOptions =similarityOptions;
	}
	
	public LdDatasetMain getLdDatasetMain() {
		return ldDatasetMain;
	}
	
	public List<SimilarityResources> getResources() {
		return similarityResources;
	}
	
	public SimilarityOptions getOptions() {
		return similarityOptions;
	}

	@Override
	public String toString() {
		return "SimilarityParameters [ldDatasetMain=" + ldDatasetMain + ", resources=" + similarityResources + ", options=" + similarityOptions + "]";
	}
	
	
}
