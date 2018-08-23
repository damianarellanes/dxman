package com.dxman.dataspace.base;

import java.util.List;

/**
 * @author Damian Arellanes
 */
public interface DXManDataSpace {
    
  public int registerThing(String id, String alias);
  
  public void registerParameter(String parameterId, String workflowId, 
    String value, List<String> readers);
  
  public String readParameter(String parameterId, String workflowId);
  
  public void writeParameter(String parameterId, String workflowId, 
    String newValue);
}
