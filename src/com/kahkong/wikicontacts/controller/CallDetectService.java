package com.kahkong.wikicontacts.controller;

import com.kahkong.wikicontacts.service.PhoneBookService;
import com.kahkong.wikicontacts.service.PhoneBookServiceImpl;
import com.kahkong.wikicontacts.service.ResourceService;
import com.kahkong.wikicontacts.service.ResourceServiceImpl;
import com.kahkong.wikicontacts.subcontroller.CallStateListener;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class CallDetectService extends Service {
	private static final String TAG = "CallDetectService";
	
	private PhoneBookService phoneBookService = PhoneBookServiceImpl.getInstance();
	private ResourceService resourceService = ResourceServiceImpl.getInstance();
	
	private CallStateListener callStateController;	
	
	@Override
	public void onCreate() {
		phoneBookService.setContext(this);
		resourceService.setContext(this);
		callStateController = new CallStateListener(this);
		callStateController.register();
		Log.i(TAG, "created service");
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		callStateController.unregister();
		Log.i(TAG, "destroyed service");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
