package common.bladefury.us;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SimpleHttpClient {
	static final int URL_CONNECTION_TIMEOUT = 8000;
	static final int URL_CONNECTION_READ_TIMEOUT = 30000;
	
	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public static String get(String url)
	{
		HttpClient httpclient = new DefaultHttpClient();
		final HttpParams http_params = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(http_params, URL_CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(http_params, URL_CONNECTION_READ_TIMEOUT);

		// Prepare a request object
		HttpGet httpget = new HttpGet(url);
		
		String result = null;

		// Execute the request
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			// Examine the response status
			Log.i("Praeda",response.getStatusLine().toString());

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			if (entity != null) {

				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				result= convertStreamToString(instream);
				// Closing the input stream will trigger connection release
				instream.close();
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static JSONObject getJSON(String url) {
		String result_str = get(url);
		JSONObject json = null;
		try {
			json = new JSONObject(result_str);
		}
		catch (JSONException e) {
			
		}
		return json;
	}
	
	public static String post(String url, String payload) {
		HttpClient httpclient = new DefaultHttpClient();
		final HttpParams http_params = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(http_params, URL_CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(http_params, URL_CONNECTION_READ_TIMEOUT);
		
		
		String result = null;
		
		HttpResponse response;
		try {
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(new StringEntity(payload));
			response = httpclient.execute(httppost);
			// Examine the response status
			Log.i("Praeda",response.getStatusLine().toString());

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			if (entity != null) {

				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				result= convertStreamToString(instream);
				// Closing the input stream will trigger connection release
				instream.close();
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
