package com.dxman.deployment.data;

import com.dxman.design.data.DXManDataChannel;
import java.util.*;

/**
 * @author Damian Arellanes
 */
public class DXManDataAlgorithm {
  
  private final HashMap<String, HashSet<String>> readers = new HashMap<>();
  private final HashMap<String, HashSet<String>> writers = new HashMap<>();
    
  public void analyze(DXManDataChannel dc) {
    
    // Analyze right (writer)
    HashSet<String> right;
    if(readers.containsKey(dc.getOrigin().getParameterId())) {
      right = readers.get(dc.getOrigin().getParameterId());
    } else {
      right = new HashSet<>();
      right.add(dc.getOrigin().getParameterId());
    }
    
    // Analyze left (reader)
    HashSet<String> left;
    if(writers.containsKey(dc.getDestination().getParameterId())) {
      
      left = writers.get(dc.getDestination().getParameterId());
      for(String reader: left) {
        
        readers.get(reader).remove(dc.getDestination().getParameterId());
        readers.get(reader).addAll(right);
      }
    } else {      
      left = new HashSet<>();
      left.add(dc.getDestination().getParameterId());
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
    
  public HashMap<String, HashSet<String>> getReaders() { return readers; }
  public HashMap<String, HashSet<String>> getWriters() { return writers; }
}
