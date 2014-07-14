package ro.pub.systems.pdsd.lab08.example02;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			

			Button computeButton = (Button)rootView.findViewById(R.id.computeButton);
			computeButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Spinner methodSpinner = (Spinner)rootView.findViewById(R.id.methodSpinner);
					EditText operand1EditText = (EditText)rootView.findViewById(R.id.operand1EditText);
					Spinner operationSpinner = (Spinner)rootView.findViewById(R.id.operationSpinner);
					EditText operand2EditText = (EditText)rootView.findViewById(R.id.operand2EditText);
					TextView contentTextView = (TextView)rootView.findViewById(R.id.contentTextView);
					contentTextView.setText(Double.valueOf(compute(methodSpinner.getSelectedItem().toString(),operationSpinner.getSelectedItem().toString(),Double.parseDouble(operand1EditText.getText().toString()),Double.parseDouble(operand2EditText.getText().toString()))).toString());
					Log.d("Web Calculator", methodSpinner.getSelectedItem().toString()+" "+operand1EditText.getText().toString()+" "+operationSpinner.getSelectedItem().toString()+" "+operand2EditText.getText().toString());
					
				}
			});
			
			return rootView;
		}
		
		public double compute(String method, String operation, double operand1, double operand2) {
			if (method.equals("GET")) {
				String URL = "http://wifi.elcom.pub.ro/pdsd/expr_get.php?";
				URL += "op=";
				if (operation.equals("+"))
					URL += "plus";
				else if (operation.equals("-"))
					URL += "minus";
				else if (operation.equals("*"))
					URL += "times";
				else if (operation.equals("/"))
					URL += "divide";
				URL += "&t1=" + operand1;
				URL += "&t2=" + operand2;
				HttpGet httpGet = new HttpGet(URL);
				Log.d("Web Calculator", "URL = " + URL);
				return compute(httpGet);
			} else if (method.equals("POST")) {
				String URL = "http://wifi.elcom.pub.ro/pdsd/expr_post.php";
				HttpPost httpPost = new HttpPost(URL);	
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				if (operation.equals("+"))
					params.add(new BasicNameValuePair("op", "plus"));
				else if (operation.equals("-"))
					params.add(new BasicNameValuePair("op", "minus"));
				else if (operation.equals("*"))
					params.add(new BasicNameValuePair("op", "times"));
				else if (operation.equals("/"))
					params.add(new BasicNameValuePair("op", "divide"));
				params.add(new BasicNameValuePair("t1", Double.valueOf(operand1).toString()));
				params.add(new BasicNameValuePair("t2", Double.valueOf(operand2).toString()));	
				UrlEncodedFormEntity urlEncodedFormEntity = null;
				try {
					urlEncodedFormEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				httpPost.setEntity(urlEncodedFormEntity);
				Log.d("Web Calculator", "URL = " + URL);
				return compute(httpPost);
			}
			
			return -1;
		}
		
		public double compute (final HttpGet httpGet) {
			final StringBuilder content = new StringBuilder();
			Thread computeThread = new Thread(new Runnable() {
				@Override
				public void run() {
					HttpClient httpClient = new DefaultHttpClient();
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					try {
						content.append(httpClient.execute(httpGet, responseHandler));
					} catch (ClientProtocolException e) {
						Log.e("Web Calculator", e.getMessage());
						e.printStackTrace();
					} catch (IOException e) {
						Log.e("Web Calculator", e.getMessage());
						e.printStackTrace();
					}					
				}
			});
			computeThread.start();
			try {
				computeThread.join();
			} catch (Exception exception) {
				Log.e("Web Calculator", exception.getMessage());
				exception.printStackTrace();
			}
			Log.d("Web Calculator", content.toString());
			return Double.parseDouble(content.toString());
		}
		
		public double compute (final HttpPost httpPost) {
			final StringBuilder content = new StringBuilder();
			Thread computeThread = new Thread(new Runnable() {
				@Override
				public void run() {
					HttpClient httpClient = new DefaultHttpClient();
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					try {
						content.append(httpClient.execute(httpPost, responseHandler));
					} catch (ClientProtocolException e) {
						Log.e("Web Calculator", e.getMessage());
						e.printStackTrace();
					} catch (IOException e) {
						Log.e("Web Calculator", e.getMessage());
						e.printStackTrace();
					}					
				}
			});
			computeThread.start();
			try {
				computeThread.join();
			} catch (Exception exception) {
				Log.e("Web Calculator", exception.getMessage());
				exception.printStackTrace();
			}
			Log.d("Web Calculator", content.toString());
			return Double.parseDouble(content.toString());			
		}		
	}

}
