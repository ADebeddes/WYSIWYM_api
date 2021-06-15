package com.lds_api.model;

/**
 * 
 * @author Alexandre DEBEDDES
 *
 */
public class Prefixes {
	private NsPrefixMap nsPrefixMap;
	
	public Prefixes() {}
	
	public NsPrefixMap getNsPrefixMap() {
		return nsPrefixMap;
	}
	public void setNsPrefixMap(NsPrefixMap nsPrefixMap) {
		this.nsPrefixMap = nsPrefixMap;
	}

	@Override
	public String toString() {
		return "Prefixes [nsPrefixMap=" + nsPrefixMap.toString() + "]";
	}
	
}
