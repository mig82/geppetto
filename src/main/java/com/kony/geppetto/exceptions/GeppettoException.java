package com.kony.geppetto.exceptions;

/**
 * An exception that occurs when calling a Fabric service operation is not successful.
 * */
public class GeppettoException extends Exception {
	private Integer opStatus = 0;
	private Integer httpStatus = 200;
	private String message;
	private String payload;

	public GeppettoException(Integer opStatus, Integer httpStatus, String message, String payload){
		this.opStatus = opStatus;
		this.httpStatus = httpStatus;
		this.message = message;
		this.payload = payload;
	}

	public Integer getOpStatus() {
		return opStatus;
	}

	public Integer getHttpStatus() {
		return httpStatus;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public String getPayload() {
		return payload;
	}
}
