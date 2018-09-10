package com.dxman.thing.server.base;

import com.dxman.thing.server.coap.DXManCoapServer;
import com.dxman.thing.server.coap.DXManCoapSingleton;
import org.eclipse.californium.core.network.config.NetworkConfig;

/**
 * @author Damian Arellanes
 */
public class DXManServerFactory {
    
  public static DXManServer createCoap(int port) {

    // Only starts the Coap server once
    try {
      
      NetworkConfig config = NetworkConfig.createStandardWithoutFile();
      config.set(NetworkConfig.Keys.MAX_RESOURCE_BODY_SIZE, 2000000);
      
      DXManCoapSingleton.createInstance(port);

      DXManCoapSingleton.get().start();
    } catch(Exception e) {
      System.out.println("The server is already running");
      System.exit(0);
    }
                  
    return new DXManCoapServer();
  }    
}
