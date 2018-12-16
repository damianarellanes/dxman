package com.dxman.utils;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Damian Arellanes
 * @param <A>
 * @param <V>
 */
public class DXManMap<A,V> extends ConcurrentHashMap<A,V> {
    
  public DXManMap() {}

  public DXManMap(DXManMap map) { super(map); }
}
