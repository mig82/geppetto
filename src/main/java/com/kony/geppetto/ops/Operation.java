package com.kony.geppetto.ops;

import com.google.gson.Gson;
import com.kony.geppetto.exceptions.GeppettoException;
import com.kony.geppetto.exceptions.OperationNotFoundException;
import com.kony.geppetto.proxies.MapProxy;
import com.kony.geppetto.proxies.ResultProxy;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Operation implements JavaService2{

	private static final Logger LOGGER = Logger.getLogger( Operation.class.getName() );

	@Override
	public Result invoke(String opId, Object[] maps, DataControllerRequest request, DataControllerResponse response){

		try{
			return invoke(opId, maps, request).getResult();
		}
		catch (Exception e){

			LOGGER.log(Level.SEVERE, "Geppetto crashed trying to call operation {0}.\n\t" +
				"due to exception: {1}\n\t" +
				"with message: {2}\n\t" +
				"Stack trace: {3}",
				new Object[]{
					opId,
					e.getClass(),
					e.getMessage(),
					ExceptionUtils.getStackTrace(e)
				}
			);

			ResultProxy result = new ResultProxy();
			result.addStandardException(e);
			return result.getResult();
		}
	}

	private ResultProxy invoke(String opId, Object[] maps, DataControllerRequest request) {

		Gson gson = new Gson();

		MapProxy cfgMap = new MapProxy(maps, 0);
		MapProxy inMap = new MapProxy(maps, 1);
		MapProxy outMap = new MapProxy(maps, 2);

		LOGGER.log(Level.INFO, "Called method: {0}", new Object[]{
				opId,
				cfgMap,
				inMap,
				outMap
		});

		ResultProxy result = new ResultProxy();
		try {
			validateOperation(opId);
			MapProxy headers = new MapProxy(request.getHeaderMap());
			result = invokeOperation(opId, cfgMap, inMap, outMap, headers);
			result.addOk();
		}
		catch (GeppettoException e){
			/*if(result == null){
				result = new ResultProxy();
			};*/
			result.addGeppettoException(e);
		}
		finally {
			/*if(result == null){
				result = new ResultProxy();
			}*/
			result.addDebugStuff(cfgMap, inMap, outMap, new MapProxy(request.getHeaderMap()));
		}

		return result;
	}

	/**
	 * Throw and exception if there's no class in this package that matches the given operation id.
	* */
	private void validateOperation(String opId) throws OperationNotFoundException {
		//TODO: Find all classes annotated Symbol('echo') and throw an exception if none have a symbol matching this id.
		if(false /*findOperation(id) == null*/){
			throw new OperationNotFoundException(opId);
		}
	}

	protected abstract ResultProxy invokeOperation(String opId, MapProxy cfgMap, MapProxy inMap, MapProxy outMap, MapProxy headers) throws GeppettoException;
}
