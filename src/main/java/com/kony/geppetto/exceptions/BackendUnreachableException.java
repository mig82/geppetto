package com.kony.geppetto.exceptions;

import org.apache.http.client.methods.HttpRequestBase;

public class BackendUnreachableException extends GeppettoException {

	public BackendUnreachableException(HttpRequestBase request){
		super(10404, 404, "Not found: Could not reach " + request.toString(), null);
	}
}