package com.dxman.thing.server.coap;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;

/**
 * @author Damian Arellanes
 */
public class DXManCoapSingleton extends CoapServer {
    
  private static DXManCoapSingleton INSTANCE = null;

  private final int port;

  private DXManCoapSingleton(int port) throws SocketException {
    
    this.port = port;

    // add endpoints on all IP addresses
    addEndpoints();
  }

  public static void createInstance(int port) {

    if (INSTANCE == null) {
      synchronized(DXManCoapSingleton.class) {
        if (INSTANCE == null) { 
          try {
              INSTANCE = new DXManCoapSingleton(port);
          } catch (SocketException ex) {
            System.err.println("Failed to initialize server: " + ex.getMessage());
          }
        }
      }
    }
  }

  public static DXManCoapSingleton get() { return INSTANCE; }

  @Override
  public Object clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException(); 
  }

  /**
   * Adds individual endpoints listening on default CoAP port 
   * on all IPv4 addresses of all network interfaces.
   */
  private void addEndpoints() {
    
    for (InetAddress addr : 
      EndpointManager.getEndpointManager().getNetworkInterfaces()) {
      
      // only binds to IPv4 addresses and localhost
        if (addr instanceof Inet4Address || addr.isLoopbackAddress()) {
          InetSocketAddress bindToAddress = new InetSocketAddress(addr, port);
          addEndpoint(new CoapEndpoint(bindToAddress));
          //System.out.println("Endpoint: " + addr + "-->" + port);
        }
      }
  }
}
