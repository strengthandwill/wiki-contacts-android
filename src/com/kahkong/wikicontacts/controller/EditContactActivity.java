package com.kahkong.wikicontacts.controller;

import com.kahkong.wikicontacts.R;
import com.kahkong.wikicontacts.callback.Validator;
import com.kahkong.wikicontacts.form.EditTextForm;
import com.kahkong.wikicontacts.form.ImageUploadForm;
import com.kahkong.wikicontacts.form.MultiEditTextForm;
import com.kahkong.wikicontacts.form.RadioGroupForm;
import com.kahkong.wikicontacts.form.SpinnerForm;
import com.kahkong.wikicontacts.form.TagsForm;
import com.kahkong.wikicontacts.modal.Contact;
import com.kahkong.wikicontacts.service.ContactService;
import com.kahkong.wikicontacts.service.ContactServiceImpl;
import com.kahkong.wikicontacts.service.CountryCodeService;
import com.kahkong.wikicontacts.service.CountryCodeServiceImpl;
import com.kahkong.wikicontacts.service.IntentService;
import com.kahkong.wikicontacts.service.IntentServiceImpl;
import com.kahkong.wikicontacts.service.ResourceService;
import com.kahkong.wikicontacts.service.ResourceServiceImpl;
import com.kahkong.wikicontacts.service.ValidationService;
import com.kahkong.wikicontacts.service.ValidationServiceImpl;
import com.kahkong.wikicontacts.subcontroller.LoadingDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public abstract class EditContactActivity extends Activity {
	protected Contact contact = new Contact();

	protected ValidationService validationService = ValidationServiceImpl.getInstance();
	protected ContactService contactService = ContactServiceImpl.getInstance();
	protected CountryCodeService countryCodeService = CountryCodeServiceImpl.getInstance();
	private ResourceService resourceService = ResourceServiceImpl.getInstance();
	private IntentService intentService = IntentServiceImpl.getInstance();
	protected EditTextForm nameETF;
	protected SpinnerForm countryCodeSPF;
	protected MultiEditTextForm numberMETF;
	protected MultiEditTextForm emailMETF;
	protected RadioGroupForm spamRGF;
	protected EditTextForm descriptionETF;
	protected TagsForm tagsForm;
	protected EditTextForm addressETF;
	protected EditTextForm websiteETF;
	protected Button submitBtn;
	protected Button removeBtn;
	protected Button cancelBtn;
	protected LoadingDialog loadingDialog;
	protected ImageUploadForm imageIUF;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_edit);
		resourceService.setContext(this);
		intentService.setActivity(this);
		loadingDialog = new LoadingDialog(this);
		initForms();		
		initButtons();
	}
	
	protected void initForms() {		
		nameETF = (EditTextForm) findViewById(R.id.editNameETF);		
		countryCodeSPF = (SpinnerForm) findViewById(R.id.editCountryCodeSPF);
		numberMETF = (MultiEditTextForm) findViewById(R.id.editNumberMETF);
		emailMETF = (MultiEditTextForm) findViewById(R.id.editEmailMETF);
		spamRGF = (RadioGroupForm) findViewById(R.id.editSpamRGF);
		descriptionETF = (EditTextForm) findViewById(R.id.editDescriptionETF);
		tagsForm = (TagsForm) findViewById(R.id.editTagsForm);
		websiteETF = (EditTextForm) findViewById(R.id.editWebsiteETF);
		addressETF = (EditTextForm) findViewById(R.id.editAddressETF);
		submitBtn = (Button) findViewById(R.id.editSubmitBtn);
		removeBtn = (Button) findViewById(R.id.editRemoveBtn);
		cancelBtn = (Button) findViewById(R.id.editCancelBtn);
		imageIUF = (ImageUploadForm) findViewById(R.id.editImageIUF);
		
		nameETF.setValidator(new Validator() {
			@Override
			public int validate(String value) {
				return validationService.isRequired(value)? 0:1;
			}			
		}, new String[] {"Sorry please enter a name"});
		
		websiteETF.setValidator(new Validator() {
			@Override
			public int validate(String value) {
				return validationService.isValidUrl(value)? 0:1;
			}			
		}, new String[] {"Sorry please enter a valid website"});

		countryCodeSPF.setValidator(new Validator() {
			@Override
			public int validate(String value) {
				return validationService.isRequired(value)? 0:1;
			}	
		});
		
		numberMETF.setValidator(new Validator() {
			@Override
			public int validate(String value) {
				if (validationService.isOrRequired(new String[] {value, emailMETF.getPrimaryValue()})) {
					emailMETF.setPrimaryErrorMsg(0);
					return 0;
				} else {
					emailMETF.setPrimaryErrorMsg(1);
					return 1;
				}
			}			
		}, new String[] {"Sorry please enter a number or email below"});
		
		emailMETF.setValidator(new Validator() {
			@Override
			public int validate(String value) {
				if (validationService.isOrRequired(new String[] {value, numberMETF.getPrimaryValue()})) {
					numberMETF.setPrimaryErrorMsg(0);
				} else {
					numberMETF.setPrimaryErrorMsg(1);
					return 1;
				}	
				return validationService.isValidEmail(value)? 0:2;
			}			
		}, new String[] {"Sorry please enter an email or number above", "Sorry please enter a valid email"});
		
		spamRGF.setOnChangeListener(imageIUF);
	}
	
	protected boolean isValidated() {
		boolean isValidated = true;				
		isValidated = nameETF.submit() && isValidated;
		isValidated = numberMETF.submit() && isValidated;
		isValidated = emailMETF.submit() && isValidated;
		isValidated = countryCodeSPF.submit() && isValidated;
		isValidated = websiteETF.submit() && isValidated;
		if (!isValidated) {
			return false;
		}
		contact.setName(nameETF.getValue());
		contact.setNumber(numberMETF.getValues());
		contact.setEmail(emailMETF.getValues());
		contact.setCountryCode(countryCodeSPF.getValue());
		contact.setSpam(spamRGF.getValue());
		contact.setDescription(descriptionETF.getValue());
		contact.setTags(tagsForm.getValues());
		contact.setAddress(addressETF.getValue());
		contact.setWebsite(websiteETF.getValue());
		if (spamRGF.getValue()) {
			contact.setImageUrl(null);
		}
		return true;
	}
	
	protected void initButtons() {
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();	
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == imageIUF.getRequestCode() && resultCode == Activity.RESULT_OK) {
			imageIUF.setImage(intentService.getActivityResultInputStream(data));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}	
	
	protected void showMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
}
