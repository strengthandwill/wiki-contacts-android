package com.kahkong.wikicontacts.link;

import com.kahkong.wikicontacts.service.IntentService;
import com.kahkong.wikicontacts.service.IntentServiceImpl;

import android.content.Context;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class UrlLink extends Link {
	protected IntentService intentService = IntentServiceImpl.getInstance();
	
	protected String url;
	
	private UrlLink(Context context) {
		super(context);
	}

	public UrlLink(Context context, String url) {
		super(context);
		this.url = url;
		initValues();
	}
	
	@Override
	protected void initValues() {
		value = url;
		message = "Goto to " + url + "?";
		super.initValues();
	}

	@Override
	protected void action() {
		intentService.startUrlActivity(url);
	}
}
