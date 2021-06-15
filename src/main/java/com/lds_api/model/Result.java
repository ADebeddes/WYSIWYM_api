package com.lds_api.model;

/**
 * 
 * @author Alexandre DEBEDDES
 *
 */
public class Result {
	private String resource1;
	
	private String resource2;
	
	private double score;

	public Result() {}

	public String getResource1() {
		return resource1;
	}

	public void setResource1(String resource1) {
		this.resource1 = resource1;
	}

	public String getResource2() {
		return resource2;
	}

	public void setResource2(String resource2) {
		this.resource2 = resource2;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
}
