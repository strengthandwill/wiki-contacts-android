package com.kahkong.wikicontacts.modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class Contact implements Parcelable {
	// default value when empty
	// string: null
	// string[]: null
	// int: -1
	// boolean: false
	
	// sample value when not empty
	// string: "abc"
	// string[]: {"abc", "efg"}
	// int: 1
	// boolean: true
	
	protected int id = -1;
	protected String name = null;
	protected String[] tags = null;
	protected String countryCode = null;
	protected String[] number = null;
	protected boolean spam = false;
	protected String description = null;
	protected String[] email = null;
	protected String address= null;
	protected String website = null;
	protected String imageUrl = null;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String getCountryCode() {
		return countryCode;
	}
	
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public String[] getNumber() {
		return number;
	}
	
	public void setNumber(String[] number) {
		this.number = number;
	}
	
	public boolean isSpam() {
		return spam;
	}

	public void setSpam(boolean spam) {
		this.spam = spam;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String[] getEmail() {
		return email;
	}
	
	public void setEmail(String[] email) {
		this.email = email;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getWebsite() {
		return website;
	}
	
	public void setWebsite(String website) {
		this.website = website;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(countryCode);
		dest.writeStringArray(number);
		dest.writeStringArray(email);
		dest.writeInt(spam?1:0);
		dest.writeString(description);
		dest.writeStringArray(tags);
		dest.writeString(address);
		dest.writeString(website);
		dest.writeString(imageUrl);		
	}
	
	public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() { 
		@Override
		public Contact[] newArray(int size) {
			return null;
		}

		@Override
		public Contact createFromParcel(Parcel source) {
			Contact contact = new Contact();
			contact.id = source.readInt();
			contact.name = source.readString();
			contact.countryCode = source.readString();
			contact.number = source.createStringArray();
			contact.email = source.createStringArray();
			contact.spam = source.readInt()==1 ? true:false;
			contact.description = source.readString();
			contact.tags = source.createStringArray();
			contact.address = source.readString();
			contact.website = source.readString();
			contact.imageUrl = source.readString();
			return contact;
		}	
	};	
}
