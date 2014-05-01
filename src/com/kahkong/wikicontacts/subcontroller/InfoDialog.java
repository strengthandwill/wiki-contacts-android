package com.kahkong.wikicontacts.subcontroller;

import com.kahkong.wikicontacts.R;
import com.kahkong.wikicontacts.service.PreferencesService;
import com.kahkong.wikicontacts.service.PreferencesServiceImpl;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class InfoDialog extends Dialog {
	private PreferencesService preferencesService = PreferencesServiceImpl.getInstance();
	
	public InfoDialog(Context context) {
		super(context, R.style.dialogLightNoTitleBar);
		setContentView(R.layout.info);
		Button closeBtn = (Button) findViewById(R.id.infoCloseBtn);
		CheckBox infoHiddenCB = (CheckBox) findViewById(R.id.infoHiddenCB);
		
		closeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}			
		});
		
		infoHiddenCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				preferencesService.setInfoHidden(isChecked);
			}			
		});
	}
}
