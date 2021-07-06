package com.lds_api.service;

import lombok.Data;
import slib.sml.sm.core.measures.vector.CosineSimilarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVWriter;
import info.debatty.java.stringsimilarity.Damerau;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.Levenshtein;
import info.debatty.java.stringsimilarity.LongestCommonSubsequence;
import info.debatty.java.stringsimilarity.NGram;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.OptimalStringAlignment;
import info.debatty.java.stringsimilarity.QGram;
import info.debatty.java.stringsimilarity.RatcliffObershelp;
import info.debatty.java.stringsimilarity.SorensenDice;
import info.debatty.java.stringsimilarity.interfaces.StringDistance;
//import info.debatty.java.stringsimilarity.*;
import ldq.LdDataset;
import ldq.LdDatasetFactory;

import com.lds_api.model.SimilarityParameters;
import com.lds_api.model.SimilarityResult;
import com.lds_api.model.LdDatasetMain;
import com.lds_api.model.MicroMeasureParameters;
import com.lds_api.model.MicroMeasureResources;
import com.lds_api.model.SimilarityResources;
import com.lds_api.model.Result;
import com.lds_api.model.SimilarityOptions;

import lds.LdManager.ontologies.Ontology;
import lds.benchmark.BenchmarkFile;
import lds.benchmark.Correlation;
import lds.benchmark.LdBenchmark;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import lds.measures.weight.WeightMethod;
import lds.resource.R;

/***
 * 
 * @author Alexandre DEBEDDES
 *
 */

@Data
@Service
public class LDSService {

	public SimilarityResult newMeasure(MicroMeasureParameters params) throws Exception{
		//JaroWinkler l = new JaroWinkler();
		SimilarityResult simRes = new SimilarityResult();
		ArrayList<Result> data = new ArrayList<Result>();

		ArrayList<String> edges1 = new ArrayList<String>();
		ArrayList<String> edges2 = new ArrayList<String>();

		PrefixMapping prefixes = new PrefixMappingImpl();

		prefixes.setNsPrefix("xsd", params.getLdDatasetMain().getPrefixes().getNsPrefixMap().getXsd());
		prefixes.setNsPrefix("rdfs", params.getLdDatasetMain().getPrefixes().getNsPrefixMap().getRdfs());
		prefixes.setNsPrefix("dbpedia", params.getLdDatasetMain().getPrefixes().getNsPrefixMap().getDbpedia());
		prefixes.setNsPrefix("dbo", params.getLdDatasetMain().getPrefixes().getNsPrefixMap().getDbpediaowl());
		prefixes.setNsPrefix("rdf", params.getLdDatasetMain().getPrefixes().getNsPrefixMap().getRdf());

		LdDataset dataSetMain = LdDatasetFactory.getInstance().service(params.getLdDatasetMain().getLink()).name(params.getLdDatasetMain().getName()).defaultGraph(params.getLdDatasetMain().getDefaultGraph()).prefixes(prefixes).create();

		Ontology.loadIndexes();

		for(MicroMeasureResources r: params.getResources()) {

			String sR1 = params.getLdDatasetMain().getBaseResourceURL()+r.getResource1();
			String sR2 = params.getLdDatasetMain().getBaseResourceURL()+r.getResource2();

			R r1 = new R(sR1);
			R r2 = new R(sR2);

			edges1 = (ArrayList<String>) getProperty(r1,dataSetMain,params.getResources().get(0).getProperty());
			edges2 = (ArrayList<String>) getProperty(r2,dataSetMain,params.getResources().get(0).getProperty());
			
			if(edges1 == null || edges2 == null) {
				simRes.setMessage("Unknown property");
				return simRes;
			}
			
			double score = 0.0;
			
			if(edges1.size() == 1 && edges2.size() == 1) {
				if(params.getOptions().getMeasureType().equals("int")) {
					String[] tab1 = edges1.get(0).split("\"");
					String[] tab2 = edges2.get(0).split("\"");
					score = IntMeasure(Double.parseDouble(tab1[1]),Double.parseDouble(tab2[1]),params.getOptions().getMeasureType());
				}
				else {
					score = StringMeasure(edges1.get(0),edges2.get(0),params.getOptions().getMeasureType());
				}
			}
			else 
				score = ListMeasure(edges1,edges2,params.getOptions().getMeasureType());
			Result res = new Result();
			res.setResource1(r.getResource1());
			res.setResource2(r.getResource2());
			res.setScore(score);

			data.add(res);
		}
		simRes.setData(data);
		return simRes;
	}
	
