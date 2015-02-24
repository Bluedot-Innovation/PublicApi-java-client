/**
 * 
 */
package com.bluedotinnovation.action;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.bluedotinnovation.common.BDCommon;

/**
 * @author Bluedot Innovation
 * Update MessageAction REST client demonstrates updating a sound action to an existing zone using JSON simple and Apache HTTP client libraries
 */
public class UpdateSoundAction extends BDCommon
{

	private static String bdCustomerApiKey    = "bc199c80-5441-11e4-b7bb-a0481cdc3311"; //This key is generated by Bluedot Access UI when you register
	private static String bdApplicationApiKey = "d3161e80-38d1-11e4-b039-bc305bf60831"; //This apiKey is generated when you create an application
    private static String bdRestUrl           = "https://api.bluedotinnovation.com/1/actions";
	private static String bdZoneId            = "24d9a245-2087-421b-9972-2af2ee0970f1"; //This is the id of the zone being updated. This can be fetched by calling zones/getAll API
	private static String actionId            = "6de96865-f3c8-42cf-bdc2-38027272770a"; //This is the id of the action being updated. This can be fetch by calling zones/get?id=yourzoneid
	
	public static void main(String[] args) throws ParseException,  IOException
	{
        CloseableHttpClient httpRestClient  = HttpClientBuilder.create().build();
		
		HttpPost postRequest = new HttpPost(bdRestUrl);
  
	    JSONParser parser    = new JSONParser();
	    JSONObject bdSoundActionJSONObject = (JSONObject) parser.parse(getJsonSoundAction()); //Sound action json
				    		    
		postRequest.addHeader("content-type", "application/json");

		postRequest.setEntity(new StringEntity(bdSoundActionJSONObject.toJSONString()));
	 
	    HttpResponse response = httpRestClient.execute(postRequest);
	    	    	    
        if (response.getStatusLine().getStatusCode() == 200)
        {
        	System.out.println("Message action was added to your zone successfully");
        	InputStream inputStream = response.getEntity().getContent();
        	byte[] bytes            = readStream(inputStream);
        	String resultString     = new String(bytes); //json result
        	JSONObject jsonResult   = (JSONObject)  parser.parse(resultString);
        	System.out.println(jsonResult);
        }
        else 
        {
        	InputStream inputStream = response.getEntity().getContent();
        	byte[] bytes            = readStream(inputStream);
        	String resultString     = new String(bytes); //json error result
        	System.out.println(resultString);
        }			
	}
	private static String getJsonSoundAction()
	{
		String soundActionJson =
			 "{" +
	            "\"security\": {" +
	                "\"apiKey\":" + "\"" + bdApplicationApiKey +"\"," +
	                "\"customerApiKey\":" +"\"" + bdCustomerApiKey + "\""+
	            "}," +
	            "\"content\": {" +
	                "\"zone\": {" +
	                    "\"zoneId\":"+ "\"" + bdZoneId +"\"," +
	                    "\"actions\": {" +
	                        "\"soundActions\": [" +
	                            "{" +
	                                "\"actionId\":"+ "\"" + actionId +"\"," +
	                                "\"name\": \"A Sound action updated\"" +
	                            "}" +
	                        "]" +
	                    "}" +
	                "}" +
	            "}" +
	        "}";		
		return soundActionJson;
	}
}
