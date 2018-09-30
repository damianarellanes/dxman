package com.dxman.dataspace.base;

import java.util.List;
import org.json.JSONObject;

/**
 * @author Damian Arellanes
 */
public interface DXManDataParameter {

  public void addWriter(DXManDataParameter writer);
  
  public JSONObject generateJSON();
  
  public String getId();
  public String getParameterId();
  public String getWorkflowId();
  public void setValue(String value);
  public String getValue();
  public List<DXManDataParameter> getWriters();
}
