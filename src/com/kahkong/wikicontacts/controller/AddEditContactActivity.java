package com.kahkong.wikicontacts.controller;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.kahkong.wikicontacts.R;
import com.kahkong.wikicontacts.callback.OnCompleteListener;
import com.kahkong.wikicontacts.modal.CountryCode;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class AddEditContactActivity extends EditContactActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		initValues();
	}
	
	@Override
	protected void initForms() {
		super.initForms();		
		submitBtn.setText("Add");
		removeBtn.setVisibility(View.GONE);				
	}
	
	@Override
	protected void initButtons() {
		super.initButtons();
		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!isValidated()) {
					return;
				}
				contactService.addContactAndImage(contact, imageIUF.getImage(), new OnCompleteListener() {
					@Override
					public void onComplete(Object[] objects) {
						boolean status = (Boolean) objects[0];
						if (status) {						
							showMsg(getResources().getString(R.string.add_success));
						} else {
							showMsg(getResources().getString(R.string.operation_error));
						}
					}
				});
				finish();
			}
		});		
	}
	
	protected void initValues() {
		loadingDialog.show();
		countryCodeService.listAndSelectCountryCodes(new OnCompleteListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onComplete(Object[] objects) {
				List<CountryCode> countryCodes = (List<CountryCode>) objects[0];
				CountryCode select = (CountryCode) objects[1];
				countryCodeSPF.setItems(countryCodes, select);				
				loadingDialog.dismiss();				
			}			
		});
	}
}