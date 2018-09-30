package com.dxman.dataspace.blockchain;

import com.dxman.utils.DXManIDGenerator;
import java.util.*;
import com.dxman.dataspace.base.DXManDataParameter;
import org.json.*;

/**
 * @author Damian Arellanes
 */
public class BlockchainParameter implements DXManDataParameter {
    
  private final String id;
  private final String parameterId;
  private final String workflowId;
  private String value;
  private List<DXManDataParameter> writers;

  public BlockchainParameter(String parameterId, String workflowId, String value) {
    this.id = DXManIDGenerator.generateParameterUUID(parameterId, workflowId);
    this.parameterId = parameterId;
    this.workflowId = workflowId;
    this.value = value;
    this.writers = new ArrayList<>();  
  }    
  
  @Override
  public void addWriter(DXManDataParameter writer) {
    writers.add(writer);
  }
  
  @Override
  public JSONObject generateJSON() {
    
    JSONObject parameterJSON = new JSONObject();
    try {
      parameterJSON.put("$class", BlockchainConfiguration.PARAM_CLASS);
      parameterJSON.put("id", id);
      parameterJSON.put("parameterId", parameterId);
      parameterJSON.put("workflowId", workflowId);
      parameterJSON.put("value", value);
      //parameterJSON.put("timestamp", instant);
      JSONArray writersJSON = new JSONArray();
      for(DXManDataParameter writer: getWriters()) {
        writersJSON.put("resource:" + BlockchainConfiguration.PARAM_CLASS + "#"
          + DXManIDGenerator.generateParameterUUID(writer.getParameterId(), getWorkflowId()));
      }
      parameterJSON.put("writers", writersJSON);
    } catch (JSONException ex) { System.out.println(ex); }
    
    return parameterJSON;
  }
  
  @Override
  public String getId() { return id; }
  @Override
  public String getParameterId() { return parameterId; }
  @Override
  public String getWorkflowId() { return workflowId; }
  @Override
  public void setValue(String value) { this.value = value; }
  @Override
  public String getValue() { return value; }
  @Override
  public List<DXManDataParameter> getWriters() { return writers; }  
  public void setReaders(List<DXManDataParameter> writers) {
    this.writers = writers;
  }

  @Override
  public String toString() {
    return BlockchainConfiguration.PARAM_CLASS + "#" + id;
  }
}
