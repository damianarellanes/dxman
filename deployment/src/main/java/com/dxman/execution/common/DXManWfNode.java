package com.dxman.execution.common;

import com.dxman.execution.wttree.DXManWorkflowTree;
import com.dxman.deployment.data.DXManDataAlgorithm;
import java.util.*;

/**
 * @author Damian Arellanes
 */
public class DXManWfNode {

  protected String classTypeWfNode = getClass().getName();
  
  private String id;  
  private String uri;
  private String workflowId;
  private String workflowTimestamp;
  private List<DXManWfNodeMapper> subNodeMappers = new ArrayList<>();

  public DXManWfNode() {}

  public DXManWfNode(String id, String uri) {    
    this.id = id;    
    this.uri = uri;
  }
  
  public void customise(String childKey, DXManWfNodeCustom custom) {
    
    for(DXManWfNodeMapper subNodeMapper: subNodeMappers) {
      
      if(subNodeMapper.getNode().getId().equalsIgnoreCase(childKey)) {
        subNodeMapper.setCustom(custom);
        return;
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
      subNodeMapper.getNode().setWorkflowTimestamp(wt.getCreationTimestamp());
      
      // Deploy again if composite detected (i.e., if there are submappers)
      if(!subNodeMapper.getNode().getSubnodeMappers().isEmpty()) {                
        subNodeMapper.getNode().deploy(alg, wt);
      }
    }    
    
    workflowId = wt.getId(); 
    workflowTimestamp = wt.getCreationTimestamp();
    
    if(wt.getDataChannels().get(id) != null)    
      wt.getDataChannels().get(id).forEach((dc) -> { alg.analyze(dc); });    
    
    build();
  }
  
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  
  public String getUri() { return uri; }
  public void setUri(String uri) { this.uri = uri; }  
  
  public String getWorkflowId() { return workflowId; }  
  public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
  
  public String getWorkflowTimestamp() { return workflowTimestamp; }
  public void setWorkflowTimestamp(String workflowTimestamp) {
    this.workflowTimestamp = workflowTimestamp;
  }
  
  public List<DXManWfNodeMapper> getSubnodeMappers() { return subNodeMappers; }
  public void setSubnodeMappers(List<DXManWfNodeMapper> subNodeMappers) { 
    this.subNodeMappers = subNodeMappers; 
  }
    
  public boolean isValid(){ return true; };
  public DXManWfSpec build(){ return null; };
}
