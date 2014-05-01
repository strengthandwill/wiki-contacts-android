package com.kahkong.wikicontacts.service;

import java.io.InputStream;

import android.graphics.Bitmap;

import com.kahkong.wikicontacts.callback.OnCompleteListener;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public interface ImageService {
	public void getImage(boolean isSpam, String url, OnCompleteListener onCompleteListener);
	
	public Bitmap processImage(InputStream inputStream, int size);
	
	public Bitmap rotateImage(Bitmap image);
	
	public byte[] bitmapToBytes(Bitmap image);
}
