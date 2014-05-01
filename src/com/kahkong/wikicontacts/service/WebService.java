package com.kahkong.wikicontacts.service;

import com.kahkong.wikicontacts.callback.OnCompleteListener;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public interface WebService {
	public static enum Method {GET, POST};
	
	public void getContacts(String query, OnCompleteListener onCompleteListener);
		
	public void addContactAndImage(String contactJson, byte[] bytes, OnCompleteListener onCompleteListener);
	
	public void updateContactAndImage(String contactJson, byte[] bytes, OnCompleteListener onCompleteListener);
	
	public void removeContact(int id, OnCompleteListener onCompleteListener);
	
	public void getImageApp(String url, OnCompleteListener onCompleteListener);
	
	public void listCountryCodes(OnCompleteListener onCompleteListener);
	
	public void listAndSelectCountryCodes(OnCompleteListener onCompleteListener);	
	
	public void getContact(String number, OnCompleteListener onCompleteListener);
		
	public void getImageSvc(String url, OnCompleteListener onCompleteListener);
			
	public void appCancel();
	
	public void svcCancel();
}
