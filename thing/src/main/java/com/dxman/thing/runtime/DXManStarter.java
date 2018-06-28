package com.dxman.thing.runtime;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.dataspace.base.DXManDataSpaceFactory;
import com.dxman.thing.deployment.common.DXManDeployer;
import com.dxman.thing.server.base.DXManServer;
import com.dxman.thing.server.base.DXManServerFactory;
import com.dxman.utils.DXManConfiguration;
import com.dxman.utils.DXManErrors;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class injects dependencies and sets a thing up
 * @author Damian Arellanes
 */
public class DXManStarter {

  public static DXManRuntime start(String configPath) {
    
    Properties config = loadConfig(configPath);
        
    // Sets the data space up
    DXManDataSpace dataSpace = setDataSpaceUp(config);
    
    // Sets the server up
    DXManServer server = DXManServerFactory.createCoap();    
    DXManThing thing = setThingUp(config, server, dataSpace);
    
    // Initializes the deployer (running at e.g., deployer-6be66ab6-3c23-49d2-b088-6a51bcee913d)
    DXManDeployer deployer = new DXManDeployer(server, dataSpace, thing.getId());
    server.initDeployerDispatcher(
      DXManConfiguration.DEPLOYER_RESOURCE /*+ "-" + thing.getId()*/, deployer
    );
    
    return new DXManRuntime(thing, dataSpace);
  }
  
  private static Properties loadConfig(String configPath) {
    
    Properties config = new Properties();
    try {
      InputStream input = new FileInputStream(configPath);
      config.load(input);
      
    } catch (IOException ex) {
      System.err.println("Error loading the configuration file");
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
    DXManDataSpace dataSpace) {
       
    DXManThing thing = new DXManThing(
      config.getProperty(DXManConfiguration.THING_ALIAS_TAG), 
      config.getProperty(DXManConfiguration.THING_IP_TAG), 
      Integer.valueOf(config.getProperty(DXManConfiguration.THING_PORT_TAG)),
      server
    );
    
    int responseCode = dataSpace.registerThing(thing.getId(), thing.getAlias());
    
    if(responseCode != 200) {
      System.exit(DXManErrors.THING_ALREADY_EXISTS.ordinal());
    }
    
    return thing;
  }
}
