package com.lds_api.model;

public class MicroMeasureOptions {
	
	private double weight;
	private String measureType;
	
	public MicroMeasureOptions() {
		
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		System.out.println(weight);
		this.weight = weight;
	}

	public String getMeasureType() {
		return measureType;
	}

	public void setMeasureType(String measureType) {
		System.out.println(measureType);
		this.measureType = measureType;
	}
	
}
