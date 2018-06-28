package com.dxman.utils;

/**
 * @author Damian Arellanes
 */
public interface DXManConfiguration {
  
  /**************************************************************************/
  // Configuration File
  /**************************************************************************/
  public static final String THING_ALIAS_TAG = "thing.alias";
  public static final String THING_IP_TAG = "thing.ip";
  public static final String THING_PORT_TAG = "thing.port";
  public static final String DATASPACE_ENDPOINT_TAG = "dataspace.endpoint";
  
  /**************************************************************************/
  // Service Template Configuration
  /**************************************************************************/
  public static final String WILDCARD_START = "##";
  public static final String WILDCARD_END = "##";
  
  /**************************************************************************/
  // Deployment Configuration
  /**************************************************************************/
  public static final String DEPLOYER_RESOURCE = "deployer";
}
