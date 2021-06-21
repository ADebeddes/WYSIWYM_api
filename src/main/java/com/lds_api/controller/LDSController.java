package com.lds_api.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lds_api.model.SimilarityResult;
import com.lds_api.service.LDSService;

import lds.measures.Measure;

import com.lds_api.model.SimilarityParameters;

@RestController
/***
 * 
 * @author Alexandre DEBEDDES
 *
 */
public class LDSController {

	@Autowired
    private LDSService LDSService;
	
	@PostMapping(value = "/similarity", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public SimilarityResult similarity(@RequestBody SimilarityParameters params) throws Exception{
		SimilarityResult res = new SimilarityResult();
		res = LDSService.LDSimilarity(params);
		try {
			//res = LDSService.LDSimilarity(params);
			res.setStatus("success");
			res.setCode(HttpStatus.OK);
		} 
		catch (Exception e) {
		// TODO Auto-generated catch block
			res.setStatus("error");
			res.setMessage("An error has occured : " + e.getMessage());
		}
		return res;
	}
	
	@PostMapping(value = "/measures", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ArrayList<String> getMeasures(@RequestBody SimilarityParameters params) throws Exception{
		ArrayList<String> res = new ArrayList<String>();
		for (Measure measure : Measure.values()) {
		    res.add(measure.toString());
		}
		return res;
	}
	
}
