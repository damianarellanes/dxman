package com.dxman.dataspace.base;

import java.util.List;

/**
 * @author Damian Arellanes
 */
public interface DXManDataSpace {
    
  public int registerThing(String id, String alias);
  
  public void registerParameter(String parameterId, String value, 
    String writerId, List<String> readerIds);
  
  public String readParameter(String parameterId, String readerId);
  
  public void writeParameter(String parameterId, String newValue);
}
