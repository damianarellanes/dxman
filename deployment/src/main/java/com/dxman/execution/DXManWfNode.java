package com.dxman.execution;

import com.dxman.deployment.data.DXManDataAlgorithm;
import com.dxman.design.data.DXManDataChannel;
import java.util.*;

/**
 * @author Damian Arellanes
 */
public class DXManWfNode {

  protected String classTypeWfNode = getClass().getName();
  
  private String id;  
  private String uri;
  private String workflowId;
  private List<DXManWfNodeMapper> subNodeMappers = new ArrayList<>();
  
  private List<DXManDataChannel> dataChannels = new ArrayList<>(); // Remove and only keep in the service template

  public DXManWfNode() {}

  public DXManWfNode(String id, String uri) {    
    this.id = id;    
    this.uri = uri;
  }
  
  public void customise(String childKey, DXManWfNodeCustom custom) {
    
    for(DXManWfNodeMapper subNodeMapper: subNodeMappers) {
      
      if(subNodeMapper.getNode().getId().equalsIgnoreCase(childKey)) {
        subNodeMapper.setCustom(custom);
      }
    }
  }
  
  public void addSubWfNode(DXManWfNode wfNode, DXManWfNodeCustom custom) {
       
    DXManWfNodeMapper subNodeMapper = new DXManWfNodeMapper(wfNode, custom);
    subNodeMappers.add(subNodeMapper);
  }
  
  public void deploy(DXManDataAlgorithm alg, DXManWorkflowTree wt) {
    
    for(DXManWfNodeMapper subNodeMapper: subNodeMappers) {
      
      // The workflowId of the subNode is the workflowtree id set by the user      
      subNodeMapper.setNode(wt.getWt().get(subNodeMapper.getNode().getId()));
      subNodeMapper.getNode().setWorkflowId(wt.getId());
      
      // Deploy again if composite detected (i.e., if there are submappers)
      if(!subNodeMapper.getNode().getSubnodeMappers().isEmpty()) {
                
        subNodeMapper.getNode().deploy(alg, wt);
      }
    }    
    
    // The workflowId of this node is the workflowtree id set by the user
    workflowId = wt.getId(); 
    
    dataChannels.forEach((dc) -> { alg.analyze(dc); });    
    
    build();
  }
  
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  
  public String getUri() { return uri; }
  public void setUri(String uri) { this.uri = uri; }  
  
  public String getWorkflowId() { return workflowId; }  
  public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
  
  public List<DXManWfNodeMapper> getSubnodeMappers() { return subNodeMappers; }
  public void setSubnodeMappers(List<DXManWfNodeMapper> subNodeMappers) { 
    this.subNodeMappers = subNodeMappers; 
  }
  
  public List<DXManDataChannel> getDataChannels() { return dataChannels; }
  public void setDataChannels(List<DXManDataChannel> dataChannels) {
    this.dataChannels = dataChannels;
  }
    
  public boolean isValid(){ return true; };
  public DXManWfSpec build(){ return null; };
}
