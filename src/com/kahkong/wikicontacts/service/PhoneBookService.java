package com.kahkong.wikicontacts.service;

import android.content.Context;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public interface PhoneBookService {
	public boolean isContactExists(String number);
	
	public void setContext(Context context);
}
