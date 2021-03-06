package com.lds_api.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lds_api.configurations.ApplicationPropertiesConfiguration;
import com.lds_api.model.MeasuresResult;
import com.lds_api.model.MicroMeasureParameters;
import com.lds_api.model.MicroMeasures;
import com.lds_api.model.SimilarityParameters;
import com.lds_api.model.SimilarityResult;
import com.lds_api.service.LDSService;

import lds.measures.Measure;

@RestController
/***
 * 
 * @author Alexandre DEBEDDES
 *
 */
public class LDSController {

	@Autowired
    private LDSService LDSService;
	
	/*@Autowired
    ApplicationPropertiesConfiguration appProperties;*/
	
	@PostMapping(value = "/similarity", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public SimilarityResult similarity(@RequestBody SimilarityParameters params) throws Exception{
		SimilarityResult res = new SimilarityResult();
		try {
			res = LDSService.LDSimilarity(params);
			res.setStatus("success");
			res.setCode(HttpStatus.OK);
		} 
		catch (Exception e) {
			res.setStatus("error");
			res.setMessage("An error has occured : " + e.getMessage());
		}
		return res;
	}
	
	@PostMapping(value = "/getMeasures",produces ="application/json")
	public ArrayList<MeasuresResult> getMeasures() throws Exception{
		ArrayList<MeasuresResult> res = new ArrayList<MeasuresResult>();
		for (Measure measure : Measure.values()) {
			MeasuresResult m = new MeasuresResult();
			m.setName(Measure.getName(measure));
			m.setAttribute(Measure.getName(measure));
		    res.add(m);
		}
		return res;
	}
	
	@PostMapping(value = "/getMicroMeasures",produces ="application/json")
	public ArrayList<MeasuresResult> getMicroMeasures() throws Exception{
		ArrayList<MeasuresResult> res = new ArrayList<MeasuresResult>();
		for (MicroMeasures measure : MicroMeasures.values()) {
			MeasuresResult m = new MeasuresResult();
			m.setAttribute(MicroMeasures.getName(measure));
			m.setName(MicroMeasures.getPath(measure));
			m.setDescription(MicroMeasures.getDescription(measure));
		    res.add(m);
		}
		return res;
	}
	
	@PostMapping(value = "/microMeasure", consumes = "application/json",produces ="application/json")
	@ResponseBody
	public SimilarityResult microMeasure(@RequestBody MicroMeasureParameters params) throws Exception{
		SimilarityResult res = new SimilarityResult();
		try {
			res = LDSService.newMeasure(params);
			if(res.getMessage()!=null) {
				res.setStatus("error");
			}
			else {
				res.setStatus("success");
				res.setCode(HttpStatus.OK);
			}
		} 
		catch (Exception e) {
			res = new SimilarityResult();
			res.setStatus("error");
			res.setMessage("An error has occured : " + e.getMessage());
		}
		return res;
	}
	
	public void afficheParams(SimilarityParameters params) {
		System.out.println(params.getLdDatasetMain().toString());
		System.out.println(params.getOptions().toString());
		System.out.println(params.getResources());
	}
}
