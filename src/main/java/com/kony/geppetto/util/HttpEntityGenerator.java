package com.kony.geppetto.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpEntityGenerator {

	public static UrlEncodedFormEntity toUrlEncodedFormEntity(Map<String, Object> map) throws UnsupportedEncodingException {

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();

		for(Map.Entry<String, Object> entry: map.entrySet()){

			formparams.add(new BasicNameValuePair(
				entry.getKey(),
				entry.getValue() == null ? null : entry.getValue().toString()
			)); // name and value of your param
		}

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		return entity;
	}
}