	public SimilarityResult LDSimilarity(SimilarityParameters params) throws Exception{
		SimilarityResult simRes = new SimilarityResult();
		ArrayList<Result> data = new ArrayList<Result>();
		LdSimilarityEngine engine = loadEngine(params.getLdDatasetMain() , params.getOptions());
		UUID uuid = UUID.randomUUID();
		if(params.getOptions().isBenchmark()) {
			if(params.getOptions().getBenchmarkName().equals("none")) {
				FileWriter csvWriter = new FileWriter("tmp/"+uuid.toString()+".csv");
				CSVWriter writer = new CSVWriter(csvWriter);
				for(SimilarityResources r: params.getResources()) {
					csvWriter.append(r.getResource1());
					csvWriter.append(",");
					csvWriter.append(r.getResource2());
					csvWriter.append(",");
					csvWriter.append(String.valueOf(r.getBenchmark()));
					csvWriter.append("\n");

				}
				csvWriter.flush();
				writer.close();

				Path benchPath = Paths.get("tmp/"+uuid.toString()+".csv");
				String benchStringPath = benchPath.toAbsolutePath().toString();

				BenchmarkFile source = new BenchmarkFile(benchStringPath , ',' , '"');

				LdBenchmark benchmark = new LdBenchmark(source);

				if(params.getOptions().getCorrelationType().equals("spearman"))
					benchmark.setCorrelationMethod(Correlation.SpearmanCorrelation);
				if(params.getOptions().getCorrelationType().equals("pearson"))
					benchmark.setCorrelationMethod(Correlation.PearsonCorrelation);


				double correlation = engine.correlation(benchmark, params.getOptions().getThreads());


				Path benchResultPath = Paths.get("tmp/"+uuid.toString()+"_Results.csv");
				String benchResultStringPath = benchResultPath.toAbsolutePath().toString();
				BufferedReader reader = new BufferedReader(new FileReader(benchResultStringPath));

				String line;

				while ((line = reader.readLine()) != null) {
					Result res = new Result();

					String[] r = line.split(",");

					String[] r1 =r[0].split(params.getLdDatasetMain().getBaseResourceURL());
					r1 = r1[1].split("\"");

					String[] r2 =r[1].split(params.getLdDatasetMain().getBaseResourceURL());
					r2 = r2[1].split("\"");

					res.setResource1(r1[0]);
					res.setResource2(r2[0]);
					res.setScore(Double.parseDouble(r[2]));
					data.add(res);
				}
				reader.close();

				Result res = new Result();
				res.setResource1("Correlation");
				res.setResource2("");
				res.setScore(correlation);
				data.add(res);

				Path benchResultDurationPath = Paths.get("tmp/"+uuid.toString()+"_Results_Duration.csv");
				String benchResultDurationStringPath = benchResultDurationPath.toAbsolutePath().toString();

				File benchFile = new File(benchStringPath);
				benchFile.delete();

				File benchResultFile = new File(benchResultStringPath);
				benchResultFile.delete();

				File benchResultDurationFile = new File(benchResultDurationStringPath);
				benchResultDurationFile.delete();

			}
			else {
				Path benchPath = Paths.get("");
				String benchStringPath = "";
				switch(params.getOptions().getBenchmarkName()) {
				case "mc30":
					benchStringPath = benchPath.toAbsolutePath().toString()+"/src/test/resources/benchmarks/mc-30/mc-30_DBpedia.csv";
					break;
				case "rg65":
					benchStringPath = benchPath.toAbsolutePath().toString()+"/src/test/resources/benchmarks/rg-65/rg-65_DBpedia.csv";
					break;
				case "wordsim353":
					benchStringPath = benchPath.toAbsolutePath().toString()+"/src/test/resources/benchmarks/wordsim-353/wordsim-353_DBpedia.txt";
					break;
				}
				//System.out.println(benchStringPath);
				BenchmarkFile source = new BenchmarkFile(benchStringPath , ',' , '"');

				LdBenchmark benchmark = new LdBenchmark(source);

				if(params.getOptions().getCorrelationType().equals("spearman"))
					benchmark.setCorrelationMethod(Correlation.SpearmanCorrelation);
				if(params.getOptions().getCorrelationType().equals("pearson"))
					benchmark.setCorrelationMethod(Correlation.PearsonCorrelation);

				double correlation = engine.correlation(benchmark, params.getOptions().getThreads());

				Path benchResultPath = Paths.get("");
				String benchResultStringPath = "";
				switch(params.getOptions().getBenchmarkName()) {
				case "mc30":
					benchResultStringPath = benchResultPath.toAbsolutePath().toString()+"/src/test/resources/benchmarks/mc-30/mc-30_DBpedia_Results.csv";
					break;
				case "rg65":
					benchResultStringPath = benchResultPath.toAbsolutePath().toString()+"/src/test/resources/benchmarks/rg-65/rg-65_DBpedia_Results.csv";
					break;
				case "wordsim353":
					benchResultStringPath = benchResultPath.toAbsolutePath().toString()+"/src/test/resources/benchmarks/wordsim-353/wordsim-353_DBpedia_Results.txt";
					break;
				}

				BufferedReader reader = new BufferedReader(new FileReader(benchResultStringPath));

				String line;

				while ((line = reader.readLine()) != null) {
					Result res = new Result();

					String[] r = line.split(",");

					String[] r1 =r[0].split(params.getLdDatasetMain().getBaseResourceURL());
					r1 = r1[1].split("\"");

					String[] r2 =r[1].split(params.getLdDatasetMain().getBaseResourceURL());
					r2 = r2[1].split("\"");

					res.setResource1(r1[0]);
					res.setResource2(r2[0]);
					res.setScore(Double.parseDouble(r[2]));
					data.add(res);
				}
				reader.close();

				Result res = new Result();
				res.setResource1("Correlation");
				res.setResource2("");
				res.setScore(correlation);
				data.add(res);

				Path benchResultDurationPath = Paths.get("");
				String benchResultDurationStringPath = "";
				switch(params.getOptions().getBenchmarkName()) {
				case "mc30":
					benchResultDurationStringPath = benchResultDurationPath.toAbsolutePath().toString()+"/src/test/resources/benchmarks/mc-30/mc-30_DBpedia_Results_Duration.csv";
					break;
				case "rg65":
					benchResultDurationStringPath = benchResultDurationPath.toAbsolutePath().toString()+"/src/test/resources/benchmarks/rg-65/rg-65_DBpedia_Results_Duration.csv";
					break;
				case "wordsim353":
					benchResultDurationStringPath = benchResultDurationPath.toAbsolutePath().toString()+"/src/test/resources/benchmarks/wordsim-353/wordsim-353_DBpedia_Results_Duration.txt";
					break;
				}

				File benchResultFile = new File(benchResultStringPath);
				benchResultFile.delete();

				File benchResultDurationFile = new File(benchResultDurationStringPath);
				benchResultDurationFile.delete();
			}
		}
		else {

			for(SimilarityResources r: params.getResources()) {
				String sR1 = params.getLdDatasetMain().getBaseResourceURL()+r.getResource1();
				String sR2 = params.getLdDatasetMain().getBaseResourceURL()+r.getResource2();
				R r1 = new R(sR1);
				R r2 = new R(sR2);
				//System.out.println("similarity start");
				double score = engine.similarity(r1, r2);
				//System.out.println("similarity end");
				Result res = new Result();
				res.setResource1(r.getResource1());
				res.setResource2(r.getResource2());
				res.setScore(score);

				data.add(res);
			}
		}
		engine.close();
		simRes.setData(data);
		return simRes;
	}

