package com.lds_api.model;

import java.util.List;

/**
 * 
 * @author Alexandre DEBEDDES
 *
 */
public class SimilarityParameters {
	
	private LdDatasetMain ldDatasetMain;
	private List<Resources> resources;
	private String uri;
	private boolean json;
	private String method;
	private String headers;
	private Options options;
	
	public SimilarityParameters() {}
	
	public void setLdDatasetMain(LdDatasetMain ldDatasetMain) {
		this.ldDatasetMain = ldDatasetMain;
	}
	
	public void setResources(List<Resources> resources) {
		this.resources = resources;
	}
	
	public void setOptions(Options options) {
		this.options =options;
	}
	
	public LdDatasetMain getLdDatasetMain() {
		return ldDatasetMain;
	}
	
	public List<Resources> getResources() {
		return resources;
	}
	
	public Options getOptions() {
		return options;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public boolean isJson() {
		return json;
	}

	public void setJson(boolean json) {
		this.json = json;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}

	@Override
	public String toString() {
		return "SimilarityParameters [ldDatasetMain=" + ldDatasetMain + ", resources=" + resources + ", uri=" + uri
				+ ", json=" + json + ", method=" + method + ", headers=" + headers + ", options=" + options + "]";
	}
	
	
}
