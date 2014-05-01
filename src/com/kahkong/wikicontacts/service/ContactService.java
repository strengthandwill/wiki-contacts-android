package com.kahkong.wikicontacts.service;

import android.graphics.Bitmap;

import com.kahkong.wikicontacts.callback.OnCompleteListener;
import com.kahkong.wikicontacts.modal.Contact;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public interface ContactService {
	public void getContact(String number, OnCompleteListener onCompleteListener);
	
	public void getContacts(String query, OnCompleteListener onCompleteListener);
	
	public void addContactAndImage(Contact contact, Bitmap image, OnCompleteListener onCompleteListener);
	
	public void updateContactAndImage(Contact contact, Bitmap image, OnCompleteListener onCompleteListener);
	
	public void removeContact(int id, final OnCompleteListener onCompleteListener);
}
