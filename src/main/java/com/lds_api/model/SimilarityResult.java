package com.lds_api.model;

public class SimilarityResult {
	private String id1;
	
	private String id2;
	
	private double result;
	
	public SimilarityResult() {}
	
	public void setId1(String id) {
		id1=id;
	}
	
	public void setId2(String id) {
		id2=id;
	}
	
	public void setResult(double r) {
		result=r;
	}
	
	public String getId1() {
		return id1;
	}
	
	public String getId2() {
		return id2;
	}
	
	public double getResult() {
		return result;
	}
}
