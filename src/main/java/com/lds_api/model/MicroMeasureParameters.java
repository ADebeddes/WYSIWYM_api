package com.lds_api.model;

import java.util.List;

public class MicroMeasureParameters {

	private LdDatasetMain ldDatasetMain;
	private List<MicroMeasureResources> microMeasureResources;
	private MicroMeasureOptions options;
	
	public MicroMeasureParameters() {
		
	}

	public LdDatasetMain getLdDatasetMain() {
		return ldDatasetMain;
	}
	
	public void setLdDatasetMain(LdDatasetMain ldDatasetMain) {
		this.ldDatasetMain = ldDatasetMain;
	}
	
	public List<MicroMeasureResources> getResources() {
		return microMeasureResources;
	}

	public void setResources(List<MicroMeasureResources> microMeasureResources) {
		this.microMeasureResources = microMeasureResources;
	}
	
	public MicroMeasureOptions getOptions() {
		return options;
	}

	public void setProperty(MicroMeasureOptions options) {
		this.options = options;
	}
	
}
