package com.dxman.design.data;

import java.util.List;

/**
 * @author Damian Arellanes
 */
public interface DXManReducerInterface extends DXManDataProcessorInterface {  
  public abstract String reduce(List<String> inputs);
}
