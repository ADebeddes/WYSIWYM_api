package com.lds_api.service;

import lombok.Data;
import sc.research.ldq.LdDataset;
import sc.research.ldq.LdDatasetFactory;

import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.springframework.stereotype.Service;

import com.lds_api.model.SimilarityParameters;
import com.lds_api.model.SimilarityResult;
import com.lds_api.model.LdDatasetMain;
import com.lds_api.model.Resources;
import com.lds_api.model.Options;

import lds.config.Config;
import lds.config.ConfigParam;
import lds.config.LdConfigFactory;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import lds.resource.R;

/***
 * 
 * @author Alexandre DEBEDDES
 *
 */

@Data
@Service
public class LDSService {

	public SimilarityResult Similarity(SimilarityParameters params) {
		if(params.getOptions().isBenchmark()) {
			
		}
		else {
			if(params.getResources().size()>1) {
				for(Resources r: params.getResources()) {
					String sR1 = params.getLdDatasetMain().getBaseResourceURL()+r.getResource1();
					String sR2 = params.getLdDatasetMain().getBaseResourceURL()+r.getResource2();

					R r1 = new R(sR1);
					R r2 = new R(sR2);
				}
			}
			else {
				String sR1 = params.getLdDatasetMain().getBaseResourceURL()+params.getResources().get(0).getResource1();
				String sR2 = params.getLdDatasetMain().getBaseResourceURL()+params.getResources().get(0).getResource2();

				R r1 = new R(sR1);
				R r2 = new R(sR2);
			}
		}
		
		return null;
	}

	public LdSimilarityEngine loadEngine(LdDatasetMain LdDatasetMain ,Options options) {
		LdSimilarityEngine engine = new LdSimilarityEngine();
		switch(options.getMeasureType()) {
		case "Resim":
			
			Config configResim = LdConfigFactory.createDefaultConf(Measure.Resim);
			engine.load(Measure.Resim , configResim);
			break;
		case "EPICS":
			Config configEPICS = LdConfigFactory.createDefaultConf(Measure.EPICS); 
			engine.load(Measure.EPICS , configEPICS);
			break;
		case "LDSD":
			Config configLDSD = LdConfigFactory.createDefaultConf(Measure.LDSD_cw);
			engine.load(Measure.LDSD_cw , configLDSD);
			break;
		case "PICSS":
			Config configPICSS = LdConfigFactory.createDefaultConf(Measure.PICSS);
			engine.load(Measure.PICSS , configPICSS);
			break;
		case "LODS_SimP":
			
			break;
		case "LODS_SimI":
			
			break;
		default:
			
			break;
			
		}
		return engine;
	}

	public Config SimilarityConfig(LdDatasetMain LdDatasetMain ,Options options) throws Exception {
		Config config = new Config();
		
		PrefixMapping prefixes = new PrefixMappingImpl();
		prefixes.setNsPrefix("xsd", LdDatasetMain.getPrefixes().getNsPrefixMap().getXsd());
		prefixes.setNsPrefix("rdfs", LdDatasetMain.getPrefixes().getNsPrefixMap().getRdfs());
		prefixes.setNsPrefix("dbpedia", LdDatasetMain.getPrefixes().getNsPrefixMap().getDbpedia());
		prefixes.setNsPrefix("dbpediaowl", LdDatasetMain.getPrefixes().getNsPrefixMap().getDbpediaowl());
		prefixes.setNsPrefix("rdf", LdDatasetMain.getPrefixes().getNsPrefixMap().getRdf());
		
		LdDataset dataSetMain = LdDatasetFactory.getInstance().service(LdDatasetMain.getLink()).name(LdDatasetMain.getName()).defaultGraph(LdDatasetMain.getDefaultGraph()).prefixes(prefixes).create();
		config.addParam(ConfigParam.LdDatasetMain, dataSetMain);
		
		if(options.isUseIndex()) config.addParam(ConfigParam.useIndexes, true);
		else config.addParam(ConfigParam.useIndexes, false);
		
		return config;
	}

}
