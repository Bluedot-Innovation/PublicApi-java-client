/**
 * 
 */
package com.bluedotinnovation.zone;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.bluedotinnovation.common.BDCommon;

/**
 * @author Bluedot Innovation
 * Update zone client demonstrates updating fields of a zone
 * 
 */

public class UpdateZone extends BDCommon 
{
    private static String bdApplicationApiKey = "5a9b1b78-3dd7-4f4d-8608-82408f3baf4c"; //This apiKey is generated when you create an application
    private static String bdCustomerApiKey    = "a6598740-75f5-11e4-86ca-a0481cdc3311";   //This key is generated by Bluedot Access UI when you register
    private static String bdRestUrl           = "https://api.bluedotinnovation.com/1/zones";
    private static String bdZoneId            = "d6c3b688-cf2e-4aac-b76b-b29f371f448e";
    
	public static void main(String[] args) throws ParseException, IOException, KeyManagementException, NoSuchAlgorithmException
	{
		CloseableHttpClient httpRestClient = HttpClients.custom().setSSLSocketFactory(new SSLSocketFactory(getSSLContext())).build();
		
		HttpPost postRequest = new HttpPost(bdRestUrl);
  
	    JSONParser parser    = new JSONParser();
	    JSONObject bdZoneJSONObject = (JSONObject) parser.parse(getJsonZone()); //zone json
				    		    
		postRequest.addHeader("content-type", "application/json");
		postRequest.setEntity(new StringEntity(bdZoneJSONObject.toJSONString(), Charset.defaultCharset()));
	    HttpResponse response = httpRestClient.execute(postRequest);
	    	    	    
        if (response.getStatusLine().getStatusCode() == 200)
        {
        	System.out.println("Zone was updated successfully");
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
	               "\"zoneId\":" + "\"" + bdZoneId + "\"" +
	               "\"zoneName\": \"First Test Bluedot Zone-Updated\"," +
	               "\"minimumRetriggerTime\": \"11:11\"," +
	               "\"enableCheckout\": true," +
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
	   "}";
	}
}
