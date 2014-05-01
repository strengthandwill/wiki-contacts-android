package com.kahkong.wikicontacts.service;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public interface ValidationService {
	public boolean isRequired(String text);
	
	public boolean isOrRequired(String[] texts);
	
	public boolean isValidEmail(String text);
	
	public boolean isValidUrl(String text);
}
