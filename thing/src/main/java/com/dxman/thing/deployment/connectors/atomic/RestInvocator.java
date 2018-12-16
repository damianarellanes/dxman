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
        result = doGet(uri, bindingInfo);
        break;            
      case HTTP_POST:
        
        String content = "";
        if(bindingInfo.getContentType().equals(DXManBindingContent.QUERY_STRING)) {
          uri += jsonRequest;
        } else {
          content = jsonRequest;
        }
        
        uri = UrlEscapers.urlFragmentEscaper().escape(uri);
        result = doPost(uri, content, bindingInfo);
        break;
    }    
    return result;
  }

  private String doGet(String uri, DXManBindingInfo bindingInfo) {

    Get response = Http.get(uri);    
    customiseRequest(response, bindingInfo);
    return response.text();
  }

  private String doPost(String uri, String content, 
    DXManBindingInfo bindingInfo) {

    Post response = Http.post(uri, content);    
    customiseRequest(response, bindingInfo);
    return response.text();
  }
  
  private void customiseRequest(Request request, DXManBindingInfo bindingInfo) {
    
    switch(bindingInfo.getContentType()) {
      case PLAIN:
        request.header("Content-Type", "text/plain");
        break;
      case APPLICATION_JSON:
        request.header("Content-Type", "application/json");
        break;
      case APPLICATION_XML:
        request.header("Content-Type", "application/xml");
        break;
    }
    
    switch(bindingInfo.getAcceptType()) {
      case PLAIN:
        request.header("Accept", "text/plain");
        break;
      case APPLICATION_JSON:
        request.header("Accept", "application/json");
        break;
      case APPLICATION_XML:
        request.header("Accept", "application/xml");
        break;
    }
  }
}
