package com.kahkong.wikicontacts.link;

import com.kahkong.wikicontacts.service.IntentService;
import com.kahkong.wikicontacts.service.IntentServiceImpl;

import android.content.Context;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class NumberLink extends Link {
	protected IntentService intentService = IntentServiceImpl.getInstance();
	
	protected String number;
	protected String countryCode;
	
	private NumberLink(Context context) {
		super(context);
	}

	public NumberLink(Context context, String number, String countryCode) {
		super(context);
		this.number = number;
		this.countryCode = countryCode;
		initValues();
	}
	
	@Override
	protected void initValues() {
		value = number;
		message = "Call +(" +countryCode + ") " + number + "?";
		super.initValues();
	}

	@Override
	protected void action() {
		intentService.startCallActivity(number, countryCode);
	}
}
