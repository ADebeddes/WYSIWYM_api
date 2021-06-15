package com.lds_api.model;

/**
 * 
 * @author Alexandre DEBEDDES
 *
 */
public class Options {
	private boolean benchmark;
	private int threads;
	private boolean useIndex;
	private String measureType;
	
	public Options() {}

	public boolean isBenchmark() {
		return benchmark;
	}

	public void setBenchmark(boolean benchmark) {
		this.benchmark = benchmark;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public boolean isUseIndex() {
		return useIndex;
	}

	public void setUseIndex(boolean useIndex) {
		this.useIndex = useIndex;
	}

	public String getMeasureType() {
		return measureType;
	}

	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}

	@Override
	public String toString() {
		return "Options [benchmark=" + benchmark + ", threads=" + threads + ", useIndex=" + useIndex + ", measureType="
				+ measureType + "]";
	}
	
	
}
