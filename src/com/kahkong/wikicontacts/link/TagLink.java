package com.kahkong.wikicontacts.link;

import android.content.Context;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class TagLink extends Link {
	protected String tag;
	protected com.kahkong.wikicontacts.callback.OnClickListener onClickListener;

	private TagLink(Context context) {
		super(context);
	}

	public TagLink(Context context, String tag, com.kahkong.wikicontacts.callback.OnClickListener onClickListener) {
		super(context);
		this.tag = tag;
		this.onClickListener = onClickListener;
		initValues();
	}

	@Override
	protected void initValues() {
		value = tag;
		message = "Search " + tag + "?";
		super.initValues();
	}

	@Override
	protected void action() {
		onClickListener.onClick(tag);
	}
}
