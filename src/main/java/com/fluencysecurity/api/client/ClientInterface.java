package com.fluencysecurity.api.client;

import com.fluencysecurity.api.CallRequest;
import com.fluencysecurity.api.CallResponse;

public interface ClientInterface {
      CallResponse Call(CallRequest req);
      CallResponse CloudCall(CallRequest req);
  	  CallResponse TextUpload(String vendor, String textBlock);
}
