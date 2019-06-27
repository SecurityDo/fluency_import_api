# Fluency Client Java API 

## Install:

* mvn clean install
* mvn eclipse:eclipse -DdownloadSources -DdownloadJavadocs

## Call event import API

* Create a new collector in Fluency Management Console. Get the token for event import.
* Sample provided:  CloudCallTest.java
```
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
```

## License: LGPLv3.0
