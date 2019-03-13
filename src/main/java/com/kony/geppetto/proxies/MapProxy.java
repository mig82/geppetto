package com.kony.geppetto.proxies;

import com.kony.geppetto.json.JsonHelper;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Collection;
//import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;

public class MapProxy {

	private static final Logger LOGGER = Logger.getLogger( MapProxy.class.getName() );

	private Map<String, Object> map;

	public MapProxy(Map<String, Object> map){
		this.map = map;
	}

	public MapProxy(Object[] maps, int index){

		//this.map = maps[index] == null ? new HashMap<String, Object>() : (HashMap<String, Object>)maps[index];
		if(index < maps.length && maps[index] != null){

			map = (HashMap<String, Object>)maps[index];
		}
		else{

			map = new HashMap<String, Object>();

			LOGGER.log(Level.INFO, "That's odd. Unable to find Fabric parameter map at index {0}.\n\t" +
					"There are only {1} maps in the maps array.", new Object[]{
					index,
					maps.length
			});
		}

	}

	//TODO: Make this case insensitive.
	public String getString(String key, String defaultValue){
		return map.containsKey(key) ? (String)map.get(key) : defaultValue;
	}

	public Boolean getBoolean(String key, Boolean defaultValue){

		Boolean booleanValue = defaultValue;

		if(map.containsKey(key)){
			if(map.get(key) instanceof Boolean){
				booleanValue = (Boolean)map.get(key);
			}
			else if (map.get(key) instanceof String){
				booleanValue = Boolean.parseBoolean((String)map.get(key));
			}
		}
		return booleanValue;
	}

	public Integer getInteger(String key, Integer defaultValue){

		Integer integerValue = defaultValue;

		if(map.containsKey(key)){
			if(map.get(key) instanceof Integer){
				integerValue = (Integer)map.get(key);
			}
			else if (map.get(key) instanceof String){
				integerValue = Integer.parseInt((String)map.get(key));
			}
		}
		return integerValue;
	}

	public Map<String, Object> getMap(String key){

		if(map.containsKey(key) && map.get(key) != null){
			String json = (String)map.get(key);
			return JsonHelper.toMap(json);
		}
		else{
			return null;
		}
	}

	public Collection<String> getStringCollection(String key){

		if(map.containsKey(key) && map.get(key) != null){
			String json = (String)map.get(key);
			return JsonHelper.toStringCollection(json);
		}
		else{
			return null;
		}
	}

	public Collection<Integer> getIntCollection(String key){

		if(map.containsKey(key) && map.get(key) != null){
			String json = (String)map.get(key);
			return JsonHelper.toIntCollection(json);
		}
		else{
			return null;
		}
	}

	/**
	 * Retrieves a value from the map and parses is into a Collection of Maps -e.g.: [{"a":1, "b": "foo"}, {"c":3}].
	 * */
	public Collection<Map<String, Object>> getMapCollection(String key){

		if(map.containsKey(key) && map.get(key) != null){
			String json = (String)map.get(key);
			return JsonHelper.toCollection(json);
		}
		else{
			return null;
		}
	}

	/**
	 * Returns the name of the class of the value for a given key.
	 * This is specially useful for determining what type is Fabric receiving a parameter as.
	 * */
	public String getClassName(String key){
		if(map.containsKey(key) && map.get(key) != null){
			return map.get(key).getClass().getCanonicalName();
		}
		else{
			return null;
		}
	}

	@Override
	public String toString() {
		return Arrays.toString(map.entrySet().toArray());
	}
}
