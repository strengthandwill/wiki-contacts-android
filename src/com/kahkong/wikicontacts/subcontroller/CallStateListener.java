package com.kahkong.wikicontacts.subcontroller;

import com.kahkong.wikicontacts.callback.OnCompleteListener;
import com.kahkong.wikicontacts.modal.Contact;
import com.kahkong.wikicontacts.service.ContactService;
import com.kahkong.wikicontacts.service.ContactServiceImpl;
import com.kahkong.wikicontacts.service.PhoneBookService;
import com.kahkong.wikicontacts.service.PhoneBookServiceImpl;
import com.kahkong.wikicontacts.service.WebService;
import com.kahkong.wikicontacts.service.WebServiceImpl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class CallStateListener extends PhoneStateListener {
	private Context context;
	private PhoneBookService phoneBookService = PhoneBookServiceImpl.getInstance();
	private ContactService contactService = ContactServiceImpl.getInstance();
	private WebService webService = WebServiceImpl.getInstance();
	private TelephonyManager telephonyManager;
	private WindowManager windowManager;
	private LayoutParams windowManagerParams;
	private IncomingContactLayout incomingContactLayout;

	public CallStateListener(Context context) {
		this.context = context;
		telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);	
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManagerParams = new WindowManager.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT |
				WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
	}

	public void register() {
		if (telephonyManager==null) {
			return;
		}
		telephonyManager.listen(this, PhoneStateListener.LISTEN_CALL_STATE);
	}

	public void unregister() {
		if (telephonyManager==null) {
			return;
		}
		telephonyManager.listen(this, PhoneStateListener.LISTEN_NONE);
	}

	@Override
	public void onCallStateChanged(int state, final String number) {
		super.onCallStateChanged(state, number);
		switch (state) {
		case TelephonyManager.CALL_STATE_RINGING:
			final Handler handler = new Handler();
	        handler.postDelayed(new Runnable() {
	            @Override
	            public void run() {	            		            
	            	addContactView(number);
	            }
	        }, 1000);							
			break;
		case TelephonyManager.CALL_STATE_IDLE:
		case TelephonyManager.CALL_STATE_OFFHOOK:
			cancel();
			break;
		}				
	}

	private void addContactView(final String number) {
		if (phoneBookService.isContactExists(number)) { // contact in phonebook
			return;
		}		
		incomingContactLayout = new IncomingContactLayout(context);		
		windowManager.addView(incomingContactLayout, windowManagerParams);		
		incomingContactLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {						
				cancel();						
			}					
		});
		
		contactService.getContact(number, new OnCompleteListener() {
			@Override
			public void onComplete(Object[] objects) {
				Contact contact = (Contact) objects[0];
				if (contact==null) { // contact not in database
					cancel();
					return;
				}					
				incomingContactLayout.setContact(contact, number);						
			}			
		});
	}

	private void cancel() {
		webService.svcCancel();
		if (incomingContactLayout==null) {
			return;
		}
		windowManager.removeView(incomingContactLayout);
		incomingContactLayout = null;
	}
}
