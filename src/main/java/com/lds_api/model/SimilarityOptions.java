package com.lds_api.model;

/**
 * 
 * @author Alexandre DEBEDDES
 *
 */
public class SimilarityOptions {
	private boolean benchmark;
	private String benchmarkName;
	private String correlationType;
	private int threads;
	private boolean useIndex;
	private String measureType;
	
	public SimilarityOptions() {}

	public boolean isBenchmark() {
		return benchmark;
	}

	public void setBenchmark(boolean benchmark) {
		this.benchmark = benchmark;
	}

	public String getBenchmarkName() {
		return benchmarkName;
	}

	public void setBenchmarkName(String benchmarkName) {
		this.benchmarkName = benchmarkName;
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
	
	public String getCorrelationType() {
		return correlationType;
	}

	public void setCorrelationType(String correlationType) {
		this.correlationType = correlationType;
	}

	@Override
	public String toString() {
		return "Options [benchmark=" + benchmark + ", threads=" + threads + ", useIndex=" + useIndex + ", measureType="
				+ measureType /*+ ", correlation=" + correlation*/ + "]";
	}
	
	
}
