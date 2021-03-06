package com.dxman.execution.wttree;

import com.dxman.deployment.data.DXManDataProcessorInstance;
import com.dxman.design.data.*;
import com.dxman.execution.common.DXManWfNode;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.utils.*;
import java.util.*;

/**
 * @author Damian Arellanes
 */
public final class DXManWorkflowTree {
    
  private String id;  
  private DXManMap<String, DXManWfNode> wt;
  private final List<DXManDataProcessorInstance> dataProcessors;
  private DXManMap<String, List<DXManDataChannel>> dataChannels;
  private String creationTimestamp;
  private DXManCompositeServiceTemplate compositeService;
  
  public DXManWorkflowTree() {
    this.dataProcessors = new ArrayList();
  }

  public DXManWorkflowTree(DXManCompositeServiceTemplate compositeService) {
    this.id = DXManIDGenerator.generateWorkflowID();
    this.wt = new DXManMap();
    this.dataProcessors = new ArrayList();
    this.dataChannels = new DXManMap();
    this.compositeService = compositeService;
  }
  
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  
  public DXManMap<String, DXManWfNode> getWt() { return wt; }
  public void setWt(DXManMap<String, DXManWfNode> wt) { this.wt = wt; }
  
  public List<DXManDataProcessorInstance> getDataProcessors() {
    return dataProcessors;
  }
  
  public DXManMap<String, List<DXManDataChannel>> getDataChannels() { 
    return dataChannels; 
  }
  public void setDataChannels(DXManMap<String, List<DXManDataChannel>> dataChannels) {
    this.dataChannels = dataChannels;
  }
  
  public String getCreationTimestamp() { return creationTimestamp; }
  public void setCreationTimestamp(String timestamp) {
    this.creationTimestamp = timestamp;
  }
  
  public DXManCompositeServiceTemplate getCompositeService() {
    return compositeService;
  }
}
