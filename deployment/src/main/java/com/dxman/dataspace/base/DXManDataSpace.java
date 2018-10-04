package com.dxman.dataspace.base;

import com.dxman.design.data.DXManParameter;
import java.util.*;

/**
 * @author Damian Arellanes
 */
public interface DXManDataSpace {
    
  public int registerThing(String id, String alias);
  
  public void createParameters(List<DXManDataParameter> parameters, 
    String workflowId);
  
  public DXManReadResult readParameters(Collection<DXManParameter> parameters, 
    String wfId, String wfTimestamp);
  
  public String readParameter(String parameterId, String workflowId, 
    String workflowTimestamp);
  
  public void writeParameters(List<DXManDataParameter> parameters);
  
  public void writeParameter(String parameterId, String workflowId, 
    String newValue, String updater);
  
  public String getDataspaceTimestamp();
  
  public String getEndpoint();
  
  public DXManDataParameter createDataParameter(String parameterId, 
    String workflowId, String value, String updater);
}
