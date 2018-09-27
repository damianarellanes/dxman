package com.dxman.dataspace.blockchain;

import com.dxman.dataspace.base.*;

/**
 * @author Damian Arellanes
 */
public class BlockchainDataEntityFactory implements DXManDataEntityFactory {

  @Override
  public synchronized DXManDataParameter createDataParameter(String parameterId, 
    String workflowId, String value) {
    
    return new BlockchainParameter(parameterId, workflowId, value);
  }

  @Override
  public DXManDataReducer createDataReducer(String parameterId, String workflowId, 
    String value, String chaincodeId) {
    return new BlockchainReducer(parameterId, workflowId, value, chaincodeId);
  }

  @Override
  public DXManDataMapper createDataMapper(String parameterId, String workflowId, 
    String value, String chaincodeId) {
    return new BlockchainMapper(parameterId, workflowId, value, chaincodeId);
  }
}
