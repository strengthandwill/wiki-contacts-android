package com.kahkong.wikicontacts.controller;

import java.util.List;

import android.graphics.Bitmap;
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
public class UpdateEditContactActivity extends EditContactActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contact = getIntent().getExtras().getParcelable("Contact");
		Bitmap image = (Bitmap) getIntent().getParcelableExtra("Image");		
		initValues(image);
	}
	
	@Override
	protected void initForms() {		
		super.initForms();
		submitBtn.setText("Update");
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
				contactService.updateContactAndImage(contact, imageIUF.getImage(), new OnCompleteListener() {
					@Override
					public void onComplete(Object[] objects) {
						boolean status = (Boolean) objects[0];
						if (status) {						
							showMsg(getResources().getString(R.string.update_success));
						} else {
							showMsg(getResources().getString(R.string.operation_error));
						}
					}
				});
				finish();
			}
		});
		
		removeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				contactService.removeContact(contact.getId(), new OnCompleteListener() {
					@Override
					public void onComplete(Object[] objects) {
						boolean status = (Boolean) objects[0];
						if (status) {						
							showMsg(getResources().getString(R.string.remove_success));
						} else {
							showMsg(getResources().getString(R.string.operation_error));
						}
					}
				});
				finish();
			}
		});
	}
	
	protected void initValues(Bitmap image) {					
		nameETF.setValue(contact.getName());
		numberMETF.setValues(contact.getNumber());
		emailMETF.setValues(contact.getEmail());
		spamRGF.setValue(contact.isSpam());
		descriptionETF.setValue(contact.getDescription());
		tagsForm.setValues(contact.getTags());
		addressETF.setValue(contact.getAddress());
		websiteETF.setValue(contact.getWebsite());
		loadingDialog.show();
		countryCodeService.listCountryCodes(new OnCompleteListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onComplete(Object[] objects) {
				List<CountryCode> countryCodes = (List<CountryCode>) objects[0];				
				countryCodeSPF.setItems(countryCodes, contact.getCountryCode());				
				loadingDialog.dismiss();				
			}			
		});
		imageIUF.setImage(image, contact.isSpam());
	}	
}
