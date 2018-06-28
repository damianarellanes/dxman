package com.dxman.thing.deployment.connectors.atomic;

import com.dxman.design.distribution.DXManBindingInfo;

/**
 * @author Damian Arellanes
 */
public interface InvocationHandler {
  public String invokeJSON(DXManBindingInfo bindingInfo, String jsonRequest);
}
