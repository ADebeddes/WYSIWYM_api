package com.lds_api.controller;

import lds.config.Config;
import lds.config.ConfigParam;
import lds.config.LdConfigFactory;
import lds.dataset.LdDatasetCreator;
import lds.engine.LdSimilarityEngine;
import lds.measures.Measure;
import lds.measures.lods.SimI;
import lds.measures.lods.ontologies.*;
import lds.resource.R;
import sc.research.ldq.LdDataset;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lds_api.model.SimilarityResult;

@RestController
/***
 * 
 * @author Alexandre DEBEDDES
 *
 */
public class LDSController {

	@RequestMapping(value = "/resim", method = RequestMethod.GET)
	@ResponseBody
	public SimilarityResult getResim(@RequestParam List<String> id) {
		String sR1 = "http://dbpedia.org/resource/"+id.get(0);
		String sR2 = "http://dbpedia.org/resource/"+id.get(1);

		R r1 = new R(sR1);
        R r2 = new R(sR2);
        
        LdSimilarityEngine engine = new LdSimilarityEngine();
        Config config = LdConfigFactory.createDefaultConf(Measure.Resim);
        engine.load(Measure.Resim , config);
        double score = engine.similarity(r1 , r2);
        engine.close();
        SimilarityResult res = new SimilarityResult();
        res.setId1(id.get(0));
        res.setId2(id.get(1));
        res.setResult(score);
        return res;
	}
	
	@RequestMapping(value = "/ldsd", method = RequestMethod.GET)
	@ResponseBody
	public SimilarityResult getLDSD(@RequestParam List<String> id) {
		String sR1 = "http://dbpedia.org/resource/"+id.get(0);
		String sR2 = "http://dbpedia.org/resource/"+id.get(1);

		R r1 = new R(sR1);
        R r2 = new R(sR2);
        LdSimilarityEngine engine = new LdSimilarityEngine();
        Config config = LdConfigFactory.createDefaultConf(Measure.LDSD_cw);
        engine.load(Measure.LDSD_cw , config);
        double score = engine.similarity(r1 , r2);
        engine.close();
        SimilarityResult res = new SimilarityResult();
        res.setId1(id.get(0));
        res.setId2(id.get(1));
        res.setResult(score);
        return res;
	}
	
	@RequestMapping(value = "/lods_simi", method = RequestMethod.GET)
	@ResponseBody
	public SimilarityResult getLODS_SimI(@RequestParam List<String> id) throws Exception{
		LdDataset dataSetMain = LdDatasetCreator.getDBpediaDataset();
        
        Config config = new Config();
        config.addParam(ConfigParam.useIndexes, false);
        config.addParam(ConfigParam.LdDatasetMain , dataSetMain);
        config.addParam(ConfigParam.dataAugmentation , true);
        
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
        
		String sR1 = "http://dbpedia.org/resource/"+id.get(0);
		String sR2 = "http://dbpedia.org/resource/"+id.get(1);

		R r1 = new R(sR1);
        R r2 = new R(sR2);
        
        double score = simi.compare(r1 , r2);
        simi.closeIndexes();
        SimilarityResult res = new SimilarityResult();
        res.setId1(id.get(0));
        res.setId2(id.get(1));
        res.setResult(score);
        return res;
	}
	
	@RequestMapping(value = "/lods_simp", method = RequestMethod.GET)
	@ResponseBody
	public SimilarityResult getLODS_SimP(@RequestParam List<String> id){
		LdDataset dataSetMain = LdDatasetCreator.getDBpediaDataset();
        
		Config config = new Config();
        config.addParam(ConfigParam.useIndexes, true);
        config.addParam(ConfigParam.LdDatasetMain , dataSetMain);
        config.addParam(ConfigParam.resourcesCount , 2350906);
        
		String sR1 = "http://dbpedia.org/resource/"+id.get(0);
		String sR2 = "http://dbpedia.org/resource/"+id.get(1);
		
		R r1 = new R(sR1);
        R r2 = new R(sR2);
        
        LdSimilarityEngine engine = new LdSimilarityEngine();
        engine.load(Measure.SimP , config);
        double score = engine.similarity(r1 , r2);
        engine.close();
        SimilarityResult res = new SimilarityResult();
        res.setId1(id.get(0));
        res.setId2(id.get(1));
        res.setResult(score);
        return res;
	}
	
	@RequestMapping(value = "/picss", method = RequestMethod.GET)
	@ResponseBody
	public SimilarityResult getPICSS(@RequestParam List<String> id) {
		Config config = LdConfigFactory.createDefaultConf(Measure.PICSS); 
		String sR1 = "http://dbpedia.org/resource/"+id.get(0);
		String sR2 = "http://dbpedia.org/resource/"+id.get(1);
		R r1 = new R(sR1);
        R r2 = new R(sR2);
        LdSimilarityEngine engine = new LdSimilarityEngine();
        engine.load(Measure.PICSS , config);
        double score = engine.similarity(r1 , r2);
        engine.close();
        SimilarityResult res = new SimilarityResult();
        res.setId1(id.get(0));
        res.setId2(id.get(1));
        res.setResult(score);
		return res;
	}
	@RequestMapping(value = "/epics", method = RequestMethod.GET)
	@ResponseBody
	public SimilarityResult getEPICS(@RequestParam List<String> id) {
		Config config = LdConfigFactory.createDefaultConf(Measure.EPICS); 
		String sR1 = "http://dbpedia.org/resource/"+id.get(0);
		String sR2 = "http://dbpedia.org/resource/"+id.get(1);
		R r1 = new R(sR1);
        R r2 = new R(sR2);
        LdSimilarityEngine engine = new LdSimilarityEngine();
        engine.load(Measure.EPICS , config);
        double score = engine.similarity(r1 , r2);
        engine.close();
        SimilarityResult res = new SimilarityResult();
        res.setId1(id.get(0));
        res.setId2(id.get(1));
        res.setResult(score);
		return res;
	}
}
