package com.dxman.execution;

/**
 * @author Damian Arellanes
 */
public abstract class DXManWfNode {

  private String id;
  private String uri;

  public DXManWfNode() {}

  public DXManWfNode(String id, String uri) {
    this.id = id;
    this.uri = uri;
  }
  
  public abstract boolean isValid();

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getUri() { return uri; }
  public void setUri(String uri) { this.uri = uri; }
}
