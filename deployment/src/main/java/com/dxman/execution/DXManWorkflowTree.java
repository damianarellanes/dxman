package com.dxman.execution;

import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.utils.*;
import java.sql.Timestamp;

/**
 * @author Damian Arellanes
 */
public class DXManWorkflowTree {
    
  private String id;  
  private DXManMap<String, DXManWfNode> wt;
  private final String creationTimestamp;
  private DXManCompositeServiceTemplate compositeService;
  
  public DXManWorkflowTree() {
  
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    this.creationTimestamp = timestamp.toInstant().toString();
  }

  public DXManWorkflowTree(DXManCompositeServiceTemplate compositeService) {
    
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    
    this.id = DXManIDGenerator.generateWorkflowID();
    this.wt = new DXManMap();
    this.creationTimestamp = timestamp.toInstant().toString();
    this.compositeService = compositeService;
  }
  
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  
  public DXManMap<String, DXManWfNode> getWt() { return wt; }
  public void setWt(DXManMap<String, DXManWfNode> wt) { this.wt = wt; }
  
  public String getCreationTimestamp() { return creationTimestamp; }
  
  public DXManCompositeServiceTemplate getCompositeService() {
    return compositeService;
  }
}
