package com.dxman.thing.server.base;

import com.dxman.thing.server.coap.DXManCoapConnectorRequester;
import com.google.gson.Gson;

/**
 * @author Damian Arellanes
 */
public class DXManConnectorRequesterFactory {
    
  public static DXManConnectorRequester createCoapRequester(Gson gson) {

    return new DXManCoapConnectorRequester(gson);
  }    
}
