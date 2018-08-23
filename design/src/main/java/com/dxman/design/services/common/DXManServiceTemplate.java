package com.dxman.design.services.common;

import com.dxman.design.data.DXManOperation;
import com.dxman.design.distribution.DXManDeploymentInfo;
import com.dxman.utils.DXManMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author darellanes
 */
@Document(collection = "services")
public class DXManServiceTemplate {
    
  protected String classTypeServ = getClass().getName();
  
  @Id
  private String id;
  private DXManServiceInfo info;
  private DXManServiceType type;
  private DXManMap<String, DXManOperation> operations;  
  
  private DXManDeploymentInfo deploymentInfo;

  public DXManServiceTemplate() {}

  public DXManServiceTemplate(DXManServiceInfo info, DXManServiceType type, 
    DXManDeploymentInfo deploymentInfo) {
    
    this.info = info;
    this.type = type;
    operations = new DXManMap<>();
    
    this.deploymentInfo = deploymentInfo;
  };

  public void addOperation(DXManOperation operation) {
    operations.put(operation.getName(), operation);
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public DXManServiceInfo getInfo() { return info; }
  public void setInfo(DXManServiceInfo info) { this.info = info; }
  
  public DXManServiceType getType() { return type; }
  public void setType(DXManServiceType type) { this.type = type; }

  public DXManMap<String, DXManOperation> getOperations() { return operations; }
  public void setOperations(DXManMap<String, DXManOperation> operations) { 
    this.operations = operations; 
  }
  
  public DXManDeploymentInfo getDeploymentInfo() { return deploymentInfo; }
  public void setDeploymentInfo(DXManDeploymentInfo deploymentInfo) {
    this.deploymentInfo = deploymentInfo;
  }

  @Override
  public String toString() {
    return "DXManService{" + "id=" + id + ", info=" + info 
      + ", operations=" + operations + ", type=" + type + '}';
  } 
}
