package com.fluencysecurity.scripts
import com.fluencysecurity.api.client.FsbClient;
import com.fluencysecurity.api.impl.FluencyApi;
import com.fluencysecurity.api.model.EsSearchHit;
import com.fluencysecurity.api.model.EsSearchResult;
import com.fluencysecurity.api.model.SessionCache;
import com.fluencysecurity.api.model.SimpleSearchOption;


FluencyApi apiClient = new FluencyApi();
FsbClient fsbClient = new FsbClient("192.168.0.113",443,"https",true);
apiClient.setClient(fsbClient);

apiClient.setDebugFlag(true);

SessionCache session = apiClient.login("admin@security.do","security");
if(session != null){
	System.out.println("Token: "+session.sessionToken);
	String token = session.sessionToken;
	fsbClient.setToken(token);
} else {
	System.out.println("login failed!");
	return;
}


SimpleSearchOption options = new SimpleSearchOption();
options.searchStr="";
options.fetchOffset=0;
options.fetchLimit=10;
EsSearchResult result = apiClient.normal_search("default","20150911","metaflow",options);
if(result !=null){
	System.out.println("took:" + result.took + " ms");
	System.out.println("total:" + result.hits.total);

	for(EsSearchHit hit: result.hits.hits){
		apiClient.prettyPrint(hit);
	}
}
