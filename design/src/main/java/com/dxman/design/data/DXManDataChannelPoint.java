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
    this.parameterId = serviceName + "." + operationName + "." + parameterName; // TODO change this to only parameterId
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
