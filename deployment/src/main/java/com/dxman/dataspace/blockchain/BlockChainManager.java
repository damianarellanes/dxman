package com.dxman.dataspace.blockchain;

import com.dxman.dataspace.base.*;
import com.dxman.utils.*;
import com.dxman.utils.DXManIDGenerator;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import org.javalite.http.*;
import org.json.*;

/**
 * @author Damian Arellanes
 */
public class BlockChainManager implements DXManDataSpace {
  
  private final String blockChainEndpoint;
  private final String THING_CLASS = "com.dxman.blockchain.Thing";
  private final String PARAM_CLASS = "com.dxman.blockchain.Parameter";
  private final String MAPPER_CLASS = "com.dxman.blockchain.Mapper";
  private final String REDUCER_CLASS = "com.dxman.blockchain.Reducer";  
  private final String UPDATE_DATAENT_CON = "com.dxman.blockchain.UpdateDataEntityConcept";
  private final String CREATE_DATAENTS = "com.dxman.blockchain.CreateDataEntities";
  private final String UPDATE_DATAENT = "com.dxman.blockchain.UpdateDataEntity";
  private final String UPDATE_DATAENTS = "com.dxman.blockchain.UpdateDataEntities";
  private final String READ_PARAMS_CLASS = "com.dxman.blockchain.ReadParameters";
  
  private final BlockchainDataEntityFactory dataEntityFactory;
  
  public BlockChainManager(String blockChainEndpoint) {
    this.blockChainEndpoint = blockChainEndpoint;
    this.dataEntityFactory = new BlockchainDataEntityFactory();
  }
  
  @Override
  public int registerThing(String id, String alias) {
    
    /*try {
      
      JSONObject node = new JSONObject();
      node.put("$class", THING_CLASS);
      node.put("thingId", id);
      node.put("alias", alias);

      Post result = post(
        blockChainEndpoint + "/api/" + THING_CLASS, 
        node.toString()
      );

      switch(result.responseCode()) {
        case 500:
          System.err.println("Participant " + id +" already exists!");
          break;
        case 200:
          System.out.println("Participant " + id +" has been added!");
          break;
      }

      return result.responseCode();
    } catch (JSONException ex) { System.err.println(ex.toString()); }*/
    
    return 200;
  }
  
  @Override
  public void createDataEntities(List<DXManDataEntity> dataEntities, String workflowId) {
    
    /*Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String instant = timestamp.toInstant().toString();*/
    
    // Registers 20 parameters at a time to avoid any issue with the blockchain server
    int from = 0, to = 0;
    while(to < dataEntities.size()) {
      
      from = to;
      to = from + 20;

      if(dataEntities.size() < to) to = dataEntities.size();
  
      try {

        JSONObject transaction = new JSONObject();
        transaction.put("$class", CREATE_DATAENTS);
        JSONArray entitiesJSON = new JSONArray();
        for(DXManDataEntity entity: dataEntities.subList(from, to)) {
          entitiesJSON.put(entity.generateJSON());
        }

        transaction.put("dataEntities", entitiesJSON);
        
        Post result = post(blockChainEndpoint + 
          "/api/" + CREATE_DATAENTS, transaction.toString());

        if(result.responseCode() == 200) {
          //System.out.println((to-from) + " parameters have been registered!");
        }

      } catch (JSONException ex) { System.err.println(ex.toString()); }
    }
  }
  
  @Override
  public JSONArray readParameters(JSONArray paramRefs, 
    String workflowTimestamp) {
    
    try {
      
      JSONObject transaction = new JSONObject();
      transaction.put("$class", READ_PARAMS_CLASS);
      transaction.put("parameters", paramRefs);
      transaction.put("workflowTimestamp", workflowTimestamp);
            
      Post result = post(blockChainEndpoint + 
        "/api/" + READ_PARAMS_CLASS, transaction.toString());
      
      if(result.responseCode() != 200) {
        System.err.println("Error reading parameters!");
      }
        
      return new JSONArray(result.text());
    } catch (JSONException ex) {}
    
    return new JSONArray();
  }
  
  @Override
  public String readParameter(String parameterId, String workflowId, 
    String workflowTimestamp) {
    
    try {
      
      JSONObject query = new JSONObject();
      query.put("$class", PARAM_CLASS);
      query.put("parameterId", parameterId);
      query.put("workflowId", workflowId);

      StringBuilder url = new StringBuilder();
      url.append(blockChainEndpoint)
        .append("/api/queries/selectWorkflowParameter")
        .append("?parameterId=").append(parameterId).append("&")
        .append("workflowId=").append(workflowId).append("&")
        .append("workflowTimestamp=").append(workflowTimestamp);
      
      JSONArray result = new JSONArray(get(url.toString()));
      return ((JSONObject)result.get(0)).getString("value");
        
    } catch (JSONException ex) {       
      return DXManErrors.PARAMETER_VALUE_NOT_FOUND.name();
    }
  }
  
