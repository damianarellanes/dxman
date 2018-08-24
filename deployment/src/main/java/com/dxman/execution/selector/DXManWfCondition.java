package com.dxman.execution.selector;

/**
 * @author Damian Arellanes
 */
public class DXManWfCondition {
    
  private String parameterId;
  private DXManWfConditionOperator operator;
  private String value;
    
  public DXManWfCondition(String parameterId, 
    DXManWfConditionOperator operator, String value) {
      this.parameterId = parameterId;
      this.operator = operator;
      this.value = value;
  }

  public String getParameterId() { return parameterId; }
  public void setParameterId(String parameterId) {
      this.parameterId = parameterId;
  }

  public DXManWfConditionOperator getOperator() { return operator; }
  public void setOperator(DXManWfConditionOperator operator) { 
    this.operator = operator; 
  }

  public String getValue() { return value; }
  public void setValue(String value) { this.value = value; }

  @Override
  public String toString() {
    return "DXManWfCondition{" + "parameterId=" 
      + parameterId + ", operator=" + operator + ", value=" + value + '}';
  }
}
