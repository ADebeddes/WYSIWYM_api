package com.lds_api.service;

import lombok.Data;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.springframework.stereotype.Service;

import com.lds_api.model.SimilarityParameters;
import com.lds_api.model.SimilarityResult;
import com.lds_api.model.LdDatasetMain;
import com.lds_api.model.Resources;
import com.lds_api.model.Result;
import com.lds_api.model.Options;

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
		if(params.getOptions().getMeasureType().equals("SimI")) {
			Config config = LDSimilarityConfig(params.getLdDatasetMain(),params.getOptions());
			List<O> ontologyList = new ArrayList<>();
	        
	        O dbpedia = new O_DBpedia();
	        ontologyList.add(dbpedia);
	        
	        O dbpedia_de = new O_DBpedia_de();
	        ontologyList.add(dbpedia_de);
	        
	        O dbpedia_fr = new O_DBpedia_fr();
	        ontologyList.add(dbpedia_fr);
	        
	        O yago = new O_Yago();
	        ontologyList.add(yago);
	        
	        config.addParam(ConfigParam.ontologyList, ontologyList);
	        SimI simi = new SimI(config);
	        
	        simi.loadIndexes();
	        if(params.getOptions().isBenchmark()) {
	        	FileWriter csvWriter = new FileWriter("bench.csv");
				for(Resources r: params.getResources()) {
					csvWriter.append(r.getResource1());
					csvWriter.append(",");
					csvWriter.append(r.getResource2());
					csvWriter.append(",");
					csvWriter.append(String.valueOf(r.getBenchmark()));
					csvWriter.append("\n");

				}
				csvWriter.flush();
				csvWriter.close();
	        }
	        else {
	        	if(params.getResources().size()>1) {
					for(Resources r: params.getResources()) {
						//System.out.println("java");
						String sR1 = params.getLdDatasetMain().getBaseResourceURL()+r.getResource1();
						String sR2 = params.getLdDatasetMain().getBaseResourceURL()+r.getResource2();

						R r1 = new R(sR1);
						R r2 = new R(sR2);

						double score = simi.compare(r1, r2);

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

					double score = simi.compare(r1, r2);

					Result res = new Result();
					res.setResource1(params.getResources().get(0).getResource1());
					res.setResource2(params.getResources().get(0).getResource2());
					res.setScore(score);

					data.add(res);
				}
	        }
		}
		else {
			if(params.getOptions().isBenchmark()) {
				FileWriter csvWriter = new FileWriter("bench.csv");
				for(Resources r: params.getResources()) {
					csvWriter.append(r.getResource1());
					csvWriter.append(",");
					csvWriter.append(r.getResource2());
					csvWriter.append(",");
					csvWriter.append(String.valueOf(r.getBenchmark()));
					csvWriter.append("\n");

				}
				csvWriter.flush();
				csvWriter.close();
			}
			else {
				if(params.getResources().size()>1) {
					for(Resources r: params.getResources()) {
						//System.out.println("java");
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
		}
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
