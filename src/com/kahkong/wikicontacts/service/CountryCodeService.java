package com.kahkong.wikicontacts.service;

import com.kahkong.wikicontacts.callback.OnCompleteListener;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public interface CountryCodeService {	
	public void listCountryCodes(OnCompleteListener onCompleteListener);
	
	public void listAndSelectCountryCodes(OnCompleteListener onCompleteListener);
}
