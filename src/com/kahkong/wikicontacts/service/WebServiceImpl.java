package com.kahkong.wikicontacts.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import com.kahkong.wikicontacts.callback.OnCompleteListener;
import com.kahkong.wikicontacts.callback.OnTimeOutListener;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class WebServiceImpl implements WebService {
	private static final String TAG = "WebServiceImpl";
	private static WebService instance;
	
	private final int GET_TIMEOUT = 2500;
	private final int POST_TIMEOUT = 10000;

	private final String liveUrl = "http://wiki-contacts.com";
	private final String localhostUrl = "http://localhost";
	private final String localhostDeviceUrl = "http://192.168.1.69";	
	private final boolean live = true;

	private final String getContactQuery = "/getcontact";
	private final String getContactsQuery = "/getcontacts";
	private final String addContactAndImageQuery = "/addcontactandimage";
	private final String updateContactAndImageQuery = "/updatecontactandimage";
	private final String removeContactQuery = "/removecontact";
	private final String listCountryCodes = "/listcountrycodes";
	private final String listAndSelectCountryCodesUpdate = "/listandselectcountrycodes";

	private List<ConnectUrl> appConnUrls = new ArrayList<ConnectUrl>();
	private List<ConnectUrl> svcConnUrls = new ArrayList<ConnectUrl>();
	private String siteUrl;

	private Handler handler = new Handler();

	private WebServiceImpl() {
		if (live) {
			siteUrl = liveUrl;
		} else {
			siteUrl = localhostDeviceUrl;
		}
	}

	public synchronized static WebService getInstance() {
		if (instance==null) {
			instance = new WebServiceImpl();
		}
		return instance;
	}

	// application connection
	public void getContact(final String number, final OnCompleteListener onCompleteListener) {
		svcCancel();
		svcConnUrls.add(getConnectUrlToInputStream(siteUrl + getContactQuery + "/" + number, new OnTimeOutListener() {
			@Override
			public void onTimeOut() {
				getContact(number, onCompleteListener);			
			}			
		}, onCompleteListener));
	}

	public void addContactAndImage(String contactJson, byte[] imageBytes, OnCompleteListener onCompleteListener) {
		String url = siteUrl + addContactAndImageQuery;
		postConnectUrl(url, new String[] {"contact", "image"}, new Object[] {contactJson, imageBytes}, onCompleteListener);
	}	

	public void updateContactAndImage(String contactJson, byte[] imageBytes, OnCompleteListener onCompleteListener) {
		String url = siteUrl + updateContactAndImageQuery;
		postConnectUrl(url, new String[] {"contact", "image"}, new Object[] {contactJson, imageBytes}, onCompleteListener);
	}	
	
	public void removeContact(int id, final OnCompleteListener onCompleteListener) {
		appCancel();
		getConnectUrlToString(siteUrl + removeContactQuery + "/" + id, null, onCompleteListener);
	}
	
	public void getImageApp(String url, OnCompleteListener onCompleteListener) {
		appConnUrls.add(getConnectUrlToInputStream(url, null, onCompleteListener));
	}

	public void listCountryCodes(OnCompleteListener onCompleteListener) {
		appCancel();
		appConnUrls.add(getConnectUrlToInputStream(siteUrl + listCountryCodes, null, onCompleteListener));
	}	

	public void listAndSelectCountryCodes(OnCompleteListener onCompleteListener) {
		appCancel();
		appConnUrls.add(getConnectUrlToInputStream(siteUrl + listAndSelectCountryCodesUpdate, null, onCompleteListener));
	}
	
	// service connection
	public void getContacts(String query, OnCompleteListener onCompleteListener) {
		appCancel();
		appConnUrls.add(getConnectUrlToInputStream(siteUrl + getContactsQuery + "/" + query, null, onCompleteListener));
	}
	
	public void getImageSvc(String url, OnCompleteListener onCompleteListener) {
		svcConnUrls.add(getConnectUrlToInputStream(url, null, onCompleteListener));
	}

	// cancel connection
	public void appCancel() {
		Log.i("IncomingCall " + TAG, "application cancel");
		cancel(appConnUrls);
	}

	public void svcCancel() {
		Log.i("IncomingCall " + TAG, "service cancel");
		cancel(svcConnUrls);
	}

	private void cancel(List<ConnectUrl> connectUrls) {
		handler.removeCallbacksAndMessages(null);
		Iterator<ConnectUrl> iterator = connectUrls.iterator();
		while (iterator.hasNext()) {
			iterator.next().cancel();
		}
		connectUrls.clear();
	}

	// get connection post-processing
	private ConnectUrl getConnectUrlToInputStream(String url, OnTimeOutListener onTimeOutListener, final OnCompleteListener onCompleteListener) {
		return getConnectUrl(url, onTimeOutListener, onCompleteListener);
	}	

	private ConnectUrl getConnectUrlToString(String url, OnTimeOutListener onTimeOutListener, final OnCompleteListener onCompleteListener) {
		return getConnectUrl(url, onTimeOutListener, new OnCompleteListener() {
			@Override
			public void onComplete(Object[] objects) {
				String responseMsg = inputStreamToString((InputStream) objects[0]);
				onCompleteListener.onComplete(new String[] {responseMsg});
			}			
		});
	}	

	// get and post connection
	private ConnectUrl getConnectUrl(String url, OnTimeOutListener onTimeOutListener, OnCompleteListener onCompleteListener) {
		ConnectUrl connectUrl = new GetConnectUrl(url, onTimeOutListener, onCompleteListener);
		connectUrl.execute();
		return connectUrl;
	}

	private ConnectUrl postConnectUrl(String url, String[] name, Object[] data, OnCompleteListener onCompleteListener) {
		ConnectUrl connectUrl = new PostConnectUrl(url, name, data, onCompleteListener);
		connectUrl.execute();
		return connectUrl;
	}	

	private class GetConnectUrl extends AsyncTask<Void, Void, InputStream> implements ConnectUrl {
		private String urlStr;
		private OnCompleteListener onCompleteListener;
		private Runnable timeOut = null;

		private GetConnectUrl(final String urlStr, final OnTimeOutListener onTimeOutListener, final OnCompleteListener onCompleteListener) {
			if (!live) {
				this.urlStr = urlStr.replace(localhostUrl, localhostDeviceUrl);
			}						
			this.urlStr = urlStr.replaceAll(" ", "%20");
			this.onCompleteListener = onCompleteListener;
			if (onTimeOutListener!=null) {	
				timeOut = new Runnable() {
					@Override
					public void run() {
						Log.i("IncomingCall " + TAG, "connection timeout");
						GetConnectUrl.this.cancel(true);
						onTimeOutListener.onTimeOut();
					}					
				};
				handler.postDelayed(timeOut, GET_TIMEOUT);
			}
		}

		@Override
		protected InputStream doInBackground(Void... params) {
			try {								
				URL url = new URL(urlStr);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestProperty("Cache-Control", "no-cache");
				return copyInputStream(connection.getInputStream());
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;			
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} 
		}		

		@Override
		protected void onPostExecute(InputStream inputStream) {
			Log.i("IncomingCall " + TAG, "post execute");
			if (timeOut!=null) {
				handler.removeCallbacks(timeOut);
			}
			if (onCompleteListener!=null) {
				onCompleteListener.onComplete(new Object[] {inputStream});
			}
			super.onPostExecute(inputStream);
		}

		@Override
		public void execute() {
			super.execute();
		}

		@Override
		public void cancel() {
			if (timeOut!=null) {
				handler.removeCallbacks(timeOut);
			}			
			super.cancel(true);
		}
	}	

	private class PostConnectUrl extends AsyncTask<Void, Void, String> implements ConnectUrl {
		private String urlStr;
		private String[] name;
		private Object[] data;
		private OnCompleteListener onCompleteListener;

		private PostConnectUrl(String urlStr, String[] name, Object[] data, OnCompleteListener onCompleteListener) {
			if (!live) {
				urlStr = urlStr.replace(localhostUrl, localhostDeviceUrl);
			}						
			this.urlStr = urlStr.replaceAll(" ", "%20");
			this.name = name;
			this.data = data;
			this.onCompleteListener = onCompleteListener;
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				MultipartEntity multipartEntity = new MultipartEntity();				
				multipartEntity.addPart(name[0], new StringBody((String) data[0]));
				if (data[1]!=null) {
					multipartEntity.addPart(name[1], new ByteArrayBody((byte[]) data[1], name[1]));
				}				
				URL url = new URL(urlStr);				
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.setUseCaches(false);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setConnectTimeout(POST_TIMEOUT);
				connection.setReadTimeout(POST_TIMEOUT);

				connection.setRequestProperty("Cache-Control", "no-cache");
				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.addRequestProperty("Content-length", String.valueOf(multipartEntity.getContentLength()));
				connection.addRequestProperty(multipartEntity.getContentType().getName(), multipartEntity.getContentType().getValue());

				OutputStream outputStream = connection.getOutputStream();
				multipartEntity.writeTo(connection.getOutputStream());
				outputStream.close();
				connection.connect();				
				if (connection.getResponseCode()!=HttpURLConnection.HTTP_OK) {
					return null;
				}
				return inputStreamToString(connection.getInputStream());
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}	

		@Override
		protected void onPostExecute(String responseMsg) {	
			if (onCompleteListener!=null) {
				onCompleteListener.onComplete(new Object[] {responseMsg});
			}
			super.onPostExecute(responseMsg);
		}

		@Override
		public void execute() {
			super.execute();
		}

		@Override
		public void cancel() {			
			super.cancel(true);
		}		
	}

	private InputStream copyInputStream(InputStream inputStream) {
		if (inputStream==null) {
			return null;
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputStream.read(buffer)) > -1 ) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			return new ByteArrayInputStream(baos.toByteArray()); 			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	private String inputStreamToString(InputStream inputStream) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();		 
		String line;
		try {		 
			br = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}		
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}				
	}

	private interface ConnectUrl {
		public void execute();

		public void cancel();
	}
}
