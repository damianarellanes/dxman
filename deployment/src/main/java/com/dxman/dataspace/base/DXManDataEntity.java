package com.dxman.dataspace.base;

import java.util.List;
import org.json.JSONObject;

/**
 * @author Damian Arellanes
 */
public interface DXManDataEntity {

  public void addReader(DXManDataEntity dataEntity);
  
  public JSONObject generateJSON();
  
  public String getType();
  public String getId();
  public String getParameterId();
  public String getWorkflowId();
  public void setValue(String value);
  public String getValue();
  public List<DXManDataEntity> getReaders();
}