  @Override
  public void writeDataEntities(List<DXManDataEntity> dataEntities) {
    
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String instant = timestamp.toInstant().toString();
    
    // Writes 20 parameters at a time to avoid any issue with the blockchain server
    int from = 0, to = 0;
    while(to < dataEntities.size()) {
      
      from = to;
      to = from + 20;

      if(dataEntities.size() < to) to = dataEntities.size();
  
      try {

        JSONObject transaction = new JSONObject();
        transaction.put("$class", UPDATE_DATAENTS);
        JSONArray updatesJSON = new JSONArray();
        for(DXManDataEntity entity: dataEntities.subList(from, to)) {

          JSONObject updateJSON = new JSONObject();
          updateJSON.put("$class", UPDATE_DATAENT_CON);
          updateJSON.put("dataEntity", entity.getType() +"#" + 
            entity.getId());
          updateJSON.put("newValue", entity.getValue());
          updateJSON.put("writerId", entity.getId());
          updateJSON.put("timestamp", instant);
      
          updatesJSON.put(updateJSON);
        }

        transaction.put("updates", updatesJSON);

        Post result = post(blockChainEndpoint + 
          "/api/" + UPDATE_DATAENTS, transaction.toString());
        
        if(result.responseCode() == 200) {
          System.out.println((to-from) + " data has been updated!");
        }

      } catch (JSONException ex) { System.err.println(ex.toString()); }
    }
  }
  
  @Override
  public void writeParameter(String parameterId, String workflowId, 
    String newValue) {
    
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String instant = timestamp.toInstant().toString();
    
    try {
      
      JSONObject transaction = new JSONObject();
      transaction.put("$class", UPDATE_DATAENT);
      JSONObject updateJSON = new JSONObject();
      updateJSON.put("$class", UPDATE_DATAENT_CON);
      updateJSON.put("dataEntity", PARAM_CLASS + "#" + 
        DXManIDGenerator.generateParameterUUID(parameterId, workflowId));
      updateJSON.put("newValue", newValue);
      updateJSON.put("writerId", parameterId);
      updateJSON.put("timestamp", instant);
      transaction.put("update", updateJSON);  

      Post result = post(blockChainEndpoint + 
        "/api/" + UPDATE_DATAENT, transaction.toString());
      
      if(result.responseCode() == 200) {
        System.out.println("Parameter " + parameterId + " has been updated!");
      }
        
    } catch (JSONException ex) { System.err.println(ex.toString()); }
  }
  
  private Post post(String url, String content) {
    return Http.post(url, content)
      .header("Accept", "application/json")
      .header("Content-Type", "application/json");
  }
    
  private String get(String url) {
    return Http.get(url).text();
  }
  
  @Override
  public DXManDataEntityFactory getDataEntityFactory() {
    return dataEntityFactory;
  }
  
