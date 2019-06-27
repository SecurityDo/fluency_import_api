package com.fluencysecurity.api.impl;


import org.junit.Test;

import com.fluencysecurity.api.CallResponse;
import com.fluencysecurity.api.client.FsbClient;

public class CloudCallTest {	

	@Test
	public void testTextUpload() {
		
	    //FsbClient fsbClient = new FsbClient("http://localhost:8080",true);
		FsbClient fsbClient = new FsbClient("https://terplab.cloud.fluencysecurity.com",false);

	    fsbClient.setToken("8660704f-32ac-412c-485e-213bad9afbc0");
	    
		System.out.println("Fluency Import API Test:");
		String eventText= "line1\nline2\n";
		CallResponse res = fsbClient.TextUpload("fidelis",eventText);
		if(res.checkVerdict()){
			System.out.println("text upload OK");
		} else {
			System.out.println("text upload failed:" + res.checkError());
		}	
	}
}
