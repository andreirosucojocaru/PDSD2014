package ro.pub.systems.pdsd.lab08.example04;

import java.util.StringTokenizer;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

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
			
			final WebView contentWebView = (WebView)rootView.findViewById(R.id.contentWebView);
			contentWebView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return false;
				}
			});			
			
			Button searchButton = (Button)rootView.findViewById(R.id.searchButton);
			searchButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Thread networkingThread = new Thread(new Runnable() {
						@Override
						public void run() {
							EditText keywordEditText = (EditText)rootView.findViewById(R.id.keywordEditText);
							String keyword = keywordEditText.getText().toString();
							HttpClient httpClient = new DefaultHttpClient();
							String URL = "http://www.google.ro/search?q=";
							StringTokenizer stringTokenizer = new StringTokenizer(keyword," ");
							while(stringTokenizer.hasMoreTokens())
								URL += stringTokenizer.nextToken() + "+";
							URL = URL.substring(0, URL.length() - 1);
						    HttpGet httpGet = new HttpGet(URL);
						    Log.d("Google Search", "URL = "+URL);
						    ResponseHandler<String> responseHandler = new BasicResponseHandler();
						    final StringBuilder content = new StringBuilder();
						    try {
						    	content.append(httpClient.execute(httpGet, responseHandler));
						    } catch (Exception e) {
						    	Log.e("Google Search", e.getMessage());
						    	e.printStackTrace();
						    }
						    
							contentWebView.post(new Runnable() {
								@Override
								public void run() {
									contentWebView.loadDataWithBaseURL("http://www.google.ro", content.toString(), "text/html", "UTF-8", null);
									Log.d("Google Search", "loaded URL" + contentWebView.getUrl()+"");
								}
							});
							
						}
					});
					networkingThread.start();
					
				}
			});
			
			return rootView;
		}
	}

}
