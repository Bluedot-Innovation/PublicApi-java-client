/**
 * 
 */
package com.bluedotinnovation.zone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.bluedotinnovation.common.BDCommon;

/**
 * @author Bluedot Innovation
 * Get zones client demonstrates the listing of zone names and ids for a given customer
 */
public class GetAllZones extends BDCommon
{

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public static void main(String[] args) throws IOException, ParseException, KeyManagementException, NoSuchAlgorithmException
	{
		String bdCustomerApiKey    = "fab77cd0-c094-11e5-a1fa-06a56cd124c5"; //This key is generated by Bluedot Access UI when you register
		String bdApplicationApiKey    = "4116edf0-c095-11e5-a1fa-06a56cd124c5"; //This apiKey is generated when you create an application
		String bdRestUrl           = "https://api.bluedotinnovation.com/1/zones?customerApiKey=" +bdCustomerApiKey + "&apiKey=" + bdApplicationApiKey + "&pageNumber=0"; 
		
		CloseableHttpClient httpRestClient = HttpClients.custom().setSSLSocketFactory(new SSLSocketFactory(getSSLContext())).build();
		HttpGet request            = new HttpGet(bdRestUrl);
		HttpResponse httpResponse  = httpRestClient.execute(request);
		
		if (httpResponse.getStatusLine().getStatusCode() == 200){
			BufferedReader rd          = new BufferedReader (new InputStreamReader
                    (httpResponse.getEntity().getContent()));
			JSONParser parser          = new JSONParser();
			String bdZonesJson;
			System.out.println(rd.readLine());
			while ((bdZonesJson = rd.readLine()) != null) 
			{
			     Object object                    = parser.parse(bdZonesJson);
			     JSONArray bdZonesJsonArry        = (JSONArray) object;
			     JSONArray jsonObjectArray        = (JSONArray) bdZonesJsonArry.get(0);
			     JSONObject applicationObject     = (JSONObject) jsonObjectArray.get(0);
			     /*Zone name and id of the first zone from the array returned*/
			     System.out.println("Zone name : " + applicationObject.get("name") + " Zone id: " + applicationObject.get("_id"));
			}
		}
		else{
			InputStream inputStream = httpResponse.getEntity().getContent();
        	byte[] bytes            = readStream(inputStream);
        	String resultString     = new String(bytes); //json error result
        	System.out.println(resultString);
		}

	}

}
