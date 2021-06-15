package com.lds_api.model;

/**
 * 
 * @author Alexandre DEBEDDES
 * 
 */
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimilarityResult {
	private String status;
	
	private int code;
	
	@JsonProperty(value = "data", required = true)
	private ArrayList<Result> data;
	
	private String message;
	
	public SimilarityResult() {}
	
	public void setStatus(String status) {
		this.status=status;
	}
	
	public void setCode(int code) {
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
	
	public int getCode() {
		return code;
	}
	
	public ArrayList<Result> setData() {
		return data;
	}
	
	public String setMessage() {
		return message;
	}
	
}
