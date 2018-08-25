package com.dxman.dataspace.base;

import com.dxman.utils.DXManIDGenerator;
import java.util.*;

/**
 * @author Damian Arellanes
 */
public class DXManDataParameter {
    
  private final String id;
  private final String parameterId;
  private final String workflowId;
  private final String value;
  private final List<String> readers;

  public DXManDataParameter(String parameterId, String workflowId, String value, 
    List<String> readers) {
    this.id = DXManIDGenerator.generateParameterUUID(parameterId, workflowId);
    this.parameterId = parameterId;
    this.workflowId = workflowId;
    this.value = value;
    this.readers = readers;
  }

  public String getId() { return id; }
  public String getParameterId() { return parameterId; }
  public String getWorkflowId() { return workflowId; }
  public String getValue() { return value; }
  public List<String> getReaders() { return readers; }
}
