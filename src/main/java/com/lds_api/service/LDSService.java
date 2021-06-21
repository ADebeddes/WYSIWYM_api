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
import java.util.UUID;

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
		UUID uuid = UUID.randomUUID();
		if(params.getOptions().isBenchmark()) {
			if(params.getOptions().getBenchmarkName().equals("none")) {
				FileWriter csvWriter = new FileWriter("tmp/"+uuid.toString()+".csv");
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
				writer.close();

				Path benchPath = Paths.get("tmp/"+uuid.toString()+".csv");
				String benchFile = benchPath.toAbsolutePath().toString();

				BenchmarkFile source = new BenchmarkFile(benchFile , ',' , '"');

				LdBenchmark benchmark = new LdBenchmark(source);

				if(params.getOptions().getCorrelationType().equals("spearman"))
					benchmark.setCorrelationMethod(Correlation.SpearmanCorrelation);
				if(params.getOptions().getCorrelationType().equals("pearson"))
					benchmark.setCorrelationMethod(Correlation.PearsonCorrelation);

				
				double correlation = engine.correlation(benchmark, params.getOptions().getThreads());


				Path benchResultPath = Paths.get("tmp/"+uuid.toString()+"_Results.csv");
				String benchResultFile = benchResultPath.toAbsolutePath().toString();
				BufferedReader reader = new BufferedReader(new FileReader(benchResultFile));

				String line;

				while ((line = reader.readLine()) != null) {
					String[] r = line.split(",");
					Result res = new Result();
					res.setResource1(r[0]);
					res.setResource2(r[1]);
					res.setScore(Double.parseDouble(r[2]));
					data.add(res);
				}
				reader.close();

				Result res = new Result();
				res.setResource1("Correlation");
				res.setResource2("");
				res.setScore(correlation);
				data.add(res);

				csvWriter = new FileWriter("tmp/"+uuid.toString()+"_Results.csv");

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

				Path benchResultDurationPath = Paths.get("tmp/"+uuid.toString()+"_Results_Duration.csv");
				String benchResultDurationFile = benchResultDurationPath.toAbsolutePath().toString();

				File file1 = new File(benchResultDurationFile);
				file1.delete();

				File file2 = new File(benchFile);
				file2.delete();
			}
			else {
				Path benchPath = Paths.get("");
				String benchFile = "";
				switch(params.getOptions().getBenchmarkName()) {
				case "mc30":
					benchFile = benchPath.toAbsolutePath().toString()+"\\src\\test\\resources\\benchmarks\\mc-30\\mc-30_DBpedia.csv";
					break;
				case "rg65":
					benchFile = benchPath.toAbsolutePath().toString()+"\\src\\test\\resources\\benchmarks\\rg-65\\rg-65.csv";
					break;
				case "wordsim353":
					benchFile = benchPath.toAbsolutePath().toString()+"\\src\\test\\resources\\benchmarks\\wordsim-353\\wordsim-353.csv";
					break;
				}

				BenchmarkFile source = new BenchmarkFile(benchFile , ',' , '"');

				LdBenchmark benchmark = new LdBenchmark(source);
				
				if(params.getOptions().getCorrelationType().equals("spearman"))
					benchmark.setCorrelationMethod(Correlation.SpearmanCorrelation);
				if(params.getOptions().getCorrelationType().equals("pearson"))
					benchmark.setCorrelationMethod(Correlation.PearsonCorrelation);
				
				double correlation = engine.correlation(benchmark, params.getOptions().getThreads());
			}
		}
		else {

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
		engine.close();
		simRes.setData(data);
		return simRes;
	}

	//public 
	public LdSimilarityEngine loadEngine(LdDatasetMain LdDatasetMain ,Options options) throws Exception {
		LdSimilarityEngine engine = new LdSimilarityEngine();
		Config config = LDSimilarityConfig(LdDatasetMain , options);
		switch(options.getMeasureType()) {
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
