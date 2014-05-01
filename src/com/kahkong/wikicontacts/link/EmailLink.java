package com.kahkong.wikicontacts.link;

import com.kahkong.wikicontacts.service.IntentService;
import com.kahkong.wikicontacts.service.IntentServiceImpl;

import android.content.Context;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class EmailLink extends Link {
	protected IntentService intentService = IntentServiceImpl.getInstance();
	
	protected String email;
	
	private EmailLink(Context context) {
		super(context);
	}

	public EmailLink(Context context, String email) {
		super(context);
		this.email = email;
		initValues();
	}
	
	@Override
	protected void initValues() {
		value = email;
		message = "Send email to " + email + "?";
		super.initValues();
	}

	@Override
	protected void action() {
		intentService.startEmailActivity(email);
	}
}
