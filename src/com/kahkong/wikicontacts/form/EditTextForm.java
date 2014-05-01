package com.kahkong.wikicontacts.form;

import com.kahkong.wikicontacts.callback.Validator;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class EditTextForm extends EditText {
	private Validator validator;
	private String[] errorMsgs;
	private boolean isSubmitted = false;
	
	public EditTextForm(Context context) {
		super(context);
	}

	public EditTextForm(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public String getValue() {
		String value = getText().toString();		
		return !value.equals("") ? value:null;
	}
	
	public void setValue(String value) {
		if (value==null) {
			return;
		}
		setText(value);
	}

	public void setValidator(final Validator validator, String[] errorMsgs) {
		this.validator = validator;
		this.errorMsgs = errorMsgs;
		addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable editable) {
				if (!isSubmitted) {
					return;
				}	
				isValidated();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
			
		});
	}
	
	public boolean submit() {
		isSubmitted = true;
		return isValidated();
	}	
	
	private boolean isValidated() {
		if (validator==null) {
			return true;
		}
		int status = validator.validate(getText().toString());
		if (status>0) {
			setError(errorMsgs[status-1]);
			return false;
		} else {
			return true;
		}
	}
}
