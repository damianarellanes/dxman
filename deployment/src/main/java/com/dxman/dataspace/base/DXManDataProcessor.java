package com.dxman.dataspace.base;

import java.util.List;

/**
 * @author Damian Arellanes
 */
public interface DXManDataProcessor extends DXManDataEntity {
  public void addWriter(DXManDataEntity dataEntity);
  public void addWriters(List<String> writerIds);  
}
