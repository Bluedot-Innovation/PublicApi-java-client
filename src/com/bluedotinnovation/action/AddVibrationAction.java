package com.bluedotinnovation.action;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.bluedotinnovation.common.BDCommon;

/**
 * @author Bluedot Innovation
 * Copyright (c) 2016 Bluedot Innovation. All rights reserved.
 * Add Vibration Action client demonstrates adding a Vibration action to an existing zone using JSON simple and Apache HTTP client libraries.
 */

public class AddVibrationAction extends BDCommon {

	private static String bdCustomerApiKey    = "bc199c80-5441-11e4-b7bb-a0481cdc3311"; //This key is generated by Bluedot Access UI when you register
	private static String bdApplicationApiKey = "d3161e80-38d1-11e4-b039-bc305bf60831"; //This apiKey is generated when you create an application
	private static String bdZoneId            = "24d9a245-2087-421b-9972-2af2ee0970f1"; //This is the id of the zone being updated. This can be fetched by calling GET Zones API
    private static String bdRestUrl           = "https://api.bluedotinnovation.com/1/actions";
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public static void main(String[] args) throws IOException, ParseException, KeyManagementException, NoSuchAlgorithmException {
		
		CloseableHttpClient httpRestClient = HttpClients.custom().setSSLSocketFactory(getSSLContextFactory()).build();
		
	    JSONParser parser    = new JSONParser();
	    JSONObject bdVibrationActionJSONObject = (JSONObject) parser.parse(getJsonVibrationAction()); //Vibration action json
				    		    
		HttpPost postRequest = new HttpPost(bdRestUrl);
		postRequest.addHeader("content-type", "application/json");
		postRequest.setEntity(new StringEntity(bdVibrationActionJSONObject.toJSONString(), Charset.defaultCharset()));
	 
	    HttpResponse response = httpRestClient.execute(postRequest); 
	    
        if (response.getStatusLine().getStatusCode() == 200) {
        	System.out.println("Vibration action was added to your zone successfully");
        	InputStream inputStream = response.getEntity().getContent();
        	byte[] bytes            = readStream(inputStream);
        	String resultString     = new String(bytes); //json result
        	JSONObject jsonResult   = (JSONObject)  parser.parse(resultString);
        	System.out.println(jsonResult);
        } else {
        	InputStream inputStream = response.getEntity().getContent();
        	byte[] bytes            = readStream(inputStream);
        	String resultString     = new String(bytes); //json error result
        	System.out.println(resultString);
        }			
	}
	
	private static String getJsonVibrationAction() {
		
		String vibrationActionJson =
			 "{" +
	            "\"security\": {" +
	                "\"apiKey\":" + "\"" + bdApplicationApiKey +"\"," +
	                "\"customerApiKey\":" +"\"" + bdCustomerApiKey + "\""+
	            "}," +
	            "\"content\": {" +
	                "\"zone\": {" +
	                    "\"zoneId\":"+ "\"" + bdZoneId +"\"," +
	                    "\"actions\": {" +
	                        "\"vibrationActions\": [" +
	                            "{" +
	                                "\"name\": \"A vibration action\"" +
	                            "}" +
	                        "]" +
	                    "}" +
	                "}" +
	            "}" +
	        "}";		
		return vibrationActionJson;
	}
}
