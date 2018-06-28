package com.dxman.thing.deployment.connectors.atomic;

import com.dxman.design.distribution.DXManBindingContent;
import com.dxman.design.distribution.DXManBindingInfo;
import org.javalite.http.Get;
import org.javalite.http.Http;
import org.javalite.http.Post;

/**
 * @author Damian Arellanes
 */
public class RestInvocator implements InvocationHandler {

  @Override
  public String invokeJSON(DXManBindingInfo bindingInfo, String jsonRequest) {

    String result = null;
    switch(bindingInfo.getType()) {

      case HTTP_GET:
        
        String uri = bindingInfo.getEndpoint().toString();
        if(bindingInfo.getContentType().equals(DXManBindingContent.QUERY_STRING)) {
          uri += jsonRequest;
        }
        result = doGet(uri);
        break;            
      case HTTP_POST:
        result = doPost(bindingInfo.getEndpoint().toString(), jsonRequest);
        break;
    }    
    return result;
  }

  private String doGet(String uri) {

      String responseStr;
      Get response = Http.get(uri)
        .header("Accept", "application/json");

      if(response.responseCode() != 200) {
        responseStr = response.responseMessage();
      } else {
        responseStr = response.text();
      }

      return responseStr;
  }

  private String doPost(String uri, String content) {

    String responseStr;
    Post response = Http.post(uri, content)
      .header("Content-Type", "application/json");

    if(response.responseCode() != 200) {
      responseStr = response.responseMessage();
    } else {
      responseStr = response.text();
    }

    return responseStr;
  }
}
