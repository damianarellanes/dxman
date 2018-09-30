package com.dxman.deployment.data;

import com.dxman.design.data.*;
import java.util.*;

/**
 * @author Damian Arellanes
 */
public class DXManDataAlgorithm {
  
  private final HashMap<String, DXManDataProcessorTemplate> processors = new HashMap<>();
  
  private final HashMap<String, HashSet<String>> readers = new HashMap<>();
  private final HashMap<String, HashSet<String>> writers = new HashMap<>();
    
  public void analyze(DXManDataChannel dc) {
    
    if(dc.getOrigin().isDataProcessor()) {      
      processors.putIfAbsent(
        dc.getOrigin().getDataEntityId(), dc.getOrigin().getDataProcessor()
      );
    }
    if(dc.getDestination().isDataProcessor()) {
      processors.putIfAbsent(
        dc.getDestination().getDataEntityId(), dc.getDestination().getDataProcessor()
      );
    }
    
    // Analyze right (writer)
    HashSet<String> right;    
    if(!dc.getOrigin().isDataProcessor() && readers.containsKey(dc.getOrigin().getDataEntityId())) {
      right = readers.get(dc.getOrigin().getDataEntityId());
    } else {
      right = new HashSet<>();
      right.add(dc.getOrigin().getDataEntityId());
    }
    
    // Analyze left (reader)
    HashSet<String> left;
    if(!dc.getDestination().isDataProcessor() && writers.containsKey(dc.getDestination().getDataEntityId())) {
      
      left = writers.get(dc.getDestination().getDataEntityId());
      for(String reader: left) {
        
        readers.get(reader).remove(dc.getDestination().getDataEntityId());
        readers.get(reader).addAll(right);
      }
    } else {      
      left = new HashSet<>();
      left.add(dc.getDestination().getDataEntityId());
    }
    
    // Creates or adds: right to all readers and left to all writers
    for(String reader: left) { updateMap(readers, reader, right); }    
    for(String writer: right) { updateMap(writers, writer, left); }        
  }
  
  private void updateMap(HashMap map, String key, HashSet<String> value) {
    
    if(map.get(key) == null) {
      map.put(key, new HashSet<>());
      map.put(key, value);
    } else {
      ((HashSet)(map.get(key))).addAll(value);
    }
  }
  
  public HashMap<String, DXManDataProcessorTemplate> getProcessors() { return processors; }
  public HashMap<String, HashSet<String>> getReaders() { return readers; }
  public HashMap<String, HashSet<String>> getWriters() { return writers; }
}
