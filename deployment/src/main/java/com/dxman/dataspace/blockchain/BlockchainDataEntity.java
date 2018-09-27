package com.dxman.dataspace.blockchain;

import com.dxman.dataspace.base.DXManDataEntity;
import com.dxman.utils.DXManIDGenerator;
import java.util.*;

/**
 * @author Damian Arellanes
 */
public abstract class BlockchainDataEntity implements DXManDataEntity {
    
  private final String type;
  
  private final String id;
  private final String parameterId;
  private final String workflowId;
  private String value;
  private List<DXManDataEntity> readers;

  public BlockchainDataEntity(String type, String parameterId, String workflowId, 
    String value) {
    this.type = type;
    this.id = DXManIDGenerator.generateParameterUUID(parameterId, workflowId);
    this.parameterId = parameterId;
    this.workflowId = workflowId;
    this.value = value;
    this.readers = new ArrayList<>();
  
  }    
  
  @Override
  public void addReader(DXManDataEntity dataEntity) {
    readers.add(dataEntity);
  }
  
  @Override
  public String getType() { return type; }
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
  public List<DXManDataEntity> getReaders() { return readers; }  
  public void setReaders(List<DXManDataEntity> readers) {
    this.readers = readers;
  }

  @Override
  public String toString() {
    return type + "#" + id;
  }
}
