package com.dxman.execution;

import com.dxman.deployment.data.DXManDataAlgorithm;
import com.dxman.design.data.DXManDataChannel;
import java.util.*;

/**
 * @author Damian Arellanes
 */
public abstract class DXManWfNode {

  private String id;
  private String uri;
  private List<DXManWfNodeMapper> subNodeMappers = new ArrayList<>();
  
  private final List<DXManDataChannel> dataChannels = new ArrayList<>();

  public DXManWfNode() {}

  public DXManWfNode(String id, String uri) {
    this.id = id;
    this.uri = uri;
  }
  
  public void customiseOrder(String childKey, DXManWfNodeCustom custom) {
    
    for(DXManWfNodeMapper subNodeMapper: subNodeMappers) {
      
      if(subNodeMapper.getNode().getId().equalsIgnoreCase(childKey)) {
        subNodeMapper.setCustom(custom);
      }
    }
  }
  
  protected void composeWf(DXManWfNode wfNode, DXManWfNodeCustom custom) {
       
    DXManWfNodeMapper subNodeMapper = new DXManWfNodeMapper(wfNode, custom);
    subNodeMappers.add(subNodeMapper);
  }
  
  public void deploy(DXManDataAlgorithm alg) {
    
    for(DXManWfNodeMapper subNodeMapper: subNodeMappers) {
      
      if(!subNodeMapper.getNode().getSubnodeMappers().isEmpty()) {
        subNodeMapper.getNode().deploy(alg);
      }
    }    
    
    dataChannels.forEach((dc) -> { alg.analyze(dc); });
    
    build();
    
    System.out.println("Deploying: " + id);
  }
  
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  
  public String getUri() { return uri; }
  public void setUri(String uri) { this.uri = uri; }  
  
  public List<DXManWfNodeMapper> getSubnodeMappers() { return subNodeMappers; }
  public void setSubnodeMappers(List<DXManWfNodeMapper> subNodeMappers) { 
    this.subNodeMappers = subNodeMappers; 
  }
  
  public List<DXManDataChannel> getDataChannels() { return dataChannels; }
  
  public abstract boolean isValid();
  public abstract DXManWfSpec build();
}
