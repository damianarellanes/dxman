package com.dxman.execution.common;

/**
 * @author Damian Arellanes
 */
public class DXManWfSpec {

  private String id;
  private DXManWfNode flow;

  public DXManWfSpec() {}

  public DXManWfSpec(String id, DXManWfNode flow) {
    this.id = id;
    this.flow = flow;
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public DXManWfNode getFlow() { return flow; } 
  public void setFlow(DXManWfNode flow) { this.flow = flow; }
}
