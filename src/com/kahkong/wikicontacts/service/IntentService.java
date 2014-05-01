package com.kahkong.wikicontacts.service;

import java.io.InputStream;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Parcelable;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public interface IntentService {	
	public void setActivity(Activity activity);
	
	public void startActivity(Class<?> cls);
	
	public void startActivityWithParcelable(Class<?> cls, String[] name, Parcelable[] extra);
	
	public void startService(Class<?> cls);
	
	public void stopService(Class<?> cls);
	
	public void startImageChooserActivity(int requestCode);
	
	public InputStream getActivityResultInputStream(Intent data);
	
	public void startCallActivity(String number, String countryCode);
	
	public void startUrlActivity(String url);
	
	public void startEmailActivity(String email);
	
	public void startEmailActivity(String email, String subject);
	
	public PendingIntent getPendingIntent(Class<?> cls, int requestCode);
}
