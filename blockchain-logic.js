/**
 * Creates a batch of parameters
 * @param {com.dxman.blockchain.CreateParameters} tx - the transaction info
 * @transaction
 */
async function createParameters(tx) {
  
  var resources = [];
  let registry = await getAssetRegistry("com.dxman.blockchain.Parameter");  
  for(i = 0; i < tx.parameters.length; i++) {
    
  	let resource = getFactory().newResource("com.dxman.blockchain", "Parameter", tx.parameters[i].id);
    resource.parameterId = tx.parameters[i].parameterId;
    resource.workflowId = tx.parameters[i].workflowId;
    resource.value = tx.parameters[i].value;
    resource.timestamp = tx.timestamp;
    resource.updater = tx.parameters[i].updater;
	resource.writers = tx.parameters[i].writers;

    resources[i] = await registry.add(resource);
  }
}

/**
 * Reads a bunch of parameters
 * @param {com.dxman.blockchain.ReadParameters} tx - the transaction to be processed
 * @returns {string[]}
 * @transaction
 */
async function readParametersTx(tx) {  
  
  var parameters = [];
  for(let i = 0; i < tx.parameters.length; i++) {    
    parameters[i] = readParameter(tx.parameters[i], tx.workflowTimestamp);
  }
    
  let result = await Promise.all(parameters).then(values => {
    return values;
  }, error => {
    return [];
  });
  
  alert(result);
  return result;
}

/**
 * Reads a parameter
 * @param {com.dxman.blockchain.ReadParameter} tx - the transaction to be processed
 * @returns {string}
 * @transaction
 */
async function readParameterTx(tx) {
  
  let value = await readParameter(tx.parameter, tx.workflowTimestamp).then(res => {
    return res;
  }, error => {    
    return error;
  });
  
  return value;
}

function readParameter(parameter, workflowTimestamp) {
  
  if(parameter.writers.length > 0) { 
    for(i = 0; i < parameter.writers.length; i++) {   
      // Only if the writer writes during the execution of the current workflow
      // Only if the writer writes by itself or the user, not by another parameter
      if(parameter.writers[i].timestamp >= workflowTimestamp && 
        (parameter.writers[i].updater == parameter.writers[i].id ||
        parameter.writers[i].updater == "user")) { 
        return Promise.resolve(parameter.writers[i].value);
      }
    }      
    return Promise.reject('PARAMETER_VALUE_NOT_FOUND');
  } else return Promise.reject('NO_WRITERS'); 
}

/**
 * Updates a bunch of parameters
 * @param {com.dxman.blockchain.UpdateParameters} tx - the transaction to be processed
 * @transaction
 */
async function updateParameters(tx) {
  
  // Sets the timestamp and new value for all parameters
  let paramsArray = [];    
  for(i = 0; i < tx.updates.length; i++) {        
    tx.updates[i].parameter.timestamp = tx.timestamp;
    tx.updates[i].parameter.value = tx.updates[i].newValue;
    tx.updates[i].parameter.updater = tx.updates[i].updater;
    paramsArray[i] = tx.updates[i].parameter;
    
    // Emits the update event
    let updateEvent = getFactory().newEvent('com.dxman.blockchain', 'UpdateParameterEvent');
    updateEvent.parameter = tx.updates[i].parameter.id;
    updateEvent.newValue = tx.updates[i].parameter.value;
    updateEvent.updater = tx.updates[i].updater;
    emit(updateEvent);
  }
  
  // Persists the state of all the parameters
  let registry = await getAssetRegistry("com.dxman.blockchain.Parameter");
  await registry.updateAll(paramsArray);
}

/**
 * Updates a parameter
 * @param {com.dxman.blockchain.UpdateParameter} tx - the transaction to be processed
 * @transaction
 */
async function updateParameter(tx) {
  
  // Sets the timestamp and new value of the parameter
  tx.update.parameter.timestamp = tx.timestamp;
  tx.update.parameter.value = tx.update.newValue;
  tx.update.parameter.updater = tx.update.updater;
  
  // Emits the update event
  let updateEvent = getFactory().newEvent('com.dxman.blockchain', 'UpdateParameterEvent');
  updateEvent.parameter = tx.update.parameter.id;
  updateEvent.newValue = tx.update.parameter.value;
  updateEvent.updater = tx.update.updater;
  emit(updateEvent);

  // Persists the state of the parameter
  let registry = await getAssetRegistry("com.dxman.blockchain.Parameter");
  await registry.update(tx.update.parameter);
}