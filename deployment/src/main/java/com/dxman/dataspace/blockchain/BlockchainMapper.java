package com.dxman.dataspace.blockchain;

import com.dxman.dataspace.base.DXManDataMapper;

/**
 * @author Damian Arellanes
 */
public class BlockchainMapper extends BlockchainDataProcessor 
  implements DXManDataMapper {
    
  public BlockchainMapper(String parameterId, String workflowId, 
    String value, String chaincodeId) {
    super("com.dxman.blockchain.Mapper", parameterId, workflowId, value, chaincodeId);
  }
}
