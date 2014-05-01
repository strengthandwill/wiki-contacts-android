package com.kahkong.wikicontacts.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class PreferencesServiceImpl implements PreferencesService {
	private static PreferencesService instance;
	
	private final boolean BOOLEAN_DEFAULTVALUE = false;
	private final String CALLERID_KEY = "callerid";
	private final String INFOHIDDEN_KEY = "infohidden";
	
	private SharedPreferences sharedPreferences;

	private PreferencesServiceImpl() {	
	}

	public synchronized static PreferencesService getInstance() {
		if (instance==null) {
			instance = new PreferencesServiceImpl();
		}
		return instance;
	}
	
	public void setSharedPreferences(Context context) {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public boolean getCallerId() {
		return getBoolean(CALLERID_KEY);
	}
	
	public void setCallerId(boolean value) {
		setBoolean(CALLERID_KEY, value);
	}	
	
	public boolean getInfoHidden() {
		return getBoolean(INFOHIDDEN_KEY);
	}
	
	public void setInfoHidden(boolean value) {
		setBoolean(INFOHIDDEN_KEY, value);
	}	
	
	public boolean getBoolean(String key) {
		return sharedPreferences.getBoolean(key, BOOLEAN_DEFAULTVALUE);
	}
	
	public void setBoolean(String key, boolean value) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
}
