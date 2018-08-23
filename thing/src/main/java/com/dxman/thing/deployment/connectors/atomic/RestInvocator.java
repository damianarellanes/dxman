package com.dxman.thing.deployment.connectors.atomic;

import com.dxman.design.distribution.*;
import com.google.common.net.UrlEscapers;
import org.javalite.http.*;

/**
 * @author Damian Arellanes
 */
public class RestInvocator implements InvocationHandler {

  @Override
  public String invokeJSON(DXManBindingInfo bindingInfo, String jsonRequest) {

    String result = null;
    String uri = bindingInfo.getEndpoint().toString();
    switch(bindingInfo.getType()) {

      case HTTP_GET:
                
        if(bindingInfo.getContentType().equals(DXManBindingContent.QUERY_STRING)) {
          uri += jsonRequest;
        }
        
        uri = UrlEscapers.urlFragmentEscaper().escape(uri);
        result = doGet(uri, bindingInfo.getAcceptType());
        break;            
      case HTTP_POST:
        
        String content = "";
        if(bindingInfo.getContentType().equals(DXManBindingContent.QUERY_STRING)) {
          uri += jsonRequest;
        } else {
          content = jsonRequest;
        }
        
        uri = UrlEscapers.urlFragmentEscaper().escape(uri);
        result = doPost(uri, content, bindingInfo.getContentType());
        break;
    }    
    return result;
  }

  private String doGet(String uri, DXManBindingContent responseType) {

    Get response = Http.get(uri);
    
    switch(responseType) {
      case APPLICATION_JSON:
        response.header("Accept", "application/json");
        break;
      case APPLICATION_XML:
        response.header("Accept", "application/xml");
        break;
    }

    return response.text();
  }

  private String doPost(String uri, String content, 
    DXManBindingContent contentType) {

    Post response = Http.post(uri, content);
    
    switch(contentType) {
      case APPLICATION_JSON:
        response.header("Content-Type", "application/json");
        break;
      case APPLICATION_XML:
        response.header("Content-Type", "application/xml");
        break;
    }

    return response.text();
  }
}
