package com.android.itravel.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.android.itravel.constant.EnvironmentVariables;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DataAccessController {
	private static DataAccessController instance = null;
	private static JSONParser jParser = new JSONParser();				// JSON Parser

	private DataAccessController() {
		
	}
	
	public static JSONObject getDataFromUrl(String url, String method,
            List<NameValuePair> params) {
    	
		return jParser.makeHttpRequest(url, method, params);
	}
	
	public static Bitmap getBitmapFromURL(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static JSONObject securePost(String url, String method,
            List<NameValuePair> params, String courriel, String mdp) {
		
		if (!courriel.isEmpty() && !mdp.isEmpty()) {
			SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	String timestamp = localDateFormat.format( new Date());
	    	String token = EnvironmentVariables.HASH_TOKEN;  
	    	String hash = "";

	    	// no whitespace
	    	timestamp = timestamp.replace(" ", "_");
	    	
	    	// ajout des parametres de securite
	    	params.add(new BasicNameValuePair("courriel", courriel));
	    	params.add(new BasicNameValuePair("tt", timestamp));
			
			for(NameValuePair v : params) {
				String key = v.getName();
				String value = v.getValue();
				hash += key + value;
			}
			
			// ajout du mot de passe a la chaine de hashage
	    	hash += mdp + courriel + EnvironmentVariables.HASH_TOKEN;
	    		    	
	    	// create hash string
	    	hash = Encryption.hashSHA256(hash);
	    	params.add(new BasicNameValuePair("hstr", hash));
	    	
	    	return jParser.makeHttpRequest(url, method, params);
		}
		
		return null;
	}
}
