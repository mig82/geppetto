package com.kony.geppetto.exceptions;

/**
 * An exception that occurs when the Fabric developer attempts to call a service operation not
 * defined by Geppetto.
 * */
public class OperationNotFoundException extends GeppettoException {
	public OperationNotFoundException(String opId){
		super(10400, 400, "Bad request: Geppetto doesn't recognise operation '" + opId +"'.", null);
	}
}
