package com.dxman.design.data;

import java.util.HashMap;
import java.util.List;

/**
 * @author Damian Arellanes
 */
public interface DXManDataMapper extends DXManDataProcessorInterface {
  public abstract HashMap<String, String> map(List<String> inputs);
}
