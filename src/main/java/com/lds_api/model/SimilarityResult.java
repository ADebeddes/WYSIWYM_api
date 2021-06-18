package com.lds_api.model;

/**
 * 
 * @author Alexandre DEBEDDES
 * 
 */
import java.util.ArrayList;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimilarityResult {
	private String status;
	
	private HttpStatus code;
	
	@JsonProperty(value = "data", required = true)
	private ArrayList<Result> data;
	
	private String message;
	
	public SimilarityResult() {}
	
	public SimilarityResult(HttpStatus code,String message) {
		this.code=code;
		this.data=null;
		this.message=message;
	}
	
	public void setStatus(String status) {
		this.status=status;
	}
	
	public void setCode(HttpStatus code) {
		this.code=code;
	}
	
	public void setData(ArrayList<Result> data) {
		this.data=data;
	}
	
	public void setMessage(String message) {
		this.message=message;
	}
	
	public String getStatus() {
		return status;
	}
	
	public HttpStatus getCode() {
		return code;
	}
	
	public ArrayList<Result> getData() {
		return data;
	}
	
	public String getMessage() {
		return message;
	}
	
}
