package com.kahkong.wikicontacts.form;

import java.util.Iterator;
import java.util.List;

import com.kahkong.wikicontacts.R;
import com.kahkong.wikicontacts.callback.Validator;
import com.kahkong.wikicontacts.modal.CountryCode;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class SpinnerForm extends RelativeLayout  {
	private Context context;
	private String[] values;
	private Validator validator;
	private boolean isSubmitted = false;	

	private Spinner spinner;
	private ImageView imageView;

	public SpinnerForm(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) {
			return;
		}
		
		this.context = context;		
		inflate(context, R.layout.contact_edit_spinner, this);
		spinner = (Spinner) findViewById(R.id.editSpinnerSPF);
		imageView = (ImageView) findViewById(R.id.editImageViewSPF);
		imageView.setVisibility(View.INVISIBLE);
	}
	
	public void setItems(List<CountryCode> countryCodes, CountryCode select) {
		String value = null;
		if (select!=null) {
			value = select.getIsoCountryCode()+select.getCountry();
		} 
		setItems(countryCodes, value);
	}
	
	public void setItems(List<CountryCode> countryCodes, String value) {
		String[] labels = new String[countryCodes.size()+1];
		values = new String[countryCodes.size()+1];
		labels[0] = "Select a country";
		values[0] = "";
		int position = 0;
		int i=1;
		Iterator<CountryCode> iterator = countryCodes.iterator();
		while (iterator.hasNext()) {
			CountryCode countryCode = iterator.next();
			if (value!=null && countryCode.getIsoCountryCode().equals(value.substring(0, 2))) {
				position = i;
			}
			labels[i] = countryCode.getCountry() + " (+" + countryCode.getCountryCode() + ")";
			values[i] = countryCode.getIsoCountryCode() + countryCode.getCountryCode();
			i++;
		}
		spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, labels));
		spinner.setSelection(position);
	}
	
	public String getValue() {
		return values[spinner.getSelectedItemPosition()];
	}

	public void setValidator(final Validator validator) {
		this.validator = validator;
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (!isSubmitted) {
					return;
				}				
				isValidated();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}			
		});
	}

	public boolean submit() {
		isSubmitted = true;
		return isValidated();
	}

	private boolean isValidated() {	
		int status = validator.validate(getValue());
		if (status>0) {
			imageView.setVisibility(View.VISIBLE);
			return false;
		} else {
			imageView.setVisibility(View.INVISIBLE);
			return true;
		}
	}
}
