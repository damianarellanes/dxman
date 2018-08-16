package com.dxman.execution;

import com.dxman.utils.DXManMap;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public abstract class DXManWfTree {
  
  private final String id;
  private final String uri;
  private final DXManWfNode wfNode;
  private final DXManMap<String, DXManWfNode> subWorkflows;  

  public DXManWfTree(String id, String uri, DXManWfNode wfNode,
    DXManWfTree... subWorkflows) {
    
    this.id = id;
    this.uri = uri;
    this.wfNode = wfNode;
    
    this.subWorkflows = new DXManMap();
    for(DXManWfTree subWorkflow: subWorkflows) {      
      this.subWorkflows.put(subWorkflow.getId(), subWorkflow.build().getFlow());
    }
  }
  
  protected void composeWf(String wfId, DXManWfNodeCustom custom) {
    
    wfNode.getSubnodeMappers().add(
      new DXManWfNodeMapper(getSubWorkflows().get(wfId), custom)
    );
  }
    
  protected void composeWf(DXManWfInvocation inv, DXManWfNodeCustom custom) {    
    wfNode.getSubnodeMappers().add(new DXManWfNodeMapper(inv, custom));
  }
  
  public DXManWorkflowResult execute(DXManWorkflowManager wfManager)
    throws JSONException {
    return wfManager.executeWorkflow(build(), getInputs(), getOutputs());
  }
  
  public String getId() { return id; }
  public String getUri() { return uri; }
  public DXManWfNode getWfNode() { return wfNode; }
  public DXManMap<String, DXManWfNode> getSubWorkflows() { return subWorkflows; }
    
  public abstract void design();
  public abstract DXManWfSpec build();
  public abstract DXManWorkflowInputs getInputs();
  public abstract DXManWorkflowOutputs getOutputs();
}
