package com.bluedotinnovation.application;

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
 * Update Application with Webhook client demonstrates updating existing an application which has webhooks enabled for it to your Bluedot backend 
 * using Apache HTTP client and JSON Simple libraries.
 */
public class UpdateApplicationWithWebhook extends BDCommon {
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public static void main(String[] args) throws IOException, ParseException, KeyManagementException, NoSuchAlgorithmException {
		
		String bdCustomerApiKey    = "86577370-7b91-11e4-bcb7-a0481cdc3311"; //This key is generated by Bluedot Access UI when you register
		String bdApplicationId     = "564ecdc0-7e86-11e4-95ff-a0481cdba490"; //This ID is retrieved through the GET Applications call
		String bdRestUrl = "https://api.bluedotinnovation.com/1/applications";
		
		CloseableHttpClient httpRestClient = HttpClients.custom().setSSLSocketFactory(getSSLContextFactory()).build();
		
		String applicationWithWebhook =
				"{" +
					"\"security\": {" +
	                     "\"customerApiKey\":" + "\"" + bdCustomerApiKey + "\"" +
	                "}," +
	                "\"content\": { " +
	               		"\"application\" : {" +
	               			/*This is the id of the application as opposed to the api key. This is returned when the application/getAll is called*/
	               			"\"applicationId\":" + "\"" + bdApplicationId + "\"," +
	               			/*Time in Hour:Minute format.*/
	                        "\"nextRuleUpdateIntervalFormatted\": \"00:10\"," +
	                        "\"webhook\": {" +
	                        	/*The URL of the server where the webhooks will be received.*/
	 							"\"url\": \"https://api.campusapp.com/webhook/checkinreceiver\"," +
	 							"\"enabled\" : true," + 
	 							/*The Security Token Key is the name of the field to be sent in the POST request header.*/
	 							"\"securityTokenKey\" : \"authToken\"," + 
	 							/*The Security Token Value field is value of the Security Token Key field sent in the POST request header.*/
	 							"\"securityTokenValue\" : \"f2f7a58c-f0d5-498c-9bad-acbc89923dc5\"" + 
	 						"}"+
	                     "}" +
	                "}" +
	            "}";
		
	    JSONObject bdApplicationJSONObject;	    
	    JSONParser parser       = new JSONParser();
	    bdApplicationJSONObject = (JSONObject)  parser.parse(applicationWithWebhook);

		HttpPost postRequest = new HttpPost(bdRestUrl);
		postRequest.addHeader("content-type", "application/json");
		postRequest.setEntity(new StringEntity(bdApplicationJSONObject.toJSONString(), Charset.defaultCharset()));
	 
	    HttpResponse response = httpRestClient.execute(postRequest);
        if (response.getStatusLine().getStatusCode() == 200) {
        	System.out.println("Application was successfully updated.");
        	InputStream inputStream = response.getEntity().getContent();
        	byte[] bytes            = readStream(inputStream);
        	String resultString     = new String(bytes); //json result
        	JSONObject jsonResult   = (JSONObject)  parser.parse(resultString);
        	System.out.println("apiKey for your application is : " + jsonResult.get("applicationId"));
        } else {
        	InputStream inputStream = response.getEntity().getContent();
        	byte[] bytes            = readStream(inputStream);
        	String resultString     = new String(bytes); //json error result
        	System.out.println(resultString);
        }			
	}
}
