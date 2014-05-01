package com.kahkong.wikicontacts.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.graphics.Bitmap;
import android.util.Log;

import com.kahkong.wikicontacts.callback.OnCompleteListener;
import com.kahkong.wikicontacts.modal.Contact;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class ContactServiceImpl implements ContactService {
	private static final String TAG = "ContactServiceImpl";
	
	private static ContactService instance;
	private WebService webService = WebServiceImpl.getInstance();
	private ImageService imageService = ImageServiceImpl.getInstance();


	private ContactServiceImpl() {	
	}

	public synchronized static ContactService getInstance() {
		if (instance==null) {
			instance = new ContactServiceImpl();
		}
		return instance;
	}

	@Override
	public void getContact(String number, final OnCompleteListener onCompleteListener) {
		Log.i("IncomingCall " + TAG, "getting contact from web");
		webService.getContact(number, new OnCompleteListener() {
			@Override
			public void onComplete(Object[] objects) {										
				try {
					Log.i("IncomingCall " + TAG, "received contact from web");
					InputStream inputStream = (InputStream) objects[0];	
					if (inputStream==null || inputStream.available()==0) {
						onCompleteListener.onComplete(new Object[] {null});
						return;
					}
					ObjectMapper objectMapper = new ObjectMapper();
					final Contact contact = objectMapper.readValue(inputStream, Contact.class);					
					if (contact!=null) {
						onCompleteListener.onComplete(new Object[] {contact});
					} else {
						onCompleteListener.onComplete(new Object[] {null});
					}
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		});
	}	

	@Override
	public void getContacts(String query, final OnCompleteListener onCompleteListener) {
		webService.getContacts(query, new OnCompleteListener() {
			@Override
			public void onComplete(Object[] objects) {										
				try {
					InputStream inputStream = (InputStream) objects[0];	
					if (inputStream==null || inputStream.available()==0) {
						onCompleteListener.onComplete(new Object[] {null});
						return;
					}
					ObjectMapper objectMapper = new ObjectMapper();
					final List<Contact> contacts = objectMapper.readValue(inputStream, new TypeReference<List<Contact>>(){});
					onCompleteListener.onComplete(new Object[] {contacts});									
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		});
	}

	@Override
	public void addContactAndImage(Contact contact, Bitmap image, final OnCompleteListener onCompleteListener) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String contactJson = objectMapper.writeValueAsString(contact);
			byte[] imageBytes = null;
			if (image!=null) {
				imageBytes = imageService.bitmapToBytes(image);
			}
			webService.addContactAndImage(contactJson, imageBytes, new OnCompleteListener() {
				@Override
				public void onComplete(Object[] objects) {					
					String status = (String) objects[0];	
					if (status==null) {
						onCompleteListener.onComplete(new Object[] {Boolean.valueOf(false)});
						return;
					} else if (status.equals("success")) {
						onCompleteListener.onComplete(new Object[] {Boolean.valueOf(true)});
						return;
					} else { // status == "error"
						onCompleteListener.onComplete(new Object[] {Boolean.valueOf(false)});
						return;						
					}					
				}			
			});
		} catch (JsonGenerationException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}	
	
	@Override
	public void updateContactAndImage(Contact contact, Bitmap image, final OnCompleteListener onCompleteListener) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String contactJson = objectMapper.writeValueAsString(contact);
			byte[] imageBytes = null;
			if (image!=null) {
				imageBytes = imageService.bitmapToBytes(image);
			}
			webService.updateContactAndImage(contactJson, imageBytes, new OnCompleteListener() {
				@Override
				public void onComplete(Object[] objects) {					
					String status = (String) objects[0];	
					if (status==null) {
						onCompleteListener.onComplete(new Object[] {Boolean.valueOf(false)});
						return;
					} else if (status.equals("success")) {
						onCompleteListener.onComplete(new Object[] {Boolean.valueOf(true)});
						return;
					} else { // status == "error"
						onCompleteListener.onComplete(new Object[] {Boolean.valueOf(false)});
						return;						
					}					
				}			
			});
		} catch (JsonGenerationException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void removeContact(int id, final OnCompleteListener onCompleteListener) {
		webService.removeContact(id, new OnCompleteListener() {
			@Override
			public void onComplete(Object[] objects) {					
				String status = (String) objects[0];	
				if (status==null) {
					onCompleteListener.onComplete(new Object[] {Boolean.valueOf(false)});
					return;
				} else if (status.equals("success")) {
					onCompleteListener.onComplete(new Object[] {Boolean.valueOf(true)});
					return;
				} else { // status == "error"
					onCompleteListener.onComplete(new Object[] {Boolean.valueOf(false)});
					return;						
				}					
			}			
		});
	}}
