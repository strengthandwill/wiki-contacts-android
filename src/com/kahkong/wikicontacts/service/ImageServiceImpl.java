package com.kahkong.wikicontacts.service;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import com.kahkong.wikicontacts.callback.OnCompleteListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class ImageServiceImpl implements ImageService {
	private static ImageService instance;
	
	private WebService webService = WebServiceImpl.getInstance();
	private ResourceService resourceService = ResourceServiceImpl.getInstance();
	
	private ImageServiceImpl() {
	}

	public synchronized static ImageService getInstance() {
		if (instance==null) {
			instance = new ImageServiceImpl();
		}
		return instance;
	}
	
	public void getImage(boolean isSpam, String url, final OnCompleteListener onCompleteListener) {
		if (isSpam) { // is spam
			onCompleteListener.onComplete(new Object[] {resourceService.getSpamImage()});
		} else { // not spam
			if (url!=null) { // has image				
				webService.getImageApp(url, new OnCompleteListener() {
					@Override
					public void onComplete(Object[] objects) {
						onCompleteListener.onComplete(new Object[] {decodeImage((InputStream) objects[0])});
					}
				});
			} else { // no image
				onCompleteListener.onComplete(new Object[] {resourceService.getNewImage()});
			}
		}
	}
	
	public Bitmap processImage(InputStream inputStream, int size) {
		Bitmap image = decodeImage(inputStream);
		image = resizeImage(image, size);
		image = cropCenterImage(image);
		return image;
	}	
	
	private Bitmap decodeImage(InputStream inputStream) {
		if (inputStream==null) {
			return null;
		}		
		Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	private Bitmap cropCenterImage(Bitmap image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int size, x, y;
		if (width>height) { // landscape
			size = height;
			x = (width-height)/2;
			y = 0;
		} else { // portrait
			size = width;
			x = 0;
			y = (height-width)/2;
		}		
		Bitmap image2 = Bitmap.createBitmap(image, x, y, size, size);
		image.recycle();
		return image2;
	}
	
	private Bitmap resizeImage(Bitmap image, int size){
		int width = image.getWidth();
		int height = image.getHeight();
		int width2, height2;
		if (width>height) { // landscape
			width2 = (int) (((double) width)/height*size);
			height2 = size;
		} else { // portrait
			width2 = size;
			height2 = (int) (((double) height)/width*size);			
		}
		Bitmap image2 = Bitmap.createScaledBitmap(image, width2, height2, false); 
		image.recycle();
	    return image2;
	}
	
	public Bitmap rotateImage(Bitmap image) {
		Matrix matrix = new Matrix();
		matrix.setRotate(90);
		Bitmap image2 = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);
		image.recycle();
		return image2;
	}
	
	public byte[] bitmapToBytes(Bitmap image) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
		image.compress(CompressFormat.PNG, 100, outputStream); 
		return outputStream.toByteArray();
	}
}
