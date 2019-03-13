package com.kony.geppetto.proxies;

import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;

public class RecordProxy {

	private final Record record;

	public RecordProxy(){
		record = new Record();
	}

	public RecordProxy(String id) {
		this();
		record.setId(id);
	}

	public RecordProxy(Record record){
		this.record = record;
	}

	public Record getRecord() {
		return record;
	}

	public RecordProxy addParam(String name, String value){
		record.addParam(new Param(name, value, ParamProxy.STRING));
		return this;
	}

	public RecordProxy addParam(String name, Boolean value){
		record.addParam(new Param(name, String.valueOf(value), ParamProxy.BOOLEAN));
		return this;
	}

	public RecordProxy addParam(String name, Integer value){
		record.addParam(new Param(name, String.valueOf(value), ParamProxy.NUMBER));
		return this;
	}

	public RecordProxy addParam(String name, Float value){
		record.addParam(new Param(name, String.valueOf(value), ParamProxy.NUMBER));
		return this;
	}

	public RecordProxy removeParam(String name){
		record.removeParamByName(name);
		return this;
	}
}
