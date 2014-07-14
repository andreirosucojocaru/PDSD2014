package ro.pub.systems.pdsd.lab08.example03;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
			new Thread(new Runnable() {
				@Override
				public void run() {
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet("http://www.xkcd.com");
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					String content = null;
					try {
						content = httpClient.execute(httpGet, responseHandler);				
					} catch (Exception exception) {
						Log.e("Comic Displayer", exception.getMessage());
						exception.printStackTrace();
					}
					Document document = Jsoup.parse(content);
					Element root = document.child(0);
					Elements elements = root.getElementsByAttributeValue("id", "comic");
					if (!elements.isEmpty()) {
						String comicUrl = elements.get(0).getElementsByAttribute("src").get(0).attr("src");
						HttpURLConnection httpUrlConnection = null;
						InputStream inputStream = null;
						try {
							URL url = new URL(comicUrl);
							httpUrlConnection = (HttpURLConnection)url.openConnection();
							inputStream = httpUrlConnection.getInputStream();
							final Bitmap comicBitmap = BitmapFactory.decodeStream(inputStream);
							final ImageView comicImageView = (ImageView)rootView.findViewById(R.id.comicImageView);
							comicImageView.post(new Runnable() {
								@Override
								public void run() {
									comicImageView.setImageBitmap(comicBitmap);
								}
							});
						} catch (Exception exception) {
							Log.e("Comic Displayer", exception.getMessage());
							exception.printStackTrace();
						} finally {
							if (inputStream != null)
								try {
									inputStream.close();
								} catch (Exception exception) {
									Log.e("Comic Displayer", exception.getMessage());
									exception.printStackTrace();
								}
							if (httpUrlConnection != null)
								try {
									httpUrlConnection.disconnect();
								} catch (Exception exception) {
									Log.e("Comic Displayer", exception.getMessage());
									exception.printStackTrace();
								}
						}
					}
				}
			}).start();

			return rootView;
		}
	}

}
