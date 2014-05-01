package com.kahkong.wikicontacts.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.kahkong.wikicontacts.R;
import com.kahkong.wikicontacts.callback.OnCompleteListener;
import com.kahkong.wikicontacts.callback.OnEventListener;
import com.kahkong.wikicontacts.form.OptionsForm;
import com.kahkong.wikicontacts.modal.Contact;
import com.kahkong.wikicontacts.modal.ContactAndImage;
import com.kahkong.wikicontacts.service.ContactService;
import com.kahkong.wikicontacts.service.ContactServiceImpl;
import com.kahkong.wikicontacts.service.ImageService;
import com.kahkong.wikicontacts.service.ImageServiceImpl;
import com.kahkong.wikicontacts.service.IntentService;
import com.kahkong.wikicontacts.service.IntentServiceImpl;
import com.kahkong.wikicontacts.service.NotificationService;
import com.kahkong.wikicontacts.service.NotificationServiceImpl;
import com.kahkong.wikicontacts.service.PreferencesService;
import com.kahkong.wikicontacts.service.PreferencesServiceImpl;
import com.kahkong.wikicontacts.service.ResourceService;
import com.kahkong.wikicontacts.service.ResourceServiceImpl;
import com.kahkong.wikicontacts.service.WebService;
import com.kahkong.wikicontacts.service.WebServiceImpl;
import com.kahkong.wikicontacts.subcontroller.InfoDialog;
import com.kahkong.wikicontacts.subcontroller.ViewContactDialog;
import com.kahkong.wikicontacts.subcontroller.ResultAdaptor;

