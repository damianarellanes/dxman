package com.dxman.design.data;

import com.dxman.utils.DXManIDGenerator;
import java.util.*;

/**
 * @author Damian Arellanes
 */
public class DXManDataProcessor extends DXManDataEntity {
  
  private final DXManDataProcessorInterface computation;
  private final String wfId; 
  private List<String> inputs;
  private final HashMap<String, String> writerIds;  
  private HashMap<String, String> writerIdsTmp;
    
  public DXManDataProcessor(DXManDataEntityType type, String name, String wfId, 
    DXManDataProcessorInterface computation) {
    
    super(name, type);
    this.computation = computation;
    this.wfId = wfId;
    this.inputs = new ArrayList<>();
    writerIds = new HashMap<>(); 
    writerIdsTmp = new HashMap<>();
  }
  
  public void addWriter(String writerId) {
    
    String finalId = DXManIDGenerator.generateParameterUUID(writerId, wfId);
    getWriterIds().put(finalId, finalId); 
    getWriterIdsTmp().put(finalId, finalId);
  }
  
  public void resetInputs() { this.inputs = new ArrayList<>(); }
  public void resetWriterIdsTmp() { writerIdsTmp.putAll(writerIds); }

  public DXManDataProcessorInterface getComputation() { return computation; }
  public String getWfId() { return wfId; }
  public List<String> getInputs() { return inputs; }
  public HashMap<String, String> getWriterIds() { return writerIds; }
  public HashMap<String, String> getWriterIdsTmp() { return writerIdsTmp; }
}
