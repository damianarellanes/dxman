package com.dxman.dataspace.blockchain;

import com.dxman.dataspace.base.*;
import com.dxman.utils.DXManIDGenerator;
import org.json.*;

/**
 * @author Damian Arellanes
 */
public class BlockchainParameter extends BlockchainDataEntity implements DXManDataParameter {

  public BlockchainParameter(String parameterId, String workflowId, String value) {
    super("com.dxman.blockchain.Parameter", parameterId, workflowId, value);
  }
    
  @Override
  public JSONObject generateJSON() {
    
    JSONObject parameterJSON = new JSONObject();
    try {
      parameterJSON.put("$class", getType());
      parameterJSON.put("id", getId());
      parameterJSON.put("parameterId", getParameterId());
      parameterJSON.put("workflowId", getWorkflowId());
      parameterJSON.put("value", getValue());
      //parameterJSON.put("timestamp", instant);
      JSONArray readersJSON = new JSONArray();
      for(DXManDataEntity reader: getReaders()) {
        readersJSON.put("resource:" + reader.getType() + "#"
          + DXManIDGenerator.generateParameterUUID(reader.getParameterId(), getWorkflowId()));
      }
      parameterJSON.put("readers", readersJSON);
    } catch (JSONException ex) { System.out.println(ex); }
    
    return parameterJSON;
  }
}
