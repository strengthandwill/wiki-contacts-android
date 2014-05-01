package com.kahkong.wikicontacts.service;

import com.kahkong.wikicontacts.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class NotificationServiceImpl implements NotificationService {
	private static NotificationService instance;
	
	private final int REQUEST_CODE = (int) (Math.random()*Integer.MAX_VALUE);
	
	private Context context;
	private NotificationManager notificationManager;
	
	private NotificationServiceImpl() {	
	}

	public synchronized static NotificationService getInstance() {
		if (instance==null) {
			instance = new NotificationServiceImpl();
		}
		return instance;
	}
	
	public int getRequestCode() {
		return REQUEST_CODE;
	}
	
	public void setNotificationManager(Context context) {
		this.context = context;
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
	}
	
	public void show(String title, String content, PendingIntent pendingIntent) {
		notificationManager.cancelAll();
		Notification notification = new NotificationCompat.Builder(context)
		        .setContentTitle(title)
		        .setContentText(content)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentIntent(pendingIntent).build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(REQUEST_CODE, notification); 
	}
	
	public void cancel() {
		notificationManager.cancelAll();
	}
}
