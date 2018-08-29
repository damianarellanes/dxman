package com.dxman.execution.wttree;

import com.dxman.execution.common.DXManWfNode;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.utils.*;
import java.sql.Timestamp;

/**
 * @author Damian Arellanes
 */
public final class DXManWorkflowTree {
    
  private String id;  
  private DXManMap<String, DXManWfNode> wt;
  private String creationTimestamp;
  private DXManCompositeServiceTemplate compositeService;
  
  public DXManWorkflowTree() {
    updateCreationTimestamp();
  }

  public DXManWorkflowTree(DXManCompositeServiceTemplate compositeService) {
    this.id = DXManIDGenerator.generateWorkflowID();
    this.wt = new DXManMap();
    updateCreationTimestamp();
    this.compositeService = compositeService;
  }
  
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  
  public DXManMap<String, DXManWfNode> getWt() { return wt; }
  public void setWt(DXManMap<String, DXManWfNode> wt) { this.wt = wt; }
  
  public String getCreationTimestamp() { return creationTimestamp; }
  public void updateCreationTimestamp() {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    this.creationTimestamp = timestamp.toInstant().toString();
  }
  
  public DXManCompositeServiceTemplate getCompositeService() {
    return compositeService;
  }
}
