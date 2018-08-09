package com.dxman.thing.server.base;

import com.dxman.execution.DXManWfNode;

/**
 * @author Damian Arellanes
 */
public interface DXManConnectorRequester {    
  
  public void transferControl(DXManWfNode subworkflow, String uri);
}
