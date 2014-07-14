import general.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import dataaccess.DataBaseConnection;

public class GoogleCloudMessagingPushNotificationServlet extends HttpServlet {
	final public static long    serialVersionUID = 2048L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			DataBaseConnection.openConnection();
		} catch (SQLException exception) {
			System.out.println("exceptie: "+exception.getMessage());
			if (Constants.DEBUG)
				exception.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		try {
			DataBaseConnection.closeConnection();
		} catch (SQLException exception) {
			System.out.println("exceptie: "+exception.getMessage());
			if (Constants.DEBUG)
				exception.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Enumeration<String> parameters = request.getParameterNames();
		boolean found = false;
		int id = -1;
		String registrationId = null;
		String message = null;
		while (parameters.hasMoreElements() && !found) {
			String parameter = (String)parameters.nextElement();
			if (parameter.contains(Constants.SEND_MESSAGE)) {
				found = true;
				id = Integer.parseInt(parameter.substring(parameter.indexOf("#")+1));
				registrationId = request.getParameter(Constants.REGISTRATION_ID+"#"+id);
				message = request.getParameter(Constants.MESSAGE_TEXT+"#"+id);
			}
		}
		if (found) {
			HttpClient client = HttpClientBuilder.create().build();

			HttpPost httpRequest = new HttpPost(Constants.REQUEST_SERVER_URL);
			HttpResponse httpResponse;

			/**
			 * HTTP headers
			 * 
			 * Authorization: key=API key provided from Google Developer Console
			 * Content-Type: the format of the HTTP body (may be omitted for plain text)
			 *   - application/json for JSON
			 *   - application/x-www-form-urlencoded;charset=UTF-8 for plain text
			 *
			 */

			httpRequest.setHeader("Authorization", "key=" + Constants.API_KEY);
			httpRequest.setHeader("Content-Type", "application/json");

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
			registrationIds.add(registrationId);
			requestBody.put(Constants.REGISTRATION_IDS, registrationIds);
			JSONObject payload = new JSONObject();
			payload.put(Constants.MESSAGE, message);
			requestBody.put(Constants.DATA, payload);

			try {
				httpRequest.setEntity(new StringEntity(JSONValue.toJSONString(requestBody)));

				// send the request
				httpResponse = client.execute(httpRequest);

				// receive the response
				if (httpResponse != null) {

					// HTTP Response Status Code
					int responseCode = httpResponse.getStatusLine().getStatusCode();
					if (Constants.DEBUG)
						System.out.println("HTTP Status Code: " + Integer.toString(responseCode));

					// HTTP Response Body
					InputStream in = httpResponse.getEntity().getContent();
					if (Constants.DEBUG)
						System.out.println("HTTP Body: " + IOUtils.toString(in));

					// display the HTTP Response
					if (Constants.DEBUG)
						System.out.println("HTTP Response: " + httpResponse.toString());
				} 
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}        

		RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/GoogleCloudMessagingRegisterServlet");
		if (requestDispatcher!=null) {
			requestDispatcher.forward(request, response);
		}

	}     	 
}
