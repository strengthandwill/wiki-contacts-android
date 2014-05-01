package com.kahkong.wikicontacts.link;

import com.kahkong.wikicontacts.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public abstract class Link extends LinearLayout {
	protected Context context;
	protected TextView linkTV;
	protected String message;
	protected String value;

	public Link(Context context) {
		super(context);
		this.context = context;
		inflate(context.getApplicationContext(), R.layout.search_link, this);
		linkTV = (TextView) findViewById(R.id.searchLinkTVL);
		linkTV.setPaintFlags(linkTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		linkTV.setSelected(true);
	}

	protected void initlinkTV() {
		linkTV.setText(value);
		linkTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {	
						action();
						dialog.cancel();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();			
			}
		});
	}
	
	protected abstract void action();
	
	protected void initValues() {
		initlinkTV();;
	}
}
