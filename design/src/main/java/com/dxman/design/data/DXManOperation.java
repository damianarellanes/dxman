package com.dxman.design.data;

import com.dxman.design.distribution.DXManBindingInfo;
import com.dxman.utils.DXManMap;

/**
 * @author Damian Arellanes
 */
public class DXManOperation {
        
  private DXManBindingInfo bindingInfo;
  private String name;
  private DXManMap<String, DXManParameter> parameters = new DXManMap<>();
  private DXManMap<String, DXManParameter> inputs = new DXManMap<>();
  private DXManMap<String, DXManParameter> outputs = new DXManMap<>();

  public DXManOperation() {}

  public DXManOperation(String name, DXManBindingInfo bindingInfo) {    
    this.name = name;
    this.bindingInfo = bindingInfo;
  }

  public void addParameter(DXManParameter parameter) {

    parameters.put(parameter.getName(), parameter);

    if(parameter.getParameterType().equals(DXManParameterType.INPUT)) {            
        inputs.put(parameter.getName(), parameter);
    } else {
        outputs.put(parameter.getName(), parameter);
    }
  }

  public DXManBindingInfo getBindingInfo() { return bindingInfo; }
  public void setBindingInfo(DXManBindingInfo bindingInfo) {
    this.bindingInfo = bindingInfo;
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public DXManMap<String, DXManParameter> getParameters() { return parameters; }
  public void setParameters(DXManMap<String, DXManParameter> parameters) { 
    this.parameters = parameters; 
  }

  public DXManMap<String,DXManParameter> getInputs() { return inputs; }    
  public void setInputs(DXManMap<String, DXManParameter> inputs) { 
    this.inputs = inputs; 
  }

  public DXManMap<String,DXManParameter> getOutputs() { return outputs; }
  public void setOutputs(DXManMap<String, DXManParameter> outputs) { 
    this.outputs = outputs; 
  }

  @Override
  public String toString() {
    return "DXManOperation{" + "bindingInfo=" + bindingInfo + ", name=" + name 
      + ", parameters=" + parameters + '}';
  }
}
