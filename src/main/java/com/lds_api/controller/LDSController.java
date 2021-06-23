package com.lds_api.controller;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
	
	@PostMapping(value = "/similarity", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public SimilarityResult similarity(@RequestBody SimilarityParameters params) throws Exception{
		System.out.println("Bonjour je suis le début du test");
		System.out.println(params.toString());
		afficheParams(params);
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
	
	@PostMapping(value = "/measures",produces ="application/json")
	public ArrayList<String> getMeasures() throws Exception{
		ArrayList<String> res = new ArrayList<String>();
		for (Measure measure : Measure.values()) {
		    res.add(measure.toString());
		}
		return res;
	}
	
	public void afficheParams(SimilarityParameters params) {
		System.out.println(params.getLdDatasetMain().toString());
		System.out.println(params.getOptions().toString());
		System.out.println(params.getResources());
	}
}
