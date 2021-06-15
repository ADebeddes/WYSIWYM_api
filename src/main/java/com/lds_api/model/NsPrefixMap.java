package com.lds_api.model;

/**
 * 
 * @author Alexandre DEBEDDES
 *
 */
public class NsPrefixMap {
	private String xsd;
	private String rdfs;
	private String dbpedia;
	private String dbpediaowl;
	private String rdf;
	
	public NsPrefixMap() {}

	public String getXsd() {
		return xsd;
	}

	public void setXsd(String xsd) {
		this.xsd = xsd;
	}

	public String getRdfs() {
		return rdfs;
	}

	public void setRdfs(String rdfs) {
		this.rdfs = rdfs;
	}

	public String getDbpedia() {
		return dbpedia;
	}

	public void setDbpedia(String dbpedia) {
		this.dbpedia = dbpedia;
	}

	public String getDbpediaowl() {
		return dbpediaowl;
	}

	public void setDbpediaowl(String dbpediaowl) {
		this.dbpediaowl = dbpediaowl;
	}

	public String getRdf() {
		return rdf;
	}

	public void setRdf(String rdf) {
		this.rdf = rdf;
	}

	@Override
	public String toString() {
		return "NsPrefixMap [xsd=" + xsd + ", rdfs=" + rdfs + ", dbpedia=" + dbpedia + ", dbpediaowl=" + dbpediaowl
				+ ", rdf=" + rdf + "]";
	}
	
}
