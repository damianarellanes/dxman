package com.dxman.design.data;

import java.util.List;

/**
 * @author Damian Arellanes
 */
public class DXManDataPipe {
    
  private final String parameterId;
  private final String value;
  private final String writerId;
  private final List<String> readerIds;

  public DXManDataPipe(String parameterId, String writerId, 
    List<String> readerIds) {
    
    this.parameterId = parameterId;
    this.value = "null";
    this.writerId = writerId;
    this.readerIds = readerIds;
  }

  public String getParameterId() { return parameterId; }
  public String getValue() { return value; }
  public String getWriterId() { return writerId; }
  public List<String> getReaderIds() { return readerIds; }
}
