package com.fluencysecurity.api.client;

import java.io.File;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluencysecurity.api.Attachment;
import com.fluencysecurity.api.CallRequest;
import com.fluencysecurity.api.CallResponse;

public class FsbClient implements ClientInterface{

	//String host;
	//int port;
	CloseableHttpClient httpClient;
	ObjectMapper mapper;
	String hostUrl;
	//String protocol; 
	String header;
	String token;
	public FsbClient(String host, int port,String protocol,boolean disableCertCheck) {
		super();
		String url = String.format("%s://%s:%s",protocol,host,port);
		this.hostUrl = url;
		if(disableCertCheck){
	    try {
			SSLContextBuilder builder = new SSLContextBuilder();
		    builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
			        builder.build(),SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			
			this.httpClient = HttpClients.custom().setSSLSocketFactory(
			            sslsf).build();
			//this.httpClient= HttpClientBuilder.create().build();

		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    } else {
	    	this.httpClient= HttpClientBuilder.create().build();
	    }
		this.mapper = new ObjectMapper();
		this.header="FluencyToken";
	}    

	public FsbClient(String hostUrl,boolean disableCertCheck) {
		super();
		this.hostUrl = hostUrl;
		if(disableCertCheck){
	    try {
			SSLContextBuilder builder = new SSLContextBuilder();
		    builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
			        builder.build(),SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			
			this.httpClient = HttpClients.custom().setSSLSocketFactory(
			            sslsf).build();
			//this.httpClient= HttpClientBuilder.create().build();

		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    } else {
	    	this.httpClient= HttpClientBuilder.create().build();
	    }
		this.mapper = new ObjectMapper();
		this.header="FluencyToken";
	}    
    

	public void setToken(String token) {
		this.token = token;
	}
	public void setHeader(String _header) {
		this.header = _header;
	}

	@Override
	public CallResponse TextUpload(String vendor, String textBlock) {
		String url = String.format("%s/api/import/%s",hostUrl, vendor);
		HttpPost postRequest = new HttpPost(url);
		try{
			   //StringEntity input = new StringEntity(textBlock);		
			   //input.setContentType("text/html; charset=utf-8");
			   
			   HttpEntity entity = new GzipCompressingEntity(
					     new StringEntity(textBlock, ContentType.TEXT_PLAIN));
			   if(token != null){
			      postRequest.addHeader(header,token);
			   }
			   
			   postRequest.setEntity(entity);
			   
			   CloseableHttpResponse response = httpClient.execute(postRequest);       
			   try {
			     int statusCode=response.getStatusLine().getStatusCode();
			     if ( statusCode != 200) {
				     System.out.println("statusCode: " + statusCode);
				     System.out.println("Reason: " + response.getStatusLine().getReasonPhrase());
				     return CallResponse.ExceptionResponse("LocalFsbClient invalid http statusCode: " + statusCode);
				 // throw new RuntimeException("Failed : HTTP error code : "
				//		+ response.getStatusLine().getStatusCode());
			     }			     
			     CallResponse res = mapper.readValue(response.getEntity().getContent(), CallResponse.class);
			     return res;
			   } catch (Exception e){
					System.out.println("LocalFsbClient post exception!");
					e.printStackTrace();
					return CallResponse.ExceptionResponse("LocalFsbClient post exception: " + e.getMessage());
				}  finally {
				  if(response != null) {response.close();}
			   }
			   
			} catch (Exception e){
				System.out.println("LocalFsbClient call exception!");
				e.printStackTrace();
				return CallResponse.ExceptionResponse("LocalFsbClient call exception: " + e.getMessage());
			}

	}


	@Override
	public CallResponse Call(CallRequest req) {
		String url = String.format("%s/%s/%s",hostUrl,req.provider,req.function);
		HttpPost postRequest = new HttpPost(url);
		try{
			   StringEntity input = new StringEntity(mapper.writeValueAsString(req));		
			   input.setContentType("application/json");
			   if(token != null){
			      postRequest.addHeader(header,token);
			   }
			   postRequest.setEntity(input);
			   
			   
			   CloseableHttpResponse response = httpClient.execute(postRequest);       
			   try {
			     int statusCode=response.getStatusLine().getStatusCode();
			     if ( statusCode != 200) {
				     System.out.println("statusCode: " + statusCode);
				     System.out.println("Reason: " + response.getStatusLine().getReasonPhrase());
				     return CallResponse.ExceptionResponse("LocalFsbClient invalid http statusCode: " + statusCode);
				 // throw new RuntimeException("Failed : HTTP error code : "
				//		+ response.getStatusLine().getStatusCode());
			     }
			     CallResponse res = mapper.readValue(response.getEntity().getContent(), CallResponse.class);
			     return res;
			   } catch (Exception e){
					System.out.println("LocalFsbClient post exception!");
					e.printStackTrace();
					return CallResponse.ExceptionResponse("LocalFsbClient post exception: " + e.getMessage());
				}  finally {
				  if(response != null) {response.close();}
			   }
			   
			} catch (Exception e){
				System.out.println("LocalFsbClient call exception!");
				e.printStackTrace();
				return CallResponse.ExceptionResponse("LocalFsbClient call exception: " + e.getMessage());
			}

	}



	@Override
	public CallResponse CloudCall(CallRequest req) {
		// TODO Auto-generated method stub
		String url = String.format("%s/fsl/cloudService",hostUrl);
		String argsText="";
		if(req.kargs!=null) {
			try {
				argsText=mapper.writeValueAsString(req.kargs);
				//System.out.println(argsText);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		String attachmentsText="";
		if(req.attachments!=null) {
			try {
				attachmentsText=mapper.writeValueAsString(req.attachments);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int fileCount=0;
		if(req.attachments!=null){
			fileCount=req.attachments.size();
		}
		MultipartEntityBuilder builder= MultipartEntityBuilder
			    .create()
			    .addTextBody("kargs", argsText)
			    .addTextBody("attachments", attachmentsText)
			    .addTextBody("functionName", req.function)
		        .addTextBody("fileCount", Integer.toString(fileCount));
		if(req.attachments!=null){
		  int fileIndex=0;
		  for(Attachment a:req.attachments){
			 builder.addBinaryBody("fsl_remote_file_" + fileIndex, 
					new File(a.path), 
					ContentType.create("application/octet-stream"), a.name);
			fileIndex++;
		  }
		}
			    
	   HttpEntity entity =builder.build();
	   HttpPost httpPost = new HttpPost(url);
	   if(token != null){
		   httpPost.addHeader(header,token);
	   }

	   httpPost.setEntity(entity);
	   
	   /*
	   try {
		   System.out.println(httpPost.toString());
		   ByteArrayOutputStream out = new ByteArrayOutputStream();
		   entity.writeTo(out);
		   System.out.println(out);

	   } catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	  } */

     	   
	   try {
		   CloseableHttpResponse response = httpClient.execute(httpPost);
	  // HttpEntity result = response.getEntity();
	   try {
		     int statusCode=response.getStatusLine().getStatusCode();
		     if ( statusCode != 200) {
			     System.out.println("statusCode: " + statusCode);
			     System.out.println("Reason: " + response.toString());
			     return CallResponse.ExceptionResponse("Invalid http statusCode: " + statusCode);
			 // throw new RuntimeException("Failed : HTTP error code : "
			//		+ response.getStatusLine().getStatusCode());
		     }
		     CallResponse res = mapper.readValue(response.getEntity().getContent(), CallResponse.class);
		     return res;
	  } catch (Exception e){
				System.out.println("LocalFsbClient post exception!");
				e.printStackTrace();
				return CallResponse.ExceptionResponse("LocalFsbClient post exception: " + e.getMessage());
	  }  finally {
			  if(response != null) {response.close();}
	  }
	  } catch (Exception e){
			System.out.println("LocalFsbClient call exception!");
			e.printStackTrace();
			return CallResponse.ExceptionResponse("LocalFsbClient call exception: " + e.getMessage());
	  }
	}


}
