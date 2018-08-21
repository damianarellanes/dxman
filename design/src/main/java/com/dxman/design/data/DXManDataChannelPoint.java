package com.dxman.design.data;

/**
 * @author Damian Arellanes
 */
public class DXManDataChannelPoint {
    
  private final String parameterId;
  private final String parameterName;
  private final String operationName;
  private final String serviceName;

  public DXManDataChannelPoint(String parameterId, String parameterName, 
    String operationName, String serviceName) {
    
    // TODO change this to only parameterId
    if(operationName.isEmpty()) {
      this.parameterId = serviceName + "." + parameterName; 
    } else {
      this.parameterId = serviceName + "." + operationName + "." + parameterName; 
    }
    
    this.parameterName = parameterName;
    this.operationName = operationName;
    this.serviceName = serviceName;
  }

  public String getParameterId() { return parameterId; }
  public String getParameterName() { return parameterName; }
  public String getOperationName() { return operationName; }
  public String getServiceName() { return serviceName; }

  @Override
  public String toString() {
    return serviceName + "." + operationName + "." + parameterName;
  }
}
