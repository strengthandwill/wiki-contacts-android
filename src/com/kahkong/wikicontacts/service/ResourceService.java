package com.kahkong.wikicontacts.service;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public interface ResourceService {	
	public void setContext(Context context);
	
	public Bitmap getNewImage();
	
	public Bitmap getSpamImage();
	
	public String getString(int id);
}