	public LdSimilarityEngine loadEngine(LdDatasetMain LdDatasetMain ,SimilarityOptions similarityOptions) throws Exception {
		LdSimilarityEngine engine = new LdSimilarityEngine();
		Config config = LDSimilarityConfig(LdDatasetMain , similarityOptions);
		switch(similarityOptions.getMeasureType()) {
		case "Resim":
			engine.load(Measure.Resim , config);
			break;
		case "TResim":
			engine.load(Measure.TResim , config);
			break;
		case "WResim":
			engine.load(Measure.WResim , config);
			break;
		case "WTResim":
			engine.load(Measure.WTResim , config);
			break;
		case "LDSD_d":
			engine.load(Measure.LDSD_d , config);
			break;
		case "LDSD_dw":
			engine.load(Measure.LDSD_dw , config);
			break;
		case "LDSD_i":
			engine.load(Measure.LDSD_i , config);
			break;
		case "LDSD_iw":
			engine.load(Measure.LDSD_iw , config);
			break;
		case "LDSD_cw":
			engine.load(Measure.LDSD_cw , config);
			break;
		case "TLDSD_cw":
			engine.load(Measure.TLDSD_cw , config);
			break;
		case "WLDSD_cw ":
			engine.load(Measure.WLDSD_cw , config);
			break;
		case "WTLDSD_cw ":
			engine.load(Measure.WTLDSD_cw , config);
			break;
		case "PICSS":
			engine.load(Measure.PICSS , config);
			break;
		case "EPICS":
			engine.load(Measure.EPICS , config);
			break;
		case "LODS":
			engine.load(Measure.LODS , config);
			break;
		case "SimI":
			engine.load(Measure.SimI , config);
			break;
		case "SimP":
			engine.load(Measure.SimP , config);
			break;
		case "SimC":
			engine.load(Measure.SimC , config);
			break;
		default:

			break;

		}
		return engine;
	}

