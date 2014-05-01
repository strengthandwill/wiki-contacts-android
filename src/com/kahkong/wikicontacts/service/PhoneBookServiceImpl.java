package com.kahkong.wikicontacts.service;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class PhoneBookServiceImpl implements PhoneBookService {
	private static PhoneBookService instance;
	
	private Context context;
	
	private PhoneBookServiceImpl() {	
	}

	public synchronized static PhoneBookService getInstance() {
		if (instance==null) {
			instance = new PhoneBookServiceImpl();
		}
		return instance;
	}

	public boolean isContactExists(String number) {
		Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		String[] mPhoneNumberProjection = { PhoneLookup._ID, PhoneLookup.NUMBER, PhoneLookup.DISPLAY_NAME };
		Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
		try {
			if (cur.moveToFirst()) {
				return true;
			}
		} finally {
			if (cur!= null)
				cur.close();
		}
		return false;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
}
