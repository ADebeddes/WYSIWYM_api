package com.lds_api.model;

/**
 * 
 * @author Alexandre DEBEDDES
 *
 */
public class Resources {
	private String resource1;
	private String resource2;
	private double benchmark;
	
	public Resources() {}

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

	public double getBenchmark() {
		return benchmark;
	}

	public void setBenchmark(double benchmark) {
		this.benchmark = benchmark;
	}

	@Override
	public String toString() {
		return "Paire [resource1=" + resource1 + ", resource2=" + resource2 + ", benchmark=" + benchmark + "]";
	}

}
