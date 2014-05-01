package com.kahkong.wikicontacts.form;

import java.io.InputStream;

import com.kahkong.wikicontacts.R;
import com.kahkong.wikicontacts.callback.OnChangeListener;
import com.kahkong.wikicontacts.service.ImageService;
import com.kahkong.wikicontacts.service.ImageServiceImpl;
import com.kahkong.wikicontacts.service.IntentService;
import com.kahkong.wikicontacts.service.IntentServiceImpl;
import com.kahkong.wikicontacts.service.ResourceService;
import com.kahkong.wikicontacts.service.ResourceServiceImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class ImageUploadForm extends RelativeLayout implements OnChangeListener {
	private static enum State { NOIMAGE, HASIMAGE, CHANGED };
	private static final int REQUEST_CODE = (int) (Math.random()*Integer.MAX_VALUE);
	
	private ImageService imageService = ImageServiceImpl.getInstance();
	private ResourceService resourceService = ResourceServiceImpl.getInstance();
	private IntentService intentService = IntentServiceImpl.getInstance();
	
	private boolean spam = false;
	private Bitmap image = null;
	private State state = State.NOIMAGE;
	
	private ImageView imageIV;
	private Button selectBtn;
	private Button rotateBtn;

	public ImageUploadForm(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) {
			return;
		}
		
		inflate(context, R.layout.contact_edit_image, this);
		imageIV = (ImageView) findViewById(R.id.editImageViewIUF);
		selectBtn = (Button) findViewById(R.id.editSelectBtnIUF);
		rotateBtn = (Button) findViewById(R.id.editRotateBtnIUF);		
		rotateBtn.setVisibility(View.GONE);
		
		selectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {				
				intentService.startImageChooserActivity(REQUEST_CODE);
			}			
		});
		
		rotateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				image = imageService.rotateImage(image);
				imageIV.setImageBitmap(image);
				state = State.CHANGED;
			}			
		});
	}
	
	public int getRequestCode() {
		return REQUEST_CODE;
	}
	
	public void setImage(Bitmap image, boolean spam) {
		this.image = image;
		this.spam = spam;		
		if (image!=null) {
			state = State.HASIMAGE;
		} else {
			state = State.NOIMAGE;
		}		
		updateImage();
	}
	
	public void setImage(InputStream inputStream) {
		if (inputStream==null) {
			return;
		}

		if (image!=null) {
			image.recycle();
		}
		image = imageService.processImage(inputStream, 100);
		imageIV.setImageBitmap(image);
		state = State.CHANGED;
		rotateBtn.setVisibility(View.VISIBLE);
	}	
	
	public Bitmap getImage() {
		if (spam || state!=State.CHANGED) {
			return null;
		}
		return image;
	}

	@Override
	public void onChange(Object object) {	
		this.spam = (Boolean) object;	
		updateImage();
	}
	
	private void updateImage() {
		if (!spam) {
			if (state==State.NOIMAGE) {
				imageIV.setImageBitmap(resourceService.getNewImage());
				rotateBtn.setVisibility(View.GONE);
			} else {
				imageIV.setImageBitmap(image);
				rotateBtn.setVisibility(View.VISIBLE);				
			}
			selectBtn.setVisibility(View.VISIBLE);
		} else { // spam
			imageIV.setImageBitmap(resourceService.getSpamImage());
			selectBtn.setVisibility(View.GONE);
			rotateBtn.setVisibility(View.GONE);
		}		
	}	
}
