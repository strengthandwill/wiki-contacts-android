package com.kahkong.wikicontacts.service;

import android.content.Context;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public interface PreferencesService {
	public void setSharedPreferences(Context context);
	
	public boolean getCallerId();
	
	public void setCallerId(boolean value);
	
	public boolean getInfoHidden();
	
	public void setInfoHidden(boolean value);
	
	public boolean getBoolean(String key);
	
	public void setBoolean(String key, boolean value);
}
