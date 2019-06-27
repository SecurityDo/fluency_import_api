package com.fluencysecurity.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallResponse {
	@JsonInclude(JsonInclude.Include.ALWAYS)
	public String verdict;
	public JsonNode response;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public List<Attachment> attachments;
    public String error;    
    public String exception;

    public static CallResponse OKResponse(JsonNode node){
    	CallResponse res= new CallResponse();
    	res.verdict="OK";
    	res.response=node;
    	return res;
    }

    public static CallResponse ExceptionResponse(String exception){
    	CallResponse res= new CallResponse();
    	res.verdict="EXCEPTION";
    	res.exception=exception;
    	return res;
    }
    public static CallResponse ErrorResponse(String error){
    	CallResponse res= new CallResponse();
    	res.verdict="ERROR";
    	res.error=error;
    	return res;
    }
    
    public boolean checkVerdict(){
    	return "OK".equalsIgnoreCase(verdict);
    }
    
    public String checkError(){
    	if(error != null) return "ERROR:" + error;
    	if(exception != null) return "EXCEPTION:" + exception;
        return "no error/exception found?";
    }
    public String toString() {
    	if(verdict.equalsIgnoreCase("OK")){
    		return response.toString();
    	} else if (error != null){
    	   return verdict + ":" + error;
    	} else {
    		return verdict + ":" + exception;
    	}
    }

}
