package com.kahkong.wikicontacts.modal;

import java.util.List;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class CountryCodesWithSelect {
	private List<CountryCode> countryCodes;
	private CountryCode select;
	
	public List<CountryCode> getCountryCodes() {
		return countryCodes;
	}
	
	public void setCountryCodes(List<CountryCode> countryCodes) {
		this.countryCodes = countryCodes;
	}
	
	public CountryCode getSelect() {
		return select;
	}
	
	public void setSelect(CountryCode select) {
		this.select = select;
	}	
}
