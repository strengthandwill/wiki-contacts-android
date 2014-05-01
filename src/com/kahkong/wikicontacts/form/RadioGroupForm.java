package com.kahkong.wikicontacts.form;

import com.kahkong.wikicontacts.callback.OnChangeListener;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class RadioGroupForm extends RadioGroup {
	private RadioButton trueRB;
	private RadioButton falseRB;
	
	public RadioGroupForm(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) {
			return;
		}	
		setOrientation(RadioGroup.HORIZONTAL);		
		trueRB = new RadioButton(context);
		addView(trueRB);
		falseRB = new RadioButton(context);		
		addView(falseRB);		
		trueRB.setText("Yes");
		falseRB.setText("No");
		falseRB.setChecked(true);
		
	}
	
	public void setOnChangeListener(final OnChangeListener onChangeListener) {
		if (onChangeListener==null) {
			return;
		}
		setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (getCheckedRadioButtonId()==trueRB.getId()) {
					onChangeListener.onChange(Boolean.valueOf(true));	
				} else {
					onChangeListener.onChange(Boolean.valueOf(false));
				}
			}		
		});
	}
	
	public boolean getValue() {
		if (getCheckedRadioButtonId()==trueRB.getId()) {
			return true;	
		} else {
			return false;
		}		
	}
	
	public void setValue(boolean value) {
		if (value) {
			trueRB.setChecked(true);
		} else {
			falseRB.setChecked(true);
		}
	}
}
