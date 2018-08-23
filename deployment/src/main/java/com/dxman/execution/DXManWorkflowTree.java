package com.dxman.execution;

import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.utils.*;

/**
 * @author Damian Arellanes
 */
public class DXManWorkflowTree {
    
  private String id;
  private DXManMap<String, DXManWfNode> wt;
  private DXManCompositeServiceTemplate compositeService;
  
  public DXManWorkflowTree() {}

  public DXManWorkflowTree(DXManCompositeServiceTemplate compositeService) {
    this.id = DXManIDGenerator.generateWorkflowID();
    this.wt = new DXManMap();
    this.compositeService = compositeService;
  }
  
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  
  public DXManMap<String, DXManWfNode> getWt() { return wt; }
  public void setWt(DXManMap<String, DXManWfNode> wt) { this.wt = wt; }
  
  public DXManCompositeServiceTemplate getCompositeService() {
    return compositeService;
  }
}
