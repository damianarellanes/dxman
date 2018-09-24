package com.dxman.dataspace.blockchain;

import com.dxman.dataspace.base.DXManDataReducer;

/**
 * @author Damian Arellanes
 */
public class BlockchainReducer extends BlockchainDataProcessor 
  implements DXManDataReducer {
    
  public BlockchainReducer(String parameterId, String workflowId, 
    String value, String chaincodeId) {
    super("com.dxman.blockchain.Reducer", parameterId, workflowId, value, chaincodeId);
  }
}
