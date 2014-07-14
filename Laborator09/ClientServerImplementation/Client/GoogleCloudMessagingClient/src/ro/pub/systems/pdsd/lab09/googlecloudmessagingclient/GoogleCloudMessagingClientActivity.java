package ro.pub.systems.pdsd.lab09.googlecloudmessagingclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GoogleCloudMessagingClientActivity extends Activity {

	final private static String TAG 										= "GoogleCloudMessagingActivity";

	private static EditText 					usernameEditText;
	private static EditText 					emailEditText;
	private static TextView 					registrationIdTextViewValue;
	private static Button 						sendButton;

	static GoogleCloudMessaging 				googleCloudMessaging;
	static String 								registrationId;

	static Context 								context;

	static GoogleCloudMessagingClientActivity	activity;
	static GoogleCloudMessagingClientFragment 	fragment 					= new GoogleCloudMessagingClientFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_cloud_messaging);
		context = getApplicationContext();
		activity = this;

		// check whether the device has Google Play Services APK installed

		if (checkPlayServices()) {
			// the check is successful

			googleCloudMessaging = GoogleCloudMessaging.getInstance(this);

			// get the Google Cloud Messaging registration Id, if it was provided previously
			// and stored into SharedPreferences
			registrationId = getRegistrationId(context);

			if (registrationId.isEmpty()) {
				// the Google Cloud Messaging registration Id was not provided previously
				// connect to the Google Cloud Messaging server in background and register
				registerInBackground();   
			}

			if (getApplicationServerRegistrationStatus(context) == Configuration.FAILURE) {
				fragment.setType(Configuration.UNREGISTERED_FRAGMENT_TYPE);
			} else {
				fragment.setType(Configuration.REGISTERED_FRAGMENT_TYPE);
			}

			if (savedInstanceState == null) {
				// load the graphic user interface
				getFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
			}

		} else {
			// the check is not successful
			Log.i(TAG, "Google Play Services APK was not found on the mobile device.");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// check whether the device has Google Play Services APK has been installed meanwhile
		checkPlayServices();
	}

	/**
	 * Check the mobile device to verify whether it has the Google Play Services APK installed. 
	 * If the application is not installed, display a dialog that allows users to download it
	 * from the Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, Configuration.PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This mobile device does not support Google Play Services.");
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Gets the registration ID previously provided to the application by the Google Cloud Messaging Server
	 * and stored into the SharedPreferences.
	 * 
	 * @return registrationId, or empty string if no registrationId was previously supplied
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences sharedPreferences = getSharedPreferences(context);
		String registrationId = sharedPreferences.getString(Configuration.REGISTRATION_ID_PROPERTY, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration ID not found into the SharedPreferences.");
			return "";
		}
		// check whether the application was updated 
		// if it is the case, the registration ID previously supplied must be cleared
		// as it is not guaranteed to work with the current version
		int registeredVersion = sharedPreferences.getInt(Configuration.APPLICATION_VERSION_PROPERTY, Integer.MIN_VALUE);
		int currentVersion = getApplicationVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "The application version was changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * Gets the application server registration status if it was previously stored 
	 * into the SharedPreferences.
	 * 
	 * @return applicationServerRegistrationStatus (0 - registered, -1 unregistered or application version changed)
	 */
	private int getApplicationServerRegistrationStatus(Context context) {
		final SharedPreferences sharedPreferences = getSharedPreferences(context);
		int applicationServerRegistrationStatus = sharedPreferences.getInt(Configuration.APPLICATION_SERVER_REGISTRATION_STATUS, Integer.MIN_VALUE);
		if (applicationServerRegistrationStatus == Integer.MIN_VALUE) {
			Log.i(TAG, "Application Server Registration Status not found into the SharedPreferences.");
			return -1;
		}
		// check whether the application was updated 
		// if it is the case, the application server registration status must be cleared
		// as the mobile device needs to submit the new registration ID
		int registeredVersion = sharedPreferences.getInt(Configuration.APPLICATION_VERSION_PROPERTY, Integer.MIN_VALUE);
		int currentVersion = getApplicationVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "The application version was changed.");
			return -1;
		}
		return applicationServerRegistrationStatus;
	}	

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getApplicationVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException exception) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + exception);
		}
	}	

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getSharedPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(GoogleCloudMessagingClientActivity.class.getSimpleName(), Context.MODE_PRIVATE);
	}

	/**
	 * Stores the registration ID and application server registration status into the application's
	 * {@code SharedPreferences}.
	 *
	 * @param registrationId - registration ID supplied by the Google Cloud Messaging Server
	 * @param applicationServerRegistrationStatus - status of registering the ID to the application server
	 */
	private void setSharedPreferences(Context context, String registrationId, int applicationServerRegistrationStatus) {
		final SharedPreferences sharedPreferences = getSharedPreferences(context);
		int currentVersion = getApplicationVersion(context);
		Log.i(TAG, "Setting the registrationId and applicationServerRegistrationStatus on application version " + currentVersion);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(Configuration.REGISTRATION_ID_PROPERTY, registrationId);
		editor.putInt(Configuration.APPLICATION_SERVER_REGISTRATION_STATUS, applicationServerRegistrationStatus);
		editor.putInt(Configuration.APPLICATION_VERSION_PROPERTY, currentVersion);
		editor.commit();
	}	

	/**
	 * Registers the application with Google Cloud Messaging server asynchronously
	 */
	private void registerInBackground() {
		new AsyncTask<Object, Integer, String>() {
			@Override
			protected String doInBackground(Object... params) {
				try {
					if (googleCloudMessaging == null) {
						googleCloudMessaging = GoogleCloudMessaging.getInstance(context);
					}
					registrationId = googleCloudMessaging.register(Configuration.PROJECT_NUMBER);

					// store the registration ID into the SharedPreferences
					// the ID is not yet registered with the application server
					setSharedPreferences(context, registrationId, Configuration.FAILURE);
				} catch (IOException exception) {
					registrationId = "exceptie:" + exception.getMessage();
					exception.printStackTrace();
				}
				return registrationId;
			}

			@Override
			protected void onPostExecute(String registrationId) {
				registrationIdTextViewValue.append(registrationId + "\n");
				Log.i(TAG, "registration ID supplied by the Google Cloud Messaging Server is "+registrationId);
			}
		}.execute(null, null, null);
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.google_cloud_messaging_client, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static class GoogleCloudMessagingClientFragment extends Fragment {

		// the type determines the graphic user interface displayed to the user
		// Configuration.REGISTERED_FRAGMENT_TYPE (1) allows the user to see the registration ID
		// Configuration.UNREGISTERED_FRAGMENT_TYPE (2) allow the user to register the ID with the application server
		private int type = 0;

		public GoogleCloudMessagingClientFragment() {
		}

		public void setType(int type) {
			this.type = type;
		}

		public static class RegisterButtonListener implements OnClickListener {
			@Override
			public void onClick(View v) {
				new AsyncTask<Object, Integer, Integer>() {
					@Override
					protected Integer doInBackground(Object... params) {

						try {
							HttpClient httpClient = new DefaultHttpClient();        

							HttpPost httpPost = new HttpPost(Configuration.GOOGLECLOUDMESSAGING_WEBSERVER_URL);

							// send the username, email and registration ID as fields of the HTTP body
							// to the application server
							List<NameValuePair> requestParameters = new ArrayList<NameValuePair>();        
							requestParameters.add(new BasicNameValuePair(Configuration.USER_NAME, usernameEditText.getText().toString()));
							requestParameters.add(new BasicNameValuePair(Configuration.EMAIL, emailEditText.getText().toString()));
							requestParameters.add(new BasicNameValuePair(Configuration.REGISTRATION_ID, registrationIdTextViewValue.getText().toString()));
							UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(requestParameters, HTTP.UTF_8);
							httpPost.setEntity(urlEncodedFormEntity);

							HttpResponse httpPostResponse = httpClient.execute(httpPost); 

							// transmission of information to the application server was successful
							if (httpPostResponse.getStatusLine().getStatusCode() == 200)
								return Configuration.SUCCESS;
						} catch (Exception exception) {
							Log.e(TAG, exception.getMessage());
							exception.printStackTrace();
						}
						// transmission of information to the application server was not successful
						return Configuration.FAILURE;
					}

					@Override
					protected void onPostExecute(Integer result) {
						switch(result.intValue()) {
							case Configuration.SUCCESS:
								// store the registration ID into the SharedPreferences
								// the ID is registered with the application server
								activity.setSharedPreferences(context, registrationId, Configuration.SUCCESS);
	
								// replace the existing fragment
								// in order to forbid the user to register its ID to the registration server
								fragment = new GoogleCloudMessagingClientFragment();
								fragment.setType(Configuration.REGISTERED_FRAGMENT_TYPE);
								activity.getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
								Log.i(TAG, "SUCCESS");
								break;
							case Configuration.FAILURE:
								Log.i(TAG, "FAILURE");
								break;
						}
					}
				}.execute(null, null, null);					
			}			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = null;

			switch(type) {
			case Configuration.REGISTERED_FRAGMENT_TYPE:
				rootView = inflater.inflate(
						R.layout.fragment_google_cloud_messaging_registered, container,
						false);					
				break;
			case Configuration.UNREGISTERED_FRAGMENT_TYPE:
				rootView = inflater.inflate(
						R.layout.fragment_google_cloud_messaging_unregistered, container,
						false);
				usernameEditText = (EditText)rootView.findViewById(R.id.usernameEditText);
				emailEditText = (EditText)rootView.findViewById(R.id.emailEditText);
				sendButton = (Button)rootView.findViewById(R.id.sendButton);
				sendButton.setOnClickListener(new RegisterButtonListener());
				break;
			}

			if (rootView != null)
				registrationIdTextViewValue = (TextView)rootView.findViewById(R.id.registrationIdTextViewValue);
			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			registrationIdTextViewValue.setText(registrationId);
			Log.i(TAG, "registration ID supplied by the Google Cloud Messaging Server is "+registrationId);
		}
	}

}
