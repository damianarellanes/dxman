package com.dxman.dataspace.base;

import java.util.List;
import org.json.JSONArray;

/**
 * @author Damian Arellanes
 */
public interface DXManDataSpace {
    
  public int registerThing(String id, String alias);
  
  public void createDataEntities(List<DXManDataEntity> dataEntities, 
    String workflowId);
  
  public JSONArray readParameters(JSONArray paramRefs, 
    String workflowTimestamp);
  
  public String readParameter(String parameterId, String workflowId, 
    String workflowTimestamp);
  
  public void writeDataEntities(List<DXManDataEntity> dataEntities);
  
  public void writeParameter(String parameterId, String workflowId, 
    String newValue);
  
  public DXManDataEntityFactory getDataEntityFactory();
}
