package com.kony.geppetto.connectors;

import com.google.gson.Gson;
import com.kony.geppetto.exceptions.GeppettoException;
import com.kony.geppetto.proxies.MapProxy;
import com.kony.geppetto.util.HttpEntityGenerator;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FabricConnector extends Connector{

	private static final Logger LOGGER = Logger.getLogger( FabricConnector.class.getName() );

	public static final String X_FABRIC_KEY = "x-kony-app-key";
	public static final String X_FABRIC_SECRET = "x-kony-app-secret";
	public static final String X_FABRIC_AUTH = "x-kony-authorization";
	public static final String X_FABRIC_HOST = "x-forwarded-host"; //We could also use x-forwarded-server

	private final String host;
	private final String endPoint;
	private final String fabricKey;
	private final String fabricSecret;
	private final String fabricAuth;

	public FabricConnector(String host, String endPoint, String fabricKey, String fabricSecret, String fabricAuth){

		this.host = host;
		this.endPoint = endPoint;
		this.fabricKey = fabricKey;
		this.fabricSecret = fabricSecret;
		this.fabricAuth = fabricAuth;
	}

	public String 	callService(Map input) throws GeppettoException, UnsupportedEncodingException {

		String path = String.format("/services/%s", endPoint);

		//Let's build the url for the request.
		URI uri = buildUri(host, path, null);

		//TODO: Fabric services always accept POST by default, but it would be more elegant to use the appropriate verb here.
		HttpPost request = createHttpPost(uri);

		//Let's add the headers to call anonymous or authenticated Fabric operations.
		request = addFabricHeaders(request);

		if(input != null) {
			UrlEncodedFormEntity entity = HttpEntityGenerator.toUrlEncodedFormEntity(input);
			request.setEntity(entity);
		}

		//Let's peek inside before we send it.
		LOGGER.log(Level.FINE, "request: {0}", request.toString());

		//Let's execute the Http request.
		return executeHttpRequest(request, false);
	}

	/**
	 * Adds the x-kony-authorization header for authenticated operations or the Basic authorization header
	 * for anonymous operations if the x-kony-app-key and x-kony-app-secret headers are present in the request.
	 * */
	private HttpPost addFabricHeaders(HttpPost request){

		if(fabricAuth != null){
			Header fabricAuthHeader = new BasicHeader(X_FABRIC_AUTH, fabricAuth);
			request.addHeader(fabricAuthHeader);
		}
		else if(fabricKey != null && fabricSecret != null){
			Header basicAuthHeader = generateBasicAuthenticationHeader(fabricKey, fabricSecret);
			request.addHeader(basicAuthHeader);
		}

		return request;
	}

	public static void main(String[] args){
		Base64 b = new Base64();
		String authHeader = b.encodeAsString(String.format("%s:%s", "112233", "112233").getBytes());
		System.out.println(authHeader);
		assert authHeader.equals("MTEyMjMzOjExMjIzMw==");
	}
}
