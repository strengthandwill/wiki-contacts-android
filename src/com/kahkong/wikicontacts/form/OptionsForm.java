package com.kahkong.wikicontacts.form;

import com.kahkong.wikicontacts.R;
import com.kahkong.wikicontacts.callback.OnEventListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class OptionsForm extends LinearLayout {
	public static enum Event {CALLID_ON, CALLID_OFF, HOMEPAGE, FEEDBACK, RATE, ICONS};
	
	private CheckBox callerIdCB;
	private TextView homePageTV;
	private TextView feedBackTV;
	private TextView rateTV;
	private TextView iconsTV;
	
	public OptionsForm(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) {
			return;
		}		
		inflate(context, R.layout.search_options, this);
		callerIdCB = (CheckBox) findViewById(R.id.searchCallerIdCBOF);
		homePageTV = (TextView) findViewById(R.id.searchHomePageTVOF);
		feedBackTV = (TextView) findViewById(R.id.searchFeedbackTVOF);
		rateTV = (TextView) findViewById(R.id.searchRateTVOF);
		iconsTV = (TextView) findViewById(R.id.searchIconsTVOF);
	}
	
	public void setCallId(boolean callerId) {
		callerIdCB.setChecked(callerId);
	}

	public void setOnEventListener(final OnEventListener onEventListener) {
		callerIdCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					onEventListener.onEvent(Event.CALLID_ON, null);
				} else {
					onEventListener.onEvent(Event.CALLID_OFF, null);
				}
			}			
		});
		
		homePageTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onEventListener.onEvent(Event.HOMEPAGE, null);
				OptionsForm.this.setVisibility(View.INVISIBLE);
			}			
		});
		
		feedBackTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onEventListener.onEvent(Event.FEEDBACK, null);
				OptionsForm.this.setVisibility(View.INVISIBLE);
			}			
		});	
		
		rateTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onEventListener.onEvent(Event.RATE, null);
				OptionsForm.this.setVisibility(View.INVISIBLE);
			}			
		});		
		
		iconsTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onEventListener.onEvent(Event.ICONS, null);
				OptionsForm.this.setVisibility(View.INVISIBLE);
			}			
		});	
	}
}
