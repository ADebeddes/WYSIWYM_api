package com.lds_api.model;

/**
 * 
 * @author Alexandre DEBEDDES
 *
 */
public class LdDatasetMain {
	private String name;
	private Prefixes prefixes;
	private String link;
	private String defaultGraph;
	private String baseResourceURL;
	
	public LdDatasetMain() {}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		System.out.println(name);
		this.name = name;
	}
	public Prefixes getPrefixes() {
		return prefixes;
	}
	public void setPrefixes(Prefixes prefixes) {
		System.out.println(prefixes);
		this.prefixes = prefixes;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		System.out.println(link);
		this.link = link;
	}
	public String getDefaultGraph() {
		return defaultGraph;
	}
	public void setDefaultGraph(String defaultGraph) {
		System.out.println(defaultGraph);
		this.defaultGraph = defaultGraph;
	}
	public String getBaseResourceURL() {
		return baseResourceURL;
	}
	public void setBaseResourceURL(String baseResourceURL) {
		System.out.println(baseResourceURL);
		this.baseResourceURL = baseResourceURL;
	}

	@Override
	public String toString() {
		return "LdDatasetMain [name=" + name + ", prefixes=" + prefixes.toString() + ", link=" + link + ", defaultGraph="
				+ defaultGraph + ", baseResourceURL=" + baseResourceURL + "]";
	}
	
}
