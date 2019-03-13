package com.kony.geppetto.connectors;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kony.geppetto.exceptions.GeppettoException;
import com.kony.geppetto.exceptions.BackendUnreachableException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The parent class for all connectors. It exists solely to force extending Connector in order to leverage
 * the static inner class ConnectorHelper.
 * */
public class Connector {

	private static final Logger LOGGER = Logger.getLogger( Connector.class.getName() );

	/**
	 * Builds a URL based on the domain, the path and the GET parameters required.
	 * @param domain A base domain to use when building a resource endpoint -e.g. The 'api.kony.com' bit in 'https://api.kony.com/oauth/request_token'.
	 * @param path The path part of the URL relative to the domain -e.g. The 'oauth/request_token' bit in 'https://api.kony.com/oauth/request_token'.
	 * @param params A list of GET query parameters to be added to the URL.
	 * @return The URL built for the Oauth endpoint.
	 */
	protected URI buildUri(String domain, String path, HashMap<String, String> params) {

		LOGGER.log(Level.FINER, "Building url for\n\tdomain:{0}\n\tpath:{1}\n\tparams:{2}", new Object[]{ domain, path, params } );

		URI uri = null;
		try {
			URIBuilder builder = new URIBuilder()
					.setScheme("https")
					.setHost(domain)
					.setPath(path);

			if(params != null) {
				for(Map.Entry<String, String> entry : params.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					LOGGER.log(Level.FINEST, "Adding parameter [{0}:{1}] to url", new Object[]{ key, value } );
					builder.setParameter(key, value);
				}
			}
			uri = builder.build();
			LOGGER.log(Level.FINER ,"Built uri: {0}", uri.toString());
		}
		//Catch any issues composing the URL for the request.
		catch (URISyntaxException e){
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		finally{
			return uri;
		}
	}

	protected HttpPost createHttpPost(URI uri) {
		HttpPost request = new HttpPost(uri);
		request.addHeader("accept", "application/json");
		return request;
	}

	private HttpGet createHttpGet(URI uri) {
		HttpGet request = new HttpGet(uri);
		request.addHeader("accept", "application/json");
		return request;
	}

	private String getResponsePayload(HttpResponse response) {

		String payload = ""; // To hold the full response payload.
		try {
			//Let's prepare to read the output.
			BufferedReader reader = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			//Let's read all the content from the response.
			String output; //Just to hold each line temporarily while we print it.
			while ((output = reader.readLine()) != null) { //readLine() may cause IOException
				payload += output;
			}
			LOGGER.log(Level.INFO, "Response payload: {0}", payload);
		}
		//Catch any issues reading the HTTP response.
		catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		finally {
			return payload;
		}
	}

	private <T> T parseJson(String json, Class<T> klass) {
		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		return gson.fromJson(json, klass);
	}

	private HttpResponse _executeHttpRequest(HttpRequestBase request, Boolean lax) throws GeppettoException {

		HttpClientBuilder builder = HttpClientBuilder.create();

		/*
		 * Prepare the request to handle redirection. A POST to oauth/consumer replies 302 redirectig to hit oauth/consumer again
		 * with a GET and a PHPSESSID cookie.*/
		if(lax) { builder.setRedirectStrategy(new LaxRedirectStrategy()); }

		//Let's create the client.
		HttpClient client = builder.build();

		HttpResponse response;

		try {

			//TODO: Lower this to FINE after development is done.
			LOGGER.log(Level.FINE, "Executing Geppeto request: {0}\n\theaders: {1}", new Object[]{
					request.toString(),
					ArrayUtils.toString(request.getAllHeaders())
			} );


			//Let's execute the request.
			return client.execute(request); //This throws IOException
		}
		catch(IOException e){
			LOGGER.log(Level.SEVERE, "Geppetto faced IOException while trying to execute connectors request: {0}\n\tException: {1}", new Object[]{
					request.toString(),
					e.toString()
			});
			throw new BackendUnreachableException(request);
		}
	}

	protected String executeHttpRequest(HttpRequestBase request, Boolean lax) throws GeppettoException {

		HttpResponse response = _executeHttpRequest(request, lax);

		//Let's see how it went.
		int code = response.getStatusLine().getStatusCode();

		//Let's get the payload from the response.
		String payload = getResponsePayload(response);

		LOGGER.log(Level.INFO, "Geppetto HTTP request: {0}\n\tCode: {1}\n\tStatus: {2}\n\tResponse: {3}", new Object[]{
			request.toString(),
			code,
			response.getStatusLine().getReasonPhrase(),
			payload
		});

		//Success
		if (code >= 200 && code < 300){
			//NOOP //TODO: Do I need/want to do anything here???
		}
		//Failure
		else {
			throw new GeppettoException(10000 + code, code, response.getStatusLine().getReasonPhrase(), payload);
		}
		return payload;
	}

	protected Header generateBasicAuthenticationHeader(String key, String secret){
		Base64 b = new Base64();
		String encoding = b.encodeAsString(String.format("%s:%s", key, secret).getBytes());
		String headerValue = String.format("Basic %s", encoding);
		Header header = new BasicHeader("authorization", headerValue);
		return header;
	}
}
