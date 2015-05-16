package com.rezzcomm.reservation.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

public class NetRequest extends AsyncTask<String, String, String>{

	Activity activity;
	OnSuccessListener onSuccess=null;

	public NetRequest(Activity activity){
		this.activity=activity;
	}

	@Override
	protected String doInBackground(String... uri) {
		System.out.println("doInBackground");
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		try {
			response = httpclient.execute(new HttpGet(uri[0]));
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK && isNetworkAvailable()){

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			} else{
				//Closes the connection.
				response.getEntity().getContent().close();
				//throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			//TODO Handle problems..

		} catch (IOException e) {
			//TODO Handle problems..

		}

		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		//Do anything with response..
		if(result!=null&&isNetworkAvailable()){
			try{onSuccess.doSuccess(result);}
			catch(Exception e){};
		}
		else{
			try{onSuccess.doError();}
			catch(Exception e){};
		}
	}

	public void setOnSuccessListener(OnSuccessListener listener){
		onSuccess=listener;
	}

	public interface OnSuccessListener {
		public abstract void doSuccess(String result);
		public abstract void doError();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
			return true;
		}
		return false;
	}

}