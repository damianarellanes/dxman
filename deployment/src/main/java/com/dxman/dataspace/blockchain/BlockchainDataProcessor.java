package com.dxman.dataspace.blockchain;

import com.dxman.dataspace.base.DXManDataEntity;
import com.dxman.dataspace.base.DXManDataProcessor;
import com.dxman.utils.DXManIDGenerator;
import java.util.*;
import org.json.*;

/**
 * @author Damian Arellanes
 */
public abstract class BlockchainDataProcessor extends BlockchainDataEntity 
  implements DXManDataProcessor {
    
  private final List<String> inputs;
  private final List<String> writerIds;
  private final List<String> writerIdsTmp;
  private final String chaincodeId;
  
  public BlockchainDataProcessor(String type, String parameterId, String workflowId, 
    String value, String chaincodeId) {
    super(type, parameterId, workflowId, value);
    
    inputs = new ArrayList<>();
    this.writerIds = new ArrayList<>();
    this.writerIdsTmp = new ArrayList<>();
    this.chaincodeId = chaincodeId;
  }
    
  @Override
  public void addWriter(DXManDataEntity dataEntity) {
    writerIds.add(dataEntity.getId());
    writerIdsTmp.add(dataEntity.getId());
  }

  public List<String> getInputs() { return inputs; }
  public List<String> getWriterIds() { return writerIds; }
  public List<String> getWriterIdsTmp() { return writerIdsTmp; }
  public String getChaincodeId() { return chaincodeId; }
  
  @Override
  public JSONObject generateJSON() {
    
    JSONObject processorJSON = new JSONObject();
    try {
      processorJSON.put("$class", getType());
      processorJSON.put("id", getId());
      processorJSON.put("parameterId", getParameterId());
      processorJSON.put("workflowId", getWorkflowId());
      processorJSON.put("value", getValue());
      //parameterJSON.put("timestamp", instant);
      JSONArray readersJSON = new JSONArray();
      for(DXManDataEntity reader: getReaders()) {
        readersJSON.put("resource:" + reader.getType() + "#"
          + DXManIDGenerator.generateParameterUUID(reader.getParameterId(), getWorkflowId()));
      }
      processorJSON.put("readers", readersJSON);
      processorJSON.put("inputs", getInputs());
      processorJSON.put("writerIds", getWriterIds());
      processorJSON.put("writerIdsTmp", getWriterIdsTmp());
      processorJSON.put("chaincodeId", getChaincodeId());
    } catch (JSONException ex) { System.out.println(ex); }
    
    return processorJSON;
  } 
}
