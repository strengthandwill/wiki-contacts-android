package com.kahkong.wikicontacts.controller;

import com.kahkong.wikicontacts.R;
import com.kahkong.wikicontacts.subcontroller.InfoDialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class TestActivity extends Activity {
	
	private Button testBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);			
		setContentView(R.layout.test);
		
		
		testBtn = (Button) findViewById(R.id.testBtn);
		testBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				InfoDialog infoDialog = new InfoDialog(TestActivity.this);
				infoDialog.show();
			}
		});
	}
}
