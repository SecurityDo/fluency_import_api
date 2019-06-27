package com.fluencysecurity.api;

import com.fasterxml.jackson.annotation.JsonInclude;

//@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attachment {
	public String name;
	public String path;
	public String id;
	public String type;
	public String hash;
}
