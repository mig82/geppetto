package com.kony.geppetto.ops;

import com.kony.geppetto.proxies.MapProxy;
import com.kony.geppetto.proxies.ResultProxy;

public class PingOperation extends Operation {

	protected ResultProxy invokeOperation(String opId, MapProxy cfgMap, MapProxy inMap, MapProxy outMap, MapProxy headers){
		ResultProxy result = new ResultProxy();
		result.addParam("ping", "Eureka!");
		return result;
	}
}