	public Config LDSimilarityConfig(LdDatasetMain LdDatasetMain ,SimilarityOptions similarityOptions) throws Exception {
		Config config = new Config();

		PrefixMapping prefixes = new PrefixMappingImpl();
		prefixes.setNsPrefix("xsd", LdDatasetMain.getPrefixes().getNsPrefixMap().getXsd());
		prefixes.setNsPrefix("rdfs", LdDatasetMain.getPrefixes().getNsPrefixMap().getRdfs());
		prefixes.setNsPrefix("dbpedia", LdDatasetMain.getPrefixes().getNsPrefixMap().getDbpedia());
		prefixes.setNsPrefix("dbpediaowl", LdDatasetMain.getPrefixes().getNsPrefixMap().getDbpediaowl());
		prefixes.setNsPrefix("rdf", LdDatasetMain.getPrefixes().getNsPrefixMap().getRdf());

		LdDataset dataSetMain = LdDatasetFactory.getInstance().service(LdDatasetMain.getLink()).name(LdDatasetMain.getName()).defaultGraph(LdDatasetMain.getDefaultGraph()).prefixes(prefixes).create();
		config.addParam(ConfigParam.LdDatasetMain, dataSetMain);

		if(similarityOptions.isUseIndex()) 
			config.addParam(ConfigParam.useIndexes, true);
		else 
			config.addParam(ConfigParam.useIndexes, false);

		if(similarityOptions.getMeasureType().equals("PICSS") || similarityOptions.getMeasureType().equals("EPICS") || similarityOptions.getMeasureType().equals("SimP"))
			config.addParam(ConfigParam.resourcesCount , 2350906);

		if(similarityOptions.getMeasureType().equals("SimI"))
			config.addParam(ConfigParam.dataAugmentation, true);

		if(similarityOptions.getMeasureType().contains("W"))
			config.addParam(ConfigParam.WeightMethod , WeightMethod.ITW);

		return config;
	}

