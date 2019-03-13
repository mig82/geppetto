package com.kony.geppetto.ops;

import com.google.gson.internal.LinkedTreeMap;
import com.kony.geppetto.connectors.FabricConnector;
import com.kony.geppetto.exceptions.GeppettoException;
import com.kony.geppetto.proxies.MapProxy;
import com.kony.geppetto.proxies.ParamProxy;
import com.kony.geppetto.proxies.RecordProxy;
import com.kony.geppetto.proxies.ResultProxy;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Call a specific service for each ocurrence of an array.
 * */
public class LoopOperation extends Operation{

	private static final String CLOUD_ENV = "cloud_env";
	private static final String END_POINT = "end_point";

	private static final Logger LOGGER = Logger.getLogger( FabricConnector.class.getName() );


	protected ResultProxy invokeOperation(String opId, MapProxy cfgMap, MapProxy inMap, MapProxy outMap, MapProxy headers){
		ResultProxy result = new ResultProxy();

		String endPoint = inMap.getString(END_POINT, null);

		String host = headers.getString(FabricConnector.X_FABRIC_HOST, null);

		String fabricKey = headers.getString(FabricConnector.X_FABRIC_KEY, null);
		String fabricSecret = headers.getString(FabricConnector.X_FABRIC_SECRET, null);
		String fabricAuth = headers.getString(FabricConnector.X_FABRIC_AUTH, null);

		FabricConnector fabric = new FabricConnector(host, endPoint, fabricKey, fabricSecret, fabricAuth);

		/*For lack of a better mechanism, we assume that the array of items to iterate over is delivered under
		attribute opId -e.g.: If the endpoint is /foo
		{
			"foo": [...]
		}*/
		Collection<Map<String, Object>> items = inMap.getMapCollection(opId);

		if(items != null){
			int k = 0;
			RecordProxy responsesRecord = new RecordProxy("responses");
			for (Map<String, Object> item : items) {
				//result.addParam("item_" + k, item.toString());
				k++;

				try {

					/*result.addParam("item_" + k, String.format(
						"%s: %s",
						item.getClass().getCanonicalName(),
						Arrays.toString(item.entrySet().toArray())
					));*/

					LOGGER.log(Level.FINE, "Geppetto iterating over items\n\tIndex: {0}\n\tClass: {1}\n\tValue: {2}", new Object[]{
						k,
						item == null ? null : item.getClass().getCanonicalName(),
						item == null ? null : Arrays.toString(item.entrySet().toArray())
					});

					String response = fabric.callService(item);

					//TODO:Add this as a record in a 'responses' array.
					responsesRecord.addParam("item_" + k, response);
				}
				catch (GeppettoException e) {
					result.addGeppettoException(e);
				}
				catch (UnsupportedEncodingException e) {
					result.addStandardException(e);
				}
			}
			result.addRecord(responsesRecord);
		}

		return result;
	}
}
