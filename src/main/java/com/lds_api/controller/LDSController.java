package com.lds_api.controller;

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
	public SimilarityResult simpleSimilarity(@RequestBody SimilarityParameters params) throws Exception{
		SimilarityResult res = new SimilarityResult();
		//res = LDSService.LDSimilarity(params);
		try {
			res = LDSService.LDSimilarity(params);
			res.setStatus("success");
			res.setCode(HttpStatus.OK);
		} 
		catch (Exception e) {
		// TODO Auto-generated catch block
			res.setStatus("error");
			res.setMessage("An error has occured : " + e.getMessage());
		}
		/*catch (InternalServerErrorException e) {
			// TODO Auto-generated catch block
				res.setStatus("error");
				res.setMessage("An error has occured : ");
				e.printStackTrace();
			}*/
		

		/*if(res == null) {
	         throw new RecordNotFoundException("Invalid employee id : " + id);
	    }*/
		/*res.setStatus("success");
		ArrayList<Result> data = new ArrayList<Result>();
		Result r =new Result();
		r.setResource1(params.getResources().get(0).getResource1());
		r.setResource2(params.getResources().get(0).getResource2());
		r.setScore(0.654);
		Result r1 =new Result();
		r1.setResource1(params.getResources().get(1).getResource1());
		r1.setResource2(params.getResources().get(1).getResource2());
		r1.setScore(0.456);
		data.add(r);
		data.add(r1);
		res.setData(data);*/
		
		return res;
	}
}
