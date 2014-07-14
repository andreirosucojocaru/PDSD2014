package ro.pub.systems.pdsd.lab09.googlecloudmessagingclient;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GoogleCloudMessagingIntentService extends IntentService {

	private final static String TAG 								= "GoogleCloudMessagingIntentService";

	private NotificationManager notificationManager;

	public GoogleCloudMessagingIntentService() {
		super("GoogleCloudMessagingIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// the intent is supplied via the BroadcastReceiver

		GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(this);

		String messageType = googleCloudMessaging.getMessageType(intent);

		Bundle extras = intent.getExtras();
		if (!extras.isEmpty()) {
			// filter the message according to its type
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification("Deleted messages on server: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				for (int counter = 0; counter < Configuration.NUMBER_OF_STEPS; counter++) {
					Log.i(TAG, "Doing some work, step " + (counter+1) + "/ "+ Configuration.NUMBER_OF_STEPS+" @ " + SystemClock.elapsedRealtime());
					try {
						Thread.sleep(Configuration.STEP_DURATION);
					} catch (InterruptedException exception) {
						System.out.println("exception: "+exception.getMessage());
						exception.printStackTrace();
					}
				}
				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
				// send the notification with the received message
				sendNotification("Received: " + extras.toString());
				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		// release the wake lock provided by the WakefulBroadcastReceiver.
		GoogleCloudMessagingBroadcastReceiver.completeWakefulIntent(intent);
	}

	// create a notification containing the message and post it
	private void sendNotification(String message) {
		notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(
				this, 
				0,
				new Intent(this, GoogleCloudMessagingClientActivity.class), 
				0);

		NotificationCompat.Builder builder =
				new NotificationCompat.Builder(this)
					.setSmallIcon(R.drawable.googlecloudmessaging)
					.setContentTitle(TAG)
					.setStyle(new NotificationCompat.BigTextStyle()
					.bigText(message))
					.setContentText(message);

		builder.setContentIntent(contentIntent);
		notificationManager.notify(Configuration.NOTIFICATION_ID, builder.build());
	}
}
