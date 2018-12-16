package com.dxman.dataspace.blockchain;

/**
 * @author Damian Arellanes
 */
public interface BlockchainConfiguration {
    
  public static String NETWORK_ID = "n82e2caf53e644d0fbc50592aff6aafcd";
  public static String FABRIC_USER = "org1";
  public static String FABRIC_PASSWORD = "9avUTSeUHaeZ7Uy1MjXIi3_KJryOCB9p_MhF1gPsT0_w7ZmLM90f3i44tNyDQLVb";
  public static String FABRIC_URI = "https://blockchain-starter.eu-gb.bluemix.net/api/v1/networks/" + NETWORK_ID;
  
  public final String THING_CLASS = "com.dxman.blockchain.Thing";
  public final String UPDATE_PARAM_CON = "com.dxman.blockchain.UpdateParameterConcept";
  public final String PARAM_CLASS = "com.dxman.blockchain.Parameter";
  public final String CREATE_PARAMS = "com.dxman.blockchain.CreateParameters";
  public final String UPDATE_PARAM = "com.dxman.blockchain.UpdateParameter";
  public final String UPDATE_PARAMS = "com.dxman.blockchain.UpdateParameters";
  public final String READ_PARAM = "com.dxman.blockchain.ReadParameter";
  public final String READ_PARAMS = "com.dxman.blockchain.ReadParameters";
  public final String TIMESTAMP_CLASS = "com.dxman.blockchain.GetBlockchainTimestamp";
}
