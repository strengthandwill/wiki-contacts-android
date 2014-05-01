package com.kahkong.wikicontacts.service;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class IntentServiceImpl implements IntentService {
	private static IntentService instance;

	private Activity activity;

	private IntentServiceImpl() {	
	}

	public synchronized static IntentService getInstance() {
		if (instance==null) {
			instance = new IntentServiceImpl();
		}
		return instance;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	public void startActivity(Class<?> cls) {
		startActivityWithParcelable(cls, null, null);
	}

	public void startActivityWithParcelable(Class<?> cls, String[] name, Parcelable[] extra) {
		Intent intent = new Intent(activity, cls);
		if (extra!=null) {
			for (int i=0; i<extra.length; i++) {
				intent.putExtra(name[i], extra[i]);			
			}
		}
		activity.startActivity(intent);			
	}

	public void startService(Class<?> cls) {
		Intent intent = new Intent(activity, cls);
		activity.startService(intent);
	}

	public void stopService(Class<?> cls) {
		Intent intent = new Intent(activity, cls);
		activity.stopService(intent);
	}

	public void startCallActivity(String number, String countryCode) {
		Intent intent = new Intent(Intent.ACTION_CALL);
		Uri data = Uri.parse("tel:+" + countryCode + number);
		intent.setData(data);
		activity.startActivity(intent);
	}
	
	public void startImageChooserActivity(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(intent, requestCode);		
	}
	
	public InputStream getActivityResultInputStream(Intent data) {
		try {
			return activity.getContentResolver().openInputStream(data.getData());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}		
	}

	public void startUrlActivity(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri data = Uri.parse(url);
		intent.setData(data);
		activity.startActivity(intent);		
	}
	
	public void startEmailActivity(String email) {
		startEmailActivity(email, null);
	}

	public void startEmailActivity(String email, String subject) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		String emailLink = "mailto:" + email;
		if (subject!=null) {
			emailLink += "?subject=" + subject;
		}
		Uri data = Uri.parse(emailLink);
		intent.setData(data);
		activity.startActivity(intent);		
	}
	
	public PendingIntent getPendingIntent(Class<?> cls, int requestCode) {
		Intent intent = new Intent(activity, cls);
		return PendingIntent.getActivity(activity, requestCode, intent, 0);
	}
}
