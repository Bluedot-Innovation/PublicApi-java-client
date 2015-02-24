/**
 * 
 */
package com.bluedotinnovation.zone;

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
 * Add Zone client demonstrates adding a zone to an existing application
 */

public class CreateZone extends BDCommon
{

    private static String bdApplicationApiKey = "d3161e80-38d1-11e4-b039-bc305bf60831"; //This apiKey is generated when you create an application
    private static String bdCustomerApiKey    = "bc199c80-5441-11e4-b7bb-a0481cdc3311"; //This key is generated by Bluedot Access UI when you register
    private static String bdRestUrl           = "https://api.bluedotinnovation.com/1/zones";
    
	public static void main(String[] args) throws ParseException, IOException
	{
		CloseableHttpClient httpRestClient  = HttpClientBuilder.create().build();
		
		HttpPost postRequest        = new HttpPost(bdRestUrl);
  
	    JSONParser parser           = new JSONParser();
	    JSONObject bdZoneJSONObject = (JSONObject) parser.parse(getJsonZone()); //zone json
				    		    
		postRequest.addHeader("content-type", "application/json");

		postRequest.setEntity(new StringEntity(bdZoneJSONObject.toJSONString()));
	 
	    HttpResponse response = httpRestClient.execute(postRequest);
	    	    	    
        if (response.getStatusLine().getStatusCode() == 200)
        {
        	System.out.println("Zone has been created successfully");
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
	
	
	 /*Return JSON for a Zone. Time values have a format of HH:MM and the period value has to be one of {am/pm}*/
    private static String getJsonZone()
    {
        return "{" +
         "\"security\": {" +
                    "\"apiKey\":" + "\"" + bdApplicationApiKey + "\"," +
                    "\"customerApiKey\":" + "\"" + bdCustomerApiKey + "\"" +
        "}," +
       "\"content\": {" +
             "\"zone\": {" +
                "\"zoneName\": \"A test Bluedot Zone\"," +
                "\"minimumRetriggerTime\": \"11:11\"," +
                "\"timeActive\" : {" +
                    "\"from\": {" +
                        "\"time\": \"06:00\"," +
                        "\"period\": \"am\"" +
                    "}," +
                    "\"to\": {" +
                        "\"time\": \"11:00\"," +
                        "\"period\": \"pm\"" +
                    "}" +
                "}" +
            "}" +
        "}" +
    "};";
   }
    
	

}
