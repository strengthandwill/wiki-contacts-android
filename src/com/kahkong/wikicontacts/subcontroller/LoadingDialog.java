package com.kahkong.wikicontacts.subcontroller;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class LoadingDialog extends ProgressDialog {
	public LoadingDialog(Context context) {
		super(context);
		setInverseBackgroundForced(true);
		setMessage("Please wait..");
	}
}