	public List<String> getProperty(R a, LdDataset dataSetMain, String request) {
		List<String> propertyList = new ArrayList<String>();
		
		String object , property;

		ParameterizedSparqlString query_cmd = dataSetMain.prepareQuery();

		query_cmd.setCommandText("select ?"+request+"\n"
				+ "from <" + dataSetMain.getDefaultGraph()+ "> \n"
				+ "where {<" + a.getUri() + "> <http://dbpedia.org/ontology/"+ request +"> ?"+request+" .}");

		ResultSet resultSet = dataSetMain.executeSelectQuery(query_cmd.toString());

		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.nextSolution();
			if(request.equals("abstract")) {
				if(qs.toString().contains("@en"))
					propertyList.add(qs.toString());
			}
			else {
				propertyList.add(qs.toString());
			}
		}
		dataSetMain.close();

		if(! propertyList.isEmpty())
			return propertyList;
		else {
			query_cmd.setCommandText("select distinct ?object ?property " + (dataSetMain.getDefaultGraph() == null ? ("") : "from <" + dataSetMain.getDefaultGraph()+ ">") + " where {<" + a.getUri() + "> ?property ?object ."
	                + " filter(isuri(?object)) }");


	        ResultSet resultSet1 = dataSetMain.executeSelectQuery(query_cmd.toString());

	        while (resultSet1.hasNext()) {
	        	QuerySolution qs = resultSet1.nextSolution();
	            object = Ontology.compressValue(qs.getResource("object"));
	            property = Ontology.compressValue(qs.getResource("property"));
	            if(property.contains(request))
	            	propertyList.add(object);
			}
	        if(! propertyList.isEmpty())
				return propertyList;
			else
				return null;
		}
	} 

	public double StringMeasure(String r1,String r2,String measure) {
		switch(measure) {
		case "levenshtein":
			Levenshtein  l = new Levenshtein ();
			return l.distance(r1, r2);
		case "normalizedLevenshtein":
			NormalizedLevenshtein  nl = new NormalizedLevenshtein ();
			return nl.distance(r1, r2);
		case "damerauLevenshtein ":
			Damerau d = new Damerau();
			return d.distance(r1, r2);
		case "optimalStringAligment":
			OptimalStringAlignment osa = new OptimalStringAlignment();
			return osa.distance(r1, r2);
		case "jaroWinkler":
			JaroWinkler jw = new JaroWinkler();
			return jw.distance(r1, r2);
		case "longestCommonSubsequence ":
			LongestCommonSubsequence lcs = new LongestCommonSubsequence();
			return lcs.distance(r1, r2);
		case "metricLongestCommonSubsequence":
			info.debatty.java.stringsimilarity.MetricLCS mlcs = 
            new info.debatty.java.stringsimilarity.MetricLCS();
			return mlcs.distance(r1, r2);
		case "nGram":
			NGram twogram = new NGram(2);
			return twogram.distance(r1, r2);
		case "qGram":
			QGram qGram =new QGram();
			return qGram.distance(r1, r2);
		case "cosineSimilarity":
			CosineSimilarity cSim =new CosineSimilarity();
			return ((StringDistance) cSim).distance(r1, r2);
		case "jaccardIndex":
			Jaccard jInd =new Jaccard();
			return jInd.distance(r1, r2);
		case "sorensenDiceCoefficient":
			SorensenDice sDice =new SorensenDice();
			return sDice.distance(r1, r2);
		case "ratcliffObershelp":
			RatcliffObershelp  ro =new RatcliffObershelp ();
			return ro.distance(r1, r2);
		default:
			return 0.0;
		}
	}
	
	public double IntMeasure(double r1, double r2, String measure) {
		double score = 0.0;
		if(r1<r2)
			score = (r2 - r1) / r2 * 100;
		else 
			score = (r1 - r2) / r1 * 100;
		return score/100;
	}
	
	private double ListMeasure(List<String> r1,List<String> r2,String measure) {
		boolean isIn = false;
		double match = 0;
		double mismatch = 0;
		for(String s1: r1) {
			
			for(String s2: r2) {
				if(s1.equals(s2)) {
					match++;
					isIn = true;
				}
			}
			if(!isIn) 
				mismatch++;
			else 
				isIn = false;
		}

		double score = match * 100 / (match+mismatch+((double)r2.size()-match));
		return score/100;
	}
}
