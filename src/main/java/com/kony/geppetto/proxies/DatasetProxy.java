package com.kony.geppetto.proxies;

import com.konylabs.middleware.dataobject.Dataset;

public class DatasetProxy {

	private final Dataset dataSet;

	public DatasetProxy(String id) {
		dataSet = new Dataset();
		dataSet.setId(id);
	}

	public DatasetProxy(Dataset dataSet) {
		this.dataSet = dataSet;
	}

	public Dataset getDataSet() {
		return dataSet;
	}

	public DatasetProxy addRecord(RecordProxy record){
		dataSet.addRecord(
			record.getRecord()
		);
		return this;
	}

	public static DatasetProxy getDataset(ResultProxy result, String id){
		return new DatasetProxy(
				result.getResult().getDatasetById(id)
		);
	}
}
