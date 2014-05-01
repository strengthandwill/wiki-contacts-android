package com.kahkong.wikicontacts.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.kahkong.wikicontacts.callback.OnCompleteListener;
import com.kahkong.wikicontacts.modal.CountryCode;
import com.kahkong.wikicontacts.modal.CountryCodesWithSelect;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class CountryCodeServiceImpl implements CountryCodeService {
	private static CountryCodeService instance;

	private WebService webService = WebServiceImpl.getInstance();

	private CountryCodeServiceImpl() {	
	}

	public synchronized static CountryCodeService getInstance() {
		if (instance==null) {
			instance = new CountryCodeServiceImpl();
		}
		return instance;
	}
	
	public void listCountryCodes(final OnCompleteListener onCompleteListener) {
		webService.listCountryCodes(new OnCompleteListener() {
			@Override
			public void onComplete(Object[] objects) {										
				try {
					InputStream inputStream = (InputStream) objects[0];	
					if (inputStream==null || inputStream.available()==0) {
						onCompleteListener.onComplete(new Object[] {null});
						return;
					}
					ObjectMapper objectMapper = new ObjectMapper();
					List<CountryCode> countryCodes = objectMapper.readValue(inputStream, new TypeReference<List<CountryCode>>(){});
					onCompleteListener.onComplete(new Object[] {countryCodes});									
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
	
	public void listAndSelectCountryCodes(final OnCompleteListener onCompleteListener) {
		webService.listAndSelectCountryCodes(new OnCompleteListener() {
			@Override
			public void onComplete(Object[] objects) {										
				try {
					InputStream inputStream = (InputStream) objects[0];	
					if (inputStream==null || inputStream.available()==0) {
						onCompleteListener.onComplete(new Object[] {null});
						return;
					}
					ObjectMapper objectMapper = new ObjectMapper();
					CountryCodesWithSelect countryCodesWithSelect = objectMapper.readValue(inputStream, CountryCodesWithSelect.class);					
					onCompleteListener.onComplete(new Object[] {countryCodesWithSelect.getCountryCodes(), countryCodesWithSelect.getSelect()});									
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
}
