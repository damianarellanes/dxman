package com.dxman.thing.runtime;

/**
 * @author Damian Arellanes
 */
public class DXManThingServer {
  
  public static void main(String[] args) {
    
    if(args.length != 1) {
      
      System.err.println("Usage: DXManThingServer [propertiesPath]");
      System.exit(0);
    }
    DXManStarter.createRuntime(args[0]);
  }
}
