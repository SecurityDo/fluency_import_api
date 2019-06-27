package com.fluencysecurity.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallRequest {
	public String provider;
	public String replyQueue;
	public String function;
	public JsonNode kargs;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)	
	public List<Attachment> attachments;
	
	public void addAttachment(Attachment a){
	  if (attachments == null){
           attachments = new ArrayList<>();           
	  }
	  attachments.add(a);
	}
}
