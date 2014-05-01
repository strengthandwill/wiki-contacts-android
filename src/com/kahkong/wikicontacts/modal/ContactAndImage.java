package com.kahkong.wikicontacts.modal;

import android.graphics.Bitmap;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class ContactAndImage extends Contact {
	protected Bitmap image = null;
	
	public ContactAndImage() {		
	}

	public ContactAndImage(Contact contact) {
		id = contact.id;
		name = contact.name;
		countryCode = contact.countryCode;
		number = contact.number;
		spam = contact.spam;
		description = contact.description;
		tags = contact.tags;
		email = contact.email;
		address= contact.address;
		website = contact.website;
		imageUrl = contact.imageUrl;	
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}
}
