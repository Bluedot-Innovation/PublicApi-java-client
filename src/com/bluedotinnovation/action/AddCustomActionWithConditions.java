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
 * Copyright (c) 2016 Bluedot Innovation Pty Ltd. All rights reserved.
 * Add Custom Action client demonstrates adding a custom action to an existing zone using JSON simple and Apache HTTP client libraries.
 */

public class AddCustomActionWithConditions extends BDCommon {
	
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
	    JSONObject bdCustomActionJSONObject = (JSONObject) parser.parse(getJsonCustomActionWithCondition()); //Custom application action with conditions json
	    
	    HttpPost postRequest = new HttpPost(bdRestUrl);				    		    
		postRequest.addHeader("content-type", "application/json");
		postRequest.setEntity(new StringEntity(bdCustomActionJSONObject.toJSONString(), Charset.defaultCharset()));
	 
	    HttpResponse response = httpRestClient.execute(postRequest);
	    	    	    
        if (response.getStatusLine().getStatusCode() == 200) {
        	System.out.println("Custom application action was added to your zone successfully");
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
	
    /**
     * Example of Custom Action.
     * @return json String
     */
	private static String getJsonCustomActionWithCondition() {
		String customActionJson =
			 "{" +
	            "\"security\": {" +
	                "\"apiKey\":" + "\"" + bdApplicationApiKey +"\"," +
	                "\"customerApiKey\":" +"\"" + bdCustomerApiKey + "\""+
	            "}," +
	            "\"content\": {" +
	                "\"zone\": {" +
	                    "\"zoneId\":"+ "\"" + bdZoneId +"\"," +
	                    "\"actions\": {" +
	                        "\"customActions\": [" +
	                            "{" +
	                                "\"name\": \"A Custom Application action\"," +
	                                "\"conditions\": {" +
		                               "\"dateRange\": [" +
		                                   "{" +
		                                       "\"start\": \"10/11/2014\"," +
		                                       "\"end\": \"26/12/2014\"" +
		                                   "}" +
		                               "]," +
		                               "\"percentageCrossed\": [" +
		                                  "{" +
		                                      "\"percentage\": 90," +
		                                      "\"timeoutPeriod\": \"00:15\"" +
		                                   "}" +
		                              "]" +
		                           "}" +
	                            "}" +
	                        "]" +
	                    "}" +
	                "}" +
	            "}" +
	        "}";		
		return customActionJson;
	}
	
}
