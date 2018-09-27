package com.dxman.dataspace.blockchain;

/**
 * @author Damian Arellanes
 */
public interface BlockchainConfiguration {
    
  public static String NETWORK_ID = "n82e2caf53e644d0fbc50592aff6aafcd";
  public static String FABRIC_USER = "org1";
  public static String FABRIC_PASSWORD = "9avUTSeUHaeZ7Uy1MjXIi3_KJryOCB9p_MhF1gPsT0_w7ZmLM90f3i44tNyDQLVb";
  public static String FABRIC_URI = "https://blockchain-starter.eu-gb.bluemix.net/api/v1/networks/" + NETWORK_ID;
  
  public static String CHAINCODE_INSTALL_URI = FABRIC_URI + "/chaincode/install";
  public static String CHAINCODE_INSTANTIATE_URI = FABRIC_URI + "/channels/defaultchannel/chaincode/instantiate";  
}
