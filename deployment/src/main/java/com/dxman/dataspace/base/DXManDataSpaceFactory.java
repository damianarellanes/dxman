package com.dxman.dataspace.base;

import com.dxman.dataspace.blockchain.BlockChainManager;

/**
 * @author Damian Arellanes
 */
public class DXManDataSpaceFactory {
    
  public static DXManDataSpace createBlockchainManager(String endpoint) {
    return new BlockChainManager(endpoint);
  }
}
