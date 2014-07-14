package ro.pub.systems.pdsd.lab08.example01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ro.pub.systems.pdsd.example01.R;
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
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			
			final EditText URLEditText = (EditText)rootView.findViewById(R.id.URLEditText);
			final EditText keywordEditText = (EditText)rootView.findViewById(R.id.keywordEditText);
			final TextView resultsTextView = (TextView)rootView.findViewById(R.id.resultsTextView);
			Button searchButton = (Button)rootView.findViewById(R.id.searchButton);
			searchButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String urlValue = URLEditText.getText().toString();
					String keywordValue = keywordEditText.getText().toString();
					resultsTextView.setText(URLKeywordSearch(urlValue, keywordValue));
				}
			});			
			
			
			return rootView;
		}
		
		private String URLKeywordSearch(final String urlValue, final String keywordValue) {
			final StringBuilder result = new StringBuilder("");
			Thread networkAccessThread = new Thread(new Runnable() {
				@Override
				public void run() {
					HttpURLConnection httpURLConnection = null;
					try {
						URL url = new URL(urlValue);
						httpURLConnection = (HttpURLConnection)url.openConnection();
						BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(httpURLConnection.getInputStream()));
						int currentLineNumber = 0;
						String currentLineContent;
						while ((currentLineContent = bufferedReader.readLine()) != null) {
							currentLineNumber++;
							Log.d("URLKeywordSearch", "line: "+currentLineNumber+"content: "+currentLineContent);
							if (currentLineContent.contains(keywordValue)) {
								Log.d("URLKeywordSearch", "keyword "+ keywordValue+" found at line: "+currentLineNumber);
								result.append("line " + currentLineNumber + ": " + currentLineContent + "\n");
							}
						}
					} catch (MalformedURLException malformedURLException) {
						Log.e("URLKeywordSearch exception", malformedURLException.getMessage());
						malformedURLException.printStackTrace();
					} catch (IOException ioException) {
						Log.e("URLKeywordSearch exception", ioException.getMessage());
						ioException.printStackTrace();
					} finally {
						if (httpURLConnection != null)
							httpURLConnection.disconnect();
					}					
				}
			});
			networkAccessThread.start();
			try {
				networkAccessThread.join();
			} catch (InterruptedException interruptedException) {
				Log.e("URLKeywordSearch exception", interruptedException.getMessage());
				interruptedException.printStackTrace();
			}

			if (result.toString().isEmpty())
				result.append("no results found");
			return result.toString();
		}		
	}

}
