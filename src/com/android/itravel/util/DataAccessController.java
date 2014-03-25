package com.android.itravel.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import model.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
}
