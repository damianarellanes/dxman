package com.dxman.design.distribution;

import java.net.URI;

/**
 * @author Damian Arellanes
 */
public class DXManBindingInfo {

  private URI endpoint;
  private DXManEndpointType type; // HTTP_METHOD, COAP_METHOD, RAW_SOCKET
  private DXManBindingContent contentType; // JSON, XML
  private DXManBindingContent acceptType; // JSON, XML
  private String requestTemplate; // e.g., JSON {"n1":##n1##,"n2":##n2##}
  private String responseTemplate;// e.g. XML, <parkingSlots>@@num@@</parkingSlots>

  public DXManBindingInfo() {}

  public DXManBindingInfo(URI endpoint, DXManEndpointType type, 
    DXManBindingContent contentType, DXManBindingContent acceptType, 
    String requestTemplate, String responseTemplate) {

    this.endpoint = endpoint;
    this.type = type;
    this.contentType = contentType;
    this.acceptType = acceptType;
    this.requestTemplate = requestTemplate;
    this.responseTemplate = responseTemplate;
  }

  public URI getEndpoint() { return endpoint; }
  public void setEndpoint(URI endpoint) { this.endpoint = endpoint; }

  public DXManEndpointType getType() { return type; }
  public void setType(DXManEndpointType type) { this.type = type; }

  public DXManBindingContent getContentType() { return contentType; }
  public void setContentType(DXManBindingContent contentType) {
    this.contentType = contentType;
  }

  public DXManBindingContent getAcceptType() { return acceptType; }
  public void setAcceptType(DXManBindingContent acceptType) {
    this.acceptType = acceptType;
  }

  public String getRequestTemplate() { return requestTemplate; }
  public void setRequestTemplate(String requestTemplate) {
    this.requestTemplate = requestTemplate;
  }

  public String getResponseTemplate() { return responseTemplate; }
  public void setResponseTemplate(String responseTemplate) {
    this.responseTemplate = responseTemplate;
  }

  @Override
  public String toString() {
    return "DXManBindingInfo{" + "endpoint=" + endpoint + ", type=" + type 
      + ", contentType=" + contentType + ", acceptType=" + acceptType 
      + ", requestTemplate=" + requestTemplate + ", responseTemplate=" 
      + responseTemplate + '}';
  }
}
