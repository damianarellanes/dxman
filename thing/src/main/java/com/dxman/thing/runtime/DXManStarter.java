package com.dxman.thing.runtime;

import com.dxman.dataspace.base.*;
import com.dxman.thing.deployment.common.DXManDeployer;
import com.dxman.thing.server.base.*;
import com.dxman.utils.*;
import java.io.*;
import java.util.Properties;

/**
 * This class injects dependencies and sets a thing up
 * @author Damian Arellanes
 */
public class DXManStarter {

  public static DXManThing createRuntime(String configPath) {
    
    Properties config = loadConfig(configPath);
    
    // Sets the data space up
    DXManDataSpace dataspace = setDataSpaceUp(config);
    
    // Sets the server up
    DXManServer server = DXManServerFactory.createCoap(
      Integer.valueOf(config.getProperty(DXManConfiguration.THING_PORT_TAG))
    );
    DXManThing thing = setThingUp(config, server, dataspace);
    
    // Initializes the deployer (running at e.g., deployer-thingAlias)
    DXManDeployer deployer = new DXManDeployer(server, dataspace);
    server.initDeployerDispatcher(
      DXManIDGenerator.getDeployerUUID(thing.getAlias()), deployer
    );
    
    System.out.println(thing.getAlias() + " listening at " 
      + "http://" + thing.getIp() + ":" + thing.getPort());
    
    return thing; 
  }
  
  private static Properties loadConfig(String configPath) {
    
    Properties config = new Properties();
    try {
      InputStream input = new FileInputStream(configPath);
      config.load(input);
      
    } catch (IOException ex) {
      System.err.println("Error loading the configuration file: " + configPath);
      System.exit(DXManErrors.CONFIG_ERROR.ordinal());
    }
    
    return config;
  }
  
  private static DXManDataSpace setDataSpaceUp(Properties config) {
    
    DXManDataSpace dataSpace = DXManDataSpaceFactory.createBlockchainManager(
      config.getProperty(DXManConfiguration.DATASPACE_ENDPOINT_TAG)
    );
    
    if(dataSpace == null) {
      System.exit(DXManErrors.DATASPACE_CREATION.ordinal());
    }
    
    return dataSpace;
  }
  
  private static DXManThing setThingUp(Properties config, DXManServer server, 
    DXManDataSpace dataspace) {
       
    DXManThing thing = new DXManThing(
      config.getProperty(DXManConfiguration.THING_ALIAS_TAG), 
      config.getProperty(DXManConfiguration.THING_IP_TAG), 
      Integer.valueOf(config.getProperty(DXManConfiguration.THING_PORT_TAG)),
      server, dataspace
    );
    
    /*int responseCode = dataSpace.registerThing(thing.getId(), thing.getAlias());
    
    if(responseCode != 200) {
      System.exit(DXManErrors.THING_ALREADY_EXISTS.ordinal());
    }*/
    
    return thing;
  }
}
