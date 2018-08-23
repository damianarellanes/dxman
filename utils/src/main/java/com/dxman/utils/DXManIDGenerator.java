package com.dxman.utils;

import java.util.UUID;

/**
 * @author Damian Arellanes
 */
public class DXManIDGenerator {
    
  private static UUID generateRandomLong() {
    return UUID.randomUUID();
  }

  public static String generateHostId() {    
    return generateRandomLong().toString();
  }

  public static String generateConnectorID() {    
    return generateRandomLong().toString();
  }

  public static String generateServiceID(String name) {        
    return generateRandomLong().toString();
  }

  public static String generateOperationID(String name) {
    return generateRandomLong().toString();
  }

  public static String generateParameterID() {
    return generateRandomLong().toString();
  }
  
  public static String generateWorkflowID() {
    return generateRandomLong().toString();
  }
  
  public static String generateParameterUUID(String paramId, String wfId) {
        
    return new UUID(
      UUID.nameUUIDFromBytes(paramId.getBytes()).getMostSignificantBits(), 
      UUID.nameUUIDFromBytes(wfId.getBytes()).getLeastSignificantBits()
    ).toString();
  }
  
  public static String getCoapUri(String ip, int port, String resource) {    
    return "coap://" +  ip + ":" +  port + "/" + resource;
  }
}
