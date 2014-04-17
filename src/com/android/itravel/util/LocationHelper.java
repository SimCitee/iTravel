package com.android.itravel.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

public class LocationHelper {
	
	public static HashMap<String,String> getAddress(Context context, double latitude, double longitude){
	
		ConnectionDetector cd = new ConnectionDetector(context);
		LocationManager locmgr = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

		if(locmgr.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null && cd.isConnectingToInternet())
		{
		
	        try{
	            Geocoder gcd = new Geocoder(context, Locale.getDefault());
	            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
	            
	            if (addresses.size() > 0) {	  
	            	HashMap<String,String> data = new HashMap<String,String>();
	            	
	                for(int i = 0; i < addresses.size(); i++){
	                	
	                	data.put("ville", addresses.get(i).getLocality());
	                	data.put("pays", addresses.get(i).getCountryName());
	                }
	                
	                return data;
	            }
	        }
	        catch(Exception e){
	            e.printStackTrace();
	            return null;
	        }
		}
		
		return null;
    };
}
