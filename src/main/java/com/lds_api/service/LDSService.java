package com.lds_api.service;

import lombok.Data;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.springframework.stereotype.Service;

import com.lds_api.model.SimilarityParameters;
import com.lds_api.model.SimilarityResult;

import au.com.bytecode.opencsv.CSVWriter;

import com.lds_api.model.LdDatasetMain;
import com.lds_api.model.Resources;
import com.lds_api.model.Result;
import com.lds_api.model.Options;

import lds.benchmark.BenchmarkFile;
import lds.benchmark.Correlation;
import lds.benchmark.LdBenchmark;
import lds.config.Config;
import lds.config.ConfigParam;
import lds.config.LdConfigFactory;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import lds.measures.lods.SimI;
import lds.measures.lods.ontologies.O;
import lds.measures.lods.ontologies.O_DBpedia;
import lds.measures.lods.ontologies.O_DBpedia_de;
import lds.measures.lods.ontologies.O_DBpedia_fr;
import lds.measures.lods.ontologies.O_Yago;
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

	public SimilarityResult LDSimilarity(SimilarityParameters params) throws Exception{
		SimilarityResult simRes = new SimilarityResult();
		ArrayList<Result> data = new ArrayList<Result>();
		LdSimilarityEngine engine = loadEngine(params.getLdDatasetMain() , params.getOptions());
			if(params.getOptions().isBenchmark()) {
				FileWriter csvWriter = new FileWriter("bench.csv");
				CSVWriter writer = new CSVWriter(csvWriter);
				for(Resources r: params.getResources()) {
					csvWriter.append(r.getResource1());
					csvWriter.append(",");
					csvWriter.append(r.getResource2());
					csvWriter.append(",");
					csvWriter.append(String.valueOf(r.getBenchmark()));
					csvWriter.append("\n");
					
				}
				csvWriter.flush();
				//csvWriter.close();
				writer.close();

				Path benchPath = Paths.get("bench.csv");
				String benchFile = benchPath.toAbsolutePath().toString();
				
				BenchmarkFile source = new BenchmarkFile(benchFile , ',' , '"');

				LdBenchmark benchmark = new LdBenchmark(source);

				benchmark.setCorrelationMethod(Correlation.PearsonCorrelation);

				loadEngine(params.getLdDatasetMain() , params.getOptions());


				double correlation = engine.correlation(benchmark, params.getOptions().getThreads());
				
				csvWriter = new FileWriter("bench_Results.csv");
				
				Path benchResultPath = Paths.get("bench_Results.csv");
				String benchResultFile = benchResultPath.toAbsolutePath().toString();
				
				BufferedReader reader = new BufferedReader(new FileReader("bench_Results.csv"));
				
				String line;
			    
			    while ((line = reader.readLine()) != null) {
			    	String[] r = line.split(",");
			    	Result res = new Result();
			    	res.setResource1(r[0]);
			    	res.setResource2(r[1]);
			    	res.setScore(Double.parseDouble(r[2]));
			    	data.add(res);
			        System.out.println(line);
			    }
			    reader.close();
			    
			    Result res = new Result();
				res.setResource1("Correlation");
				res.setResource2("");
				res.setScore(correlation);
				data.add(res);
				csvWriter.append("Correlation");
				csvWriter.append(",");
				csvWriter.append("");
				csvWriter.append(",");
				csvWriter.append(String.valueOf(correlation));
				csvWriter.append(",\n");

				csvWriter.flush();
				csvWriter.close();
				
				File file = new File(benchResultFile);
				file.delete();
				
				Path benchResultDurationPath = Paths.get("bench_Results_Duration.csv");
				String benchResultDurationFile = benchResultDurationPath.toAbsolutePath().toString();
				
				File file1 = new File(benchResultDurationFile);
				file1.delete();
			}
			else {
				if(params.getResources().size()>1) {
					for(Resources r: params.getResources()) {
						String sR1 = params.getLdDatasetMain().getBaseResourceURL()+r.getResource1();
						String sR2 = params.getLdDatasetMain().getBaseResourceURL()+r.getResource2();

						R r1 = new R(sR1);
						R r2 = new R(sR2);

						double score = engine.similarity(r1, r2);

						Result res = new Result();
						res.setResource1(r.getResource1());
						res.setResource2(r.getResource2());
						res.setScore(score);

						data.add(res);
					}
				}
				else {
					String sR1 = params.getLdDatasetMain().getBaseResourceURL()+params.getResources().get(0).getResource1();
					String sR2 = params.getLdDatasetMain().getBaseResourceURL()+params.getResources().get(0).getResource2();

					R r1 = new R(sR1);
					R r2 = new R(sR2);

					double score = engine.similarity(r1, r2);

					Result res = new Result();
					res.setResource1(params.getResources().get(0).getResource1());
					res.setResource2(params.getResources().get(0).getResource2());
					res.setScore(score);

					data.add(res);
				}
			}
			engine.close();
		simRes.setData(data);
		return simRes;
	}

	//public 
	public LdSimilarityEngine loadEngine(LdDatasetMain LdDatasetMain ,Options options) throws Exception {
		LdSimilarityEngine engine = new LdSimilarityEngine();
		switch(options.getMeasureType()) {
		case "Resim":

			Config configResim = LDSimilarityConfig(LdDatasetMain , options);
			engine.load(Measure.Resim , configResim);
			break;
		case "EPICS":
			Config configEPICS = LDSimilarityConfig(LdDatasetMain , options);
			engine.load(Measure.EPICS , configEPICS);
			break;
		case "LDSD":
			Config configLDSD = LDSimilarityConfig(LdDatasetMain , options);
			engine.load(Measure.LDSD_cw , configLDSD);
			break;
		case "PICSS":
			Config configPICSS = LDSimilarityConfig(LdDatasetMain , options);
			engine.load(Measure.PICSS , configPICSS);
			break;
		case "SimP":
			Config configSimP = LDSimilarityConfig(LdDatasetMain , options);
			engine.load(Measure.SimP , configSimP);
			break;
		default:

			break;

		}
		return engine;
	}

	public Config LDSimilarityConfig(LdDatasetMain LdDatasetMain ,Options options) throws Exception {
		Config config = new Config();

		PrefixMapping prefixes = new PrefixMappingImpl();
		prefixes.setNsPrefix("xsd", LdDatasetMain.getPrefixes().getNsPrefixMap().getXsd());
		prefixes.setNsPrefix("rdfs", LdDatasetMain.getPrefixes().getNsPrefixMap().getRdfs());
		prefixes.setNsPrefix("dbpedia", LdDatasetMain.getPrefixes().getNsPrefixMap().getDbpedia());
		prefixes.setNsPrefix("dbpediaowl", LdDatasetMain.getPrefixes().getNsPrefixMap().getDbpediaowl());
		prefixes.setNsPrefix("rdf", LdDatasetMain.getPrefixes().getNsPrefixMap().getRdf());

		LdDataset dataSetMain = LdDatasetFactory.getInstance().service(LdDatasetMain.getLink()).name(LdDatasetMain.getName()).defaultGraph(LdDatasetMain.getDefaultGraph()).prefixes(prefixes).create();
		config.addParam(ConfigParam.LdDatasetMain, dataSetMain);

		if(options.isUseIndex()) 
			config.addParam(ConfigParam.useIndexes, true);
		else 
			config.addParam(ConfigParam.useIndexes, false);

		if(options.getMeasureType().equals("PICSS") || options.getMeasureType().equals("EPICS") || options.getMeasureType().equals("SimP"))
			config.addParam(ConfigParam.resourcesCount , 2350906);

		if(options.getMeasureType().equals("SimI"))
			config.addParam(ConfigParam.dataAugmentation, true);

		if(options.getMeasureType().contains("W"))
			config.addParam(ConfigParam.WeightMethod , WeightMethod.ITW);

		return config;
	}

}
