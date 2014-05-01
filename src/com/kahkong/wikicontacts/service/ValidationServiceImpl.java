package com.kahkong.wikicontacts.service;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class ValidationServiceImpl implements ValidationService {
	private static ValidationService instance;	
	
	private static final String EMAIL_PATTERN = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";	
	private static final String URL_PATTERN = "^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$";
	
	private ValidationServiceImpl() {	
	}

	public synchronized static ValidationService getInstance() {
		if (instance==null) {
			instance = new ValidationServiceImpl();
		}
		return instance;
	}
	
	public boolean isRequired(String text) {
		return !text.equals("") ? true:false;
	}
	
	public boolean isOrRequired(String[] texts) {
		if (texts==null) {
			return false;
		}		
		for (int i=0; i<texts.length; i++) {
			if (!texts[i].equals("")) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isValidEmail(String text) {
		return validate(text, EMAIL_PATTERN);
	}
	
	public boolean isValidUrl(String text) {
		return validate(text, URL_PATTERN);
	}

	
	private boolean validate(String text, String pattern) {
		if (text.equals("")) {
			return true;
		}
		return text.matches(pattern);
	}
}
