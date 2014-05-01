package com.kahkong.wikicontacts.subcontroller;

import com.kahkong.wikicontacts.R;
import com.kahkong.wikicontacts.callback.OnCompleteListener;
import com.kahkong.wikicontacts.callback.OnEventListener;
import com.kahkong.wikicontacts.link.EmailLink;
import com.kahkong.wikicontacts.link.NumberLink;
import com.kahkong.wikicontacts.link.TagLink;
import com.kahkong.wikicontacts.link.UrlLink;
import com.kahkong.wikicontacts.modal.ContactAndImage;
import com.kahkong.wikicontacts.service.ImageService;
import com.kahkong.wikicontacts.service.ImageServiceImpl;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class ViewContactDialog extends Dialog {
	private ImageService imageService = ImageServiceImpl.getInstance();
	
	public static enum Event {EDIT_CLICK, TAG_CLICK};
	
	private Context context;
	private ContactAndImage contactAndImage;
	private OnEventListener onEventListener;

	private TableRow numberTR;
	private TableRow emailTR;
	private TableRow descriptionTR;
	private TableRow tagsTR;
	private TableRow addressTR;
	private TableRow websiteTR;	

	private TextView nameTV;
	private FlowLayout numberFL;
	private FlowLayout emailFL;
	private TextView descriptionTV;
	private FlowLayout tagsFL;
	private TextView addressTV;
	private LinearLayout websiteLL;
	private ImageView imageIV;
	private Button editBtn;
	private Button closeBtn;

	public ViewContactDialog(Context context) {
		super(context, R.style.dialogLightNoTitleBar);
	}

	public ViewContactDialog(Context context, final ContactAndImage contactAndImage, final OnEventListener onEventListener) {
		super(context, R.style.dialogLightNoTitleBar);		
		if (contactAndImage==null) {
			return;
		}		
		setContentView(R.layout.contact_view);
		this.context = context;
		this.contactAndImage = contactAndImage;
		this.onEventListener = onEventListener;
		
		numberTR = (TableRow) findViewById(R.id.viewNumberTR);
		emailTR = (TableRow) findViewById(R.id.viewEmailTR);
		descriptionTR = (TableRow) findViewById(R.id.viewDescriptionTR);
		tagsTR = (TableRow) findViewById(R.id.viewTagsTR);
		addressTR = (TableRow) findViewById(R.id.viewAddressTR);
		websiteTR = (TableRow) findViewById(R.id.viewWebsiteTR);

		nameTV = (TextView) findViewById(R.id.viewNameTV);
		numberFL = (FlowLayout) findViewById(R.id.viewNumberFL);
		emailFL = (FlowLayout) findViewById(R.id.viewEmailFL);
		descriptionTV = (TextView) findViewById(R.id.viewDescriptionTV);
		tagsFL = (FlowLayout) findViewById(R.id.viewTagsFL);
		addressTV = (TextView) findViewById(R.id.viewAddressTV);
		websiteLL = (LinearLayout) findViewById(R.id.viewWebsiteLL);	    
		imageIV = (ImageView) findViewById(R.id.viewImageIV);
		editBtn = (Button) findViewById(R.id.viewEditBtn);
		closeBtn = (Button) findViewById(R.id.viewCloseBtn);

		init();		
	}
	
	private void init() {
		nameTV.setText(contactAndImage.getName());
		if (contactAndImage.getNumber()!=null) {
			TextView textView = new TextView(context);			
			textView.setText("(" + contactAndImage.getCountryCode().subSequence(0, 2) + ")");
			textView.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Medium);
			textView.setPadding(0, 0, 5, 0);
			numberFL.addView(textView);
			String[] number = contactAndImage.getNumber();
			String countryCode = contactAndImage.getCountryCode().substring(2);
			for (int i=0; i<number.length; i++) {
				numberFL.addView(new NumberLink(context, number[i], countryCode));
			}			
		} else {
			numberTR.setVisibility(View.GONE);
		}
		if (contactAndImage.getEmail()!=null) {
			String[] email = contactAndImage.getEmail();
			for (int i=0; i<email.length; i++) {
				emailFL.addView(new EmailLink(context, email[i]));
			}
		} else {
			emailTR.setVisibility(View.GONE);
		}	

		if (contactAndImage.getDescription()!=null) {
			descriptionTV.setText(contactAndImage.getDescription());	
		} else {
			descriptionTR.setVisibility(View.GONE);
		}
		if (contactAndImage.getTags()!=null) {
			String[] tags = contactAndImage.getTags();
			for (int i=0; i<tags.length; i++) {
				tagsFL.addView(new TagLink(context, tags[i], new com.kahkong.wikicontacts.callback.OnClickListener() {
					@Override
					public void onClick(Object object) {
						onEventListener.onEvent(Event.TAG_CLICK, object);
						dismiss();
					}					
				}));				
			}	
		} else {
			tagsTR.setVisibility(View.GONE);
		}
		if (contactAndImage.getAddress()!=null) {
			addressTV.setText(contactAndImage.getAddress());	
		} else {
			addressTR.setVisibility(View.GONE);
		}
		if (contactAndImage.getWebsite()!=null) {
			websiteLL.addView(new UrlLink(context, contactAndImage.getWebsite()));
		} else {
			websiteTR.setVisibility(View.GONE);
		}
		if (contactAndImage.getImage()!=null) {
			imageIV.setImageBitmap(contactAndImage.getImage());
		} else {
			imageService.getImage(contactAndImage.isSpam(), contactAndImage.getImageUrl(), new OnCompleteListener() {
				@Override
				public void onComplete(Object[] objects) {	
					Bitmap image = (Bitmap) objects[0];
					contactAndImage.setImage(image);
					imageIV.setImageBitmap(image);
				}					
			});	
		}

		editBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onEventListener.onEvent(Event.EDIT_CLICK, null);
				ViewContactDialog.this.dismiss();
			}		
		});

		closeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ViewContactDialog.this.dismiss();
			}		
		});
	}
}
