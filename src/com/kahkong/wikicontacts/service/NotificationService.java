package com.kahkong.wikicontacts.service;

import android.app.PendingIntent;
import android.content.Context;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public interface NotificationService {
	public void setNotificationManager(Context context);
	
	public int getRequestCode();
	
	public void show(String title, String content, PendingIntent pendingIntent);
	
	public void cancel();
}