  public static void main(String[] args) {
        
    BlockChainManager blockchain = new BlockChainManager("http://148.100.5.104:3000");  
    DXManDataEntityFactory factory = blockchain.getDataEntityFactory();
    
    DXManDataParameter name = factory.createDataParameter("646e2529-2113-4dca-9a3b-050c7827670a", "MyWf-MusicCorp-2", "null");
    DXManDataParameter addr = factory.createDataParameter("6c33528e-f5d3-45f6-9843-dab467d7a839", "MyWf-MusicCorp-2", "null");
    
    System.out.println(name.getId());
    System.out.println(addr.getId());
    
    
    
    // Creation
    DXManDataParameter i1 = factory.createDataParameter("i1", "MyWfId", "null");
    DXManDataParameter i2 = factory.createDataParameter("i2", "MyWfId", "null");
    DXManDataParameter o1 = factory.createDataParameter("o1", "MyWfId", "null");
    DXManDataParameter o2 = factory.createDataParameter("o2", "MyWfId", "null");
    DXManDataParameter o3 = factory.createDataParameter("o3", "MyWfId", "null");
    DXManDataParameter o4 = factory.createDataParameter("o4", "MyWfId", "null");    
    //DXManDataMapper mapper1 = factory.createDataMapper("mapper1", "MyWfId", "null", "mapper");
    //DXManDataReducer reducer1 = factory.createDataReducer("redcuer1", "MyWfId", "null", "reducer");
        
    // Data channels    
    i1.addReader(i2); i1.addReader(o1);
    o2.addReader(o3);
    /*i1.addReader(mapper1);
    i2.addReader(mapper1);    
    mapper1.addReader(o1);mapper1.addReader(reducer1);mapper1.addWriter(i1);mapper1.addWriter(i2);    
    reducer1.addReader(o2);reducer1.addWriter(mapper1);
    o1.addReader(o3);
    o2.addReader(o4);*/
//    
//    long start, end;
//    
//    List<DXManDataParameter> parameters = new ArrayList<>();
//    parameters.add(i1); parameters.add(i2);
//    parameters.add(o1); parameters.add(o2); parameters.add(o3);
//    parameters.add(o4);
//    String currentWorkflow = "MyWfId";
//    JSONArray paramRefs = new JSONArray();
//    for(DXManDataParameter parameter: parameters) {
//      paramRefs.put(parameter.getType() + "#" 
//      + DXManIDGenerator.generateParameterUUID(
//        parameter.getParameterId(), currentWorkflow));
//    }    
//    start = System.currentTimeMillis();         
//    JSONArray result = blockchain.readParameters(paramRefs, "2017-09-19T20:41:53.491Z");    
//    end = System.currentTimeMillis();
//    System.out.println(end - start);
//    
//    System.out.println(result.toString());
    
    // Batch of entities
    List<DXManDataEntity> entities = new ArrayList<>();
    entities.add(i1); entities.add(i2); entities.add(o1); entities.add(o2); entities.add(o3);
    entities.add(o4); //entities.add(mapper1); entities.add(reducer1);
        
    // Call the blockchain transaction
    //blockchain.createDataEntities(entities, "MyWfId");
        
    //"2018-01-02T11:42Z";
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    Instant instant = timestamp.toInstant();
    
    System.out.println("----------------UPDATING...----------");
    //blockchain.writeParameter("i1", "MyWfId", "Interfaces done 2");    
    //System.out.println(blockchain.readParameter("i1", "MyWfId", instant.toString()));
    
    List<DXManDataEntity> updates = new ArrayList<>();
    DXManDataParameter i1_up = factory.createDataParameter("i1", "MyWfId", "Hello i1");
    DXManDataParameter i2_up = factory.createDataParameter("o2", "MyWfId", "Yeah o2");
    //DXManDataParameter i1_up = factory.createDataParameter("i1", "MyWfId", "IPX 1");
    //DXManDataParameter i2_up = factory.createDataParameter("i2", "MyWfId", "IPY 2");    
    updates.add(i1_up); updates.add(i2_up);
    blockchain.writeDataEntities(updates);
    
    /*System.out.println("i1-->" + blockchain.readParameter("i1", "MyWfId", instant.toString()));
    System.out.println("i2-->" + blockchain.readParameter("i2", "MyWfId", instant.toString()));
    System.out.println("o1-->" + blockchain.readParameter("o1", "MyWfId", instant.toString()));
    System.out.println("o2-->" + blockchain.readParameter("o2", "MyWfId", instant.toString()));
    System.out.println("o3-->" + blockchain.readParameter("o3", "MyWfId", instant.toString()));
    System.out.println("o4-->" + blockchain.readParameter("o4", "MyWfId", instant.toString()));*/
    
    
    List<DXManDataParameter> parameters = new ArrayList<>();
    parameters.add(i1); parameters.add(i2);
    parameters.add(o1); parameters.add(o2); parameters.add(o3);
    parameters.add(o4);
    String currentWorkflow = "MyWfId";
    JSONArray paramRefs = new JSONArray();
    for(DXManDataParameter parameter: parameters) {
      paramRefs.put("resource:" + parameter.getType() + "#" 
      + DXManIDGenerator.generateParameterUUID(
        parameter.getParameterId(), currentWorkflow));
    }    
    JSONArray result = blockchain.readParameters(paramRefs, "2017-09-19T20:41:53.491Z");
    
    System.out.println(result.toString());
    
    /*String par1UUID = generateParameterUUID("Par1", "MyWfId");System.out.println("PAR1:" + par1UUID);
    String par2UUID = generateParameterUUID("Par2", "MyWfId");System.out.println("PAR2:" + par2UUID);
    String par3UUID = generateParameterUUID("Par3", "MyWfId");System.out.println("PAR3:" + par3UUID);
    
    List<String> readers = Arrays.asList("Par2", "Par3");
    blockchain.registerParameter("Par1", "MyWfId", "Hello World Initial", readers);
    List<String> readers2 = new ArrayList<>();
    blockchain.registerParameter("Par2", "MyWfId", "null", readers2);
    List<String> readers3 = new ArrayList<>();
    blockchain.registerParameter("Par3", "MyWfId", "null", readers3);

    //"2018-01-02T11:42Z";
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    Instant instant = timestamp.toInstant();
    //System.out.println(instant);
    
    System.out.println("Par1-->" + blockchain.readParameter("Par1", "MyWfId", "2017-08-24T17:17:53.666Z"));
    System.out.println("Par2-->" + blockchain.readParameter("Par2", "MyWfId", "2017-08-24T17:17:53.666Z"));
    System.out.println("Par3-->" + blockchain.readParameter("Par3", "MyWfId", "2017-08-24T17:17:53.666Z"));
    
    System.out.println("----------------UPDATING...----------");
    blockchain.writeParameter("Par1", "MyWfId", "AN UPDATE HAS BEEN DONE!!!");
    
    System.out.println("Par1-->" + blockchain.readParameter("Par1", "MyWfId", instant.toString()));
    System.out.println("Par2-->" + blockchain.readParameter("Par2", "MyWfId", instant.toString()));
    System.out.println("Par3-->" + blockchain.readParameter("Par3", "MyWfId", instant.toString()));*/
  }
}
