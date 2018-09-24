package com.dxman.dataspace.base;

/**
 * @author Damian Arellanes
 */
public interface DXManDataEntityFactory {
  
  public DXManDataParameter createDataParameter(String parameterId, 
    String workflowId, String value);
  
  public DXManDataReducer createDataReducer(String parameterId, 
    String workflowId, String value, String chaincodeId);
  
  public DXManDataMapper createDataMapper(String parameterId, 
    String workflowId, String value, String chaincodeId);
}
