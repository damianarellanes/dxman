package com.dxman.design.data;

import com.dxman.design.distribution.DXManBindingInfo;
import com.dxman.utils.*;

/**
 * @author Damian Arellanes
 */
public class DXManOperation implements Cloneable {
        
  private String id;
  private DXManBindingInfo bindingInfo;
  private String name;
  private DXManMap<String, DXManParameter> parameters = new DXManMap<>();
  private DXManMap<String, DXManParameter> inputs = new DXManMap<>();
  private DXManMap<String, DXManParameter> outputs = new DXManMap<>();

  public DXManOperation() {}

  public DXManOperation(String name, DXManBindingInfo bindingInfo) {    
    this.id = DXManIDGenerator.generateOperationID(name);
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
  
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

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
  
  @Override
  public DXManOperation clone() {

    Object clone = null;
    try {
      clone = super.clone();
    } 
    catch(CloneNotSupportedException e) {
      System.err.println(e.toString());
    }

    // Operations are unique regarless they make reference to any in sub-services
    String newId = DXManIDGenerator.generateOperationID(name);               
    ((DXManOperation) clone).setId(newId);
    //((DXManOperation) clone).setName(name.split("\\.")[0] +"." + newId);

    // Deep clone of inputs and outputs
    ((DXManOperation)clone).setInputs(new DXManMap<>());        
    for(DXManParameter input: inputs.values()) {
      ((DXManOperation)clone).addParameter(input.clone());
    }
    ((DXManOperation)clone).setOutputs(new DXManMap<>());        
    for(DXManParameter output: outputs.values()) {
      ((DXManOperation)clone).addParameter(output.clone());
    }        

    return (DXManOperation) clone;
  }
}
