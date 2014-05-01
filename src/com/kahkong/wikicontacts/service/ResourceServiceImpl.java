package com.kahkong.wikicontacts.service;

import com.kahkong.wikicontacts.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class ResourceServiceImpl implements ResourceService {
	private static ResourceService instance;
	
	private Context context;
	
	private ResourceServiceImpl() {
	}

	public synchronized static ResourceService getInstance() {
		if (instance==null) {
			instance = new ResourceServiceImpl();
		}
		return instance;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public Bitmap getNewImage() {
		return BitmapFactory.decodeResource(context.getResources(), R.drawable.contact_new);
	}
	
	public Bitmap getSpamImage() {
		return BitmapFactory.decodeResource(context.getResources(), R.drawable.contact_spam);
	}
	
	public String getString(int id) {
		return context.getResources().getString(id);
	}
}
