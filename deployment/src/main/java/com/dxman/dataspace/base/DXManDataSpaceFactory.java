package com.dxman.dataspace.base;

import com.dxman.dataspace.blockchain.DXManBlockChainManager;

/**
 * @author Damian Arellanes
 */
public class DXManDataSpaceFactory {
    
  public static DXManDataSpace createBlockchainManager(String endpoint) {
    return new DXManBlockChainManager(endpoint);
  }
}
