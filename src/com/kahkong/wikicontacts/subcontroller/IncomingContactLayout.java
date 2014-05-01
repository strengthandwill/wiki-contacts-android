package com.kahkong.wikicontacts.subcontroller;

import com.kahkong.wikicontacts.R;
import com.kahkong.wikicontacts.callback.OnCompleteListener;
import com.kahkong.wikicontacts.modal.Contact;
import com.kahkong.wikicontacts.service.ImageService;
import com.kahkong.wikicontacts.service.ImageServiceImpl;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class IncomingContactLayout extends RelativeLayout {
	private ImageService imageService = ImageServiceImpl.getInstance();
	
	private RelativeLayout loadingRL;
	private RelativeLayout viewRL;
	
	private TextView nameTV;
	private TextView numberTV;
	private TextView emailTV;
	private TextView descriptionTV;
	private TextView tagsTV;
	private ImageView imageIV;	
	
	public IncomingContactLayout(Context context) {
		super(context);
		inflate(context, R.layout.contact_incoming, this);
		
		loadingRL = (RelativeLayout) findViewById(R.id.incomingLoadingRL);
		viewRL = (RelativeLayout) findViewById(R.id.incomingViewRL);
		nameTV = (TextView) findViewById(R.id.incomingNameTV);
		numberTV = (TextView) findViewById(R.id.incomingNumberTV);
		emailTV = (TextView) findViewById(R.id.incomingEmailTV);
		descriptionTV = (TextView) findViewById(R.id.incomingDescriptionTV);
		tagsTV = (TextView) findViewById(R.id.incomingTagsTV);
		imageIV = (ImageView) findViewById(R.id.incomingImageIV);
		
		loadingRL.setVisibility(View.VISIBLE);
		viewRL.setVisibility(View.GONE);
	}
	
	public void setContact(Contact contact, String number) {
		nameTV.setText(contact.getName());
		numberTV.setText("(" + contact.getCountryCode().substring(0, 2) + ") " + number);
		emailTV.setVisibility(View.GONE);
		if (contact.getDescription()!=null) {
			descriptionTV.setText(contact.getDescription());
			tagsTV.setVisibility(View.GONE);
		} else {
			descriptionTV.setVisibility(View.GONE);
			if (contact.getTags()!=null) {
				tagsTV.setText("tags: " + ViewFormatter.arrayToString(contact.getTags()));
			} else {
				tagsTV.setVisibility(View.GONE);
			}
		}
		
		imageService.getImage(contact.isSpam(), contact.getImageUrl(), new OnCompleteListener() {
			@Override
			public void onComplete(Object[] objects) {	
				imageIV.setImageBitmap((Bitmap) objects[0]);

			}					
		});	
		
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				IncomingContactLayout.this.setVisibility(View.GONE);
			}			
		});
		loadingRL.setVisibility(View.GONE);
		viewRL.setVisibility(View.VISIBLE);
	}
}
