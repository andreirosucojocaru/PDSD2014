package ro.pub.systems.pdsd.lab09.googlecloudmessaginghttpserverwithjson;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class GoogleCloudMessagingHTTPServerWithJson {
 
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
 
		HttpClient client = HttpClientBuilder.create().build();
 
	    HttpPost request = new HttpPost(Configuration.REQUEST_SERVER_URL);
	    HttpResponse response;
 
	    /**
	     * HTTP headers
	     * 
	     * Authorization: key=API key provided from Google Developer Console
	     * Content-Type: the format of the HTTP body (may be omitted for plain text)
	     *   - application/json for JSON
	     *   - application/x-www-form-urlencoded;charset=UTF-8 for plain text
	     *
	     */
	    
	    request.setHeader("Authorization", "key=" + Configuration.API_KEY);
	    request.setHeader("Content-Type", "application/json");
 
	    /**
	     * HTTP body
	     * 
	     * -> registration_id/registration_ids indicates the recipient/recipients
	     * (for each mobile device to which the message is to be transmitted the message must specify
	     * the id provided by the GCM server during the registration process)
	     * 
		 * -> data contains the payload to be transmitted; the developer may define its own keys
		 * 
		 * -> other fields: collapse_key, delay_while_idle, time_to_live, restricted_package_name, dry_run
	     * 
	     * The complete list of the message parameters can be found out at: 
	     * http://developer.android.com/google/gcm/server.html#params
	     */
	    
	    JSONObject requestBody = new JSONObject();
	    JSONArray registrationIds = new JSONArray();
	    registrationIds.add(Configuration.GCM_REGISTRATION_ID);
		requestBody.put("registration_ids", registrationIds);
		JSONObject payload = new JSONObject();
		payload.put("greeting", "Hello world");
		requestBody.put("data", payload);
 
	    try {
	    	request.setEntity(new StringEntity(JSONValue.toJSONString(requestBody)));
	        
	        // send the request
	        client.execute(request);
 
	        // receive the response
	        response = client.execute(request);
 
	         // process the response
	         if(response != null) {
	        	 
	        	 // HTTP Status Code
	        	 int responseCode = response.getStatusLine().getStatusCode();
	        	 System.out.println("HTTP Status Code: " + Integer.toString(responseCode));
	        	 
	        	 // HTTP Body
	             InputStream in = response.getEntity().getContent();
	             System.out.println("HTTP Body: " + IOUtils.toString(in));
	             
	             // display the HTTP full response
	             System.out.println("HTTP full response: " + response.toString());
	         } 
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}