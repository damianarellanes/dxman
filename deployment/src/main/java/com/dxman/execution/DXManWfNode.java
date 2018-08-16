package com.dxman.execution;

import java.util.*;

/**
 * @author Damian Arellanes
 */
public abstract class DXManWfNode {

  private String id;
  private String uri;
  private List<DXManWfNodeMapper> subNodeMappers = new ArrayList<>();

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
  public List<DXManWfNodeMapper> getSubnodeMappers() { return subNodeMappers; }
  public void setSubnodeMappers(List<DXManWfNodeMapper> subNodeMappers) { 
    this.subNodeMappers = subNodeMappers; 
  }
}
