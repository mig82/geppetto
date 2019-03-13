package com.kony.geppetto.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonHelper {

	public static Map<String, Object> toMap(String json){
		Gson gson = new Gson();
		Map<String, Object> map = new HashMap<String, Object>();
		map = (Map<String, Object>) gson.fromJson(json, map.getClass());
		return map;
	}

	public static Collection<Integer> toIntCollection(String json){
		Gson gson = new Gson();

		Type collectionType = new TypeToken<Collection<Integer>>(){}.getType();
		Collection<Integer> collection = gson.fromJson(json, collectionType);

		return collection;
	}

	public static Collection<String> toStringCollection(String json){
		Gson gson = new Gson();

		Type collectionType = new TypeToken<Collection<String>>(){}.getType();
		Collection<String> collection = gson.fromJson(json, collectionType);

		return collection;
	}

	public static Collection<Map<String, Object>> toCollection(String json){
		Gson gson = new Gson();

		Type collectionType = new TypeToken<List<Map<String,Object>>>(){}.getType();
		Collection<Map<String, Object>> collection = gson.fromJson(json, collectionType);

		return collection;
	}

	//public static Collection<Map<String, Object>> toMap(String json){

}
