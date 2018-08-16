package com.dxman.execution;

/**
 * @author Damian Arellanes
 */
public class DXManWfTreeInv extends DXManWfTree {

  public DXManWfTreeInv(String operation, String uri) {
    super(operation, uri, new DXManWfInvocation(operation, uri));
  }

  @Override
  public void design() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public DXManWfSpec build() {
    return new DXManWfSpec(getId()+"-wf-spec", getWfNode());
  }

  @Override
  public DXManWfInputs getInputs() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public DXManWfOutputs getOutputs() {
    throw new UnsupportedOperationException("Not supported yet.");
  }    
}