import android.app.Activity;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.view.View.OnClickListener;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class SearchActivity extends Activity {	
	private ContactService contactService = ContactServiceImpl.getInstance();
	private ResourceService resourceService = ResourceServiceImpl.getInstance();
	private WebService webService = WebServiceImpl.getInstance();
	private ImageService imageService = ImageServiceImpl.getInstance();
	private PreferencesService preferencesService = PreferencesServiceImpl.getInstance();
	private IntentService intentService = IntentServiceImpl.getInstance();
	private NotificationService notificationService = NotificationServiceImpl.getInstance();

	private View backgroundView;
	private EditText inputET;
	private ProgressBar loadingPB;
	private ListView resultLV;
	private Button addBtn;
	private ImageView resetIV;
	private ImageView optionsIV;
	private ResultAdaptor resultAdaptor;
	private OptionsForm optionsOF;
	private String query;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.application);
		resourceService.setContext(this);
		preferencesService.setSharedPreferences(this);
		intentService.setActivity(this);
		notificationService.setNotificationManager(this);
		
		FrameLayout applicationFL = (FrameLayout) findViewById(R.id.applicationFL);
		backgroundView = getLayoutInflater().inflate(R.layout.background, null);
		View searchView = getLayoutInflater().inflate(R.layout.search, null);
		applicationFL.addView(backgroundView);
		applicationFL.addView(searchView);

		inputET = (EditText) findViewById(R.id.searchInputET);
		loadingPB = (ProgressBar) findViewById(R.id.searchLoadingPB);
		resultLV = (ListView) findViewById(R.id.searchResultLV);
		addBtn = (Button) findViewById(R.id.searchAddBtn);
		resetIV = (ImageView) findViewById(R.id.searchResetIV);
		optionsIV = (ImageView) findViewById(R.id.searchOptionsIV);
		optionsOF = (OptionsForm) findViewById(R.id.searchOptionsOF);
		
		initResultAdaptor();
		initSearchET();
		initResultLV();
		initBtns();
		initOptionsOP();
		initPreferences();
	}
	
	private void initResultAdaptor() {
		resultAdaptor = new ResultAdaptor(SearchActivity.this, null);
		resultLV.setAdapter(resultAdaptor);	
	}
	
	private void initSearchET() {
		inputET.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable editable) {
				query = editable.toString();
				if (query.equals("")) {
					webService.appCancel();
					resultAdaptor.clear();	
					loadingPB.setVisibility(View.INVISIBLE);
					resetIV.setVisibility(View.INVISIBLE);
					backgroundView.setVisibility(View.VISIBLE);
					return;
				}
				search();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {							
			}
		});
	}
	
	private void initResultLV() {
		resultLV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final ContactAndImage contactAndImage = (ContactAndImage) resultAdaptor.getItem(position);
				ViewContactDialog viewContactDialog = new ViewContactDialog(SearchActivity.this, contactAndImage, new OnEventListener() {
					@Override
					public void onEvent(Object event, Object data) {
						switch ((ViewContactDialog.Event) event) {
						case EDIT_CLICK:
							Bitmap image = null;
							if (contactAndImage.getImageUrl()!=null) {
								image = contactAndImage.getImage();
							}							
							String[] name = {"Contact", "Image"};
							Parcelable [] extra = {(Contact) contactAndImage, image};
							intentService.startActivityWithParcelable(UpdateEditContactActivity.class, name, extra);							
							break;
						case TAG_CLICK:
							if (!query.equals("")) {
								inputET.setText(query + " "  + (String) data);
							} else {
								inputET.setText((String) data);
							}							
							break;
						default:
							break;						
						}
												
					}					
				});
				viewContactDialog.show();
			}			
		});
	}
	
	private void initBtns() {
		addBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				intentService.startActivity(AddEditContactActivity.class);						
			}			
		});
		
		resetIV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				resetSearch();
			}			
		});
		
		optionsIV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				optionsOF.setVisibility(View.VISIBLE);
			}			
		});
	}
	
	private void initOptionsOP() {
		optionsOF.setCallId(preferencesService.getCallerId());
		optionsOF.setVisibility(View.INVISIBLE);
		optionsOF.setOnEventListener(new OnEventListener() {
			@Override
			public void onEvent(Object event, Object data) {
				switch ((OptionsForm.Event) event) {
				case CALLID_ON:
					startCallDetectService();
					break;
				case CALLID_OFF:
					stopCallDetectService();
					break;
				case HOMEPAGE:
					intentService.startUrlActivity(getResources().getString(R.string.app_web));
					break;					
				case FEEDBACK:
					intentService.startEmailActivity(getResources().getString(R.string.app_email), getResources().getString(R.string.app_name));
					break;
				case RATE:
					intentService.startUrlActivity(getResources().getString(R.string.app_android));
					break;					
				case ICONS:
					intentService.startUrlActivity(getResources().getString(R.string.search_iconsurl));
					break;					
				default:
					break;				
				}
			}			
		});
	}
	
	private void initPreferences() {
		if (preferencesService.getCallerId()) {
			startCallDetectService();
		} else {
			stopCallDetectService();
		}
		if (!preferencesService.getInfoHidden()) {
			InfoDialog infoDialog = new InfoDialog(this);
			infoDialog.show();
		}
	}
	
	private List<ContactAndImage> contactsToViewContacts(List<Contact> contacts) {
		if (contacts==null) {
			return null;
		}		
		List<ContactAndImage> viewContacts = new ArrayList<ContactAndImage>();
		Iterator <Contact> iterator = contacts.iterator();
		while (iterator.hasNext()) {
			final ContactAndImage contactAndImage = new ContactAndImage(iterator.next()); 
			viewContacts.add(contactAndImage);			
			imageService.getImage(contactAndImage.isSpam(), contactAndImage.getImageUrl(), new OnCompleteListener() {
				@Override
				public void onComplete(Object[] objects) {	
					Bitmap image = (Bitmap) objects[0];
					contactAndImage.setImage(image);
					resultAdaptor.notifyDataSetChanged();
				}					
			});	
		}
		return viewContacts;
	}
	
	private void search() {	
		if (query==null || query.equals("") || query.matches(".*\\s+$")) {
			return;
		}
		
		loadingPB.setVisibility(View.VISIBLE);
		resetIV.setVisibility(View.INVISIBLE);
		backgroundView.setVisibility(View.INVISIBLE);
		contactService.getContacts(query, new OnCompleteListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onComplete(Object[] objects) {				
				List<Contact> contacts = (List<Contact>) objects[0];
				if (contacts==null) { // contact not in database
					loadingPB.setVisibility(View.INVISIBLE);
					return;
				}
				resultAdaptor.setContacts(contactsToViewContacts(contacts));
				resultAdaptor.notifyDataSetChanged();
				loadingPB.setVisibility(View.INVISIBLE);
				resetIV.setVisibility(View.VISIBLE);
			}			
		});		
	}
	
	private void resetSearch() {
		inputET.setText("");
	}
	
	private void startCallDetectService() {
		intentService.startService(CallDetectService.class);
		preferencesService.setCallerId(true);
		PendingIntent pendingIntent = intentService.getPendingIntent(SearchActivity.class, notificationService.getRequestCode());
		notificationService.show(getResources().getString(R.string.notification_title), getResources().getString(R.string.notification_content), pendingIntent);		
	}
	
	private void stopCallDetectService() {
		intentService.stopService(CallDetectService.class);	
		preferencesService.setCallerId(false);
		notificationService.cancel();		
	}	
	
	@Override
	protected void onPause() {
		webService.appCancel();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		search();
	}

	@Override
	public void onBackPressed() {
		if (optionsOF.getVisibility()==View.VISIBLE) {
			optionsOF.setVisibility(View.GONE);
			return;
		}
		super.onBackPressed();
	}
}
