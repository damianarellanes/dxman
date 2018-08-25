package com.dxman.dataspace.base;

import java.util.List;

/**
 * @author Damian Arellanes
 */
public interface DXManDataSpace {
    
  public int registerThing(String id, String alias);
  
  public void registerParameters(List<DXManDataParameter> parameters, 
    String workflowId);
  
  public String readParameter(String parameterId, String workflowId, 
    String workflowTimestamp);
  
  public void writeParameters(List<DXManDataParameter> parameters, 
    String workflowId);
  
  public void writeParameter(String parameterId, String workflowId, 
    String newValue);
}
