package model;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/*
 * Classe utilitaire pour chercher la position du gps
 * Comme notre application n'utilise que le GPS de mani�re sporadique
 * on ne veux pas suivre le gps. On veux juste lui demander une position
 * � l'ocasion et se d�senregistrer du service
 * 
 * NB : ne pas utiliser cette classe pour suivre la position constament (ex. application de style gps)
 * 
 */
public class PositionUtil {
	//private Context appContext;
	private Location location = null;
	private LocationManager locmgr;
	
	public PositionUtil()
	{
		
		//Cherche la premi�re position gps pour initialiser l'objet
		updatePosition();
	}
	
	
	public void updatePosition()
	{
		//D�but l'update de location (elle sera arr�ter au premier update de location)
		locmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onLocationChange);
	}
 
	LocationListener onLocationChange=new LocationListener() {
			
		public void onLocationChanged(Location loc) {
			//sets and displays the lat/long when a location is provided
			location = locmgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
			//Arr�te de suivre la position
			locmgr.removeUpdates(onLocationChange);
		}
 
		public void onProviderDisabled(String provider) {
			// required for interface, not used
		}
 
		public void onProviderEnabled(String provider) {
			// required for interface, not used
		}
			
		@Override
		public void onStatusChanged(String provider, int status,
				Bundle extras) {
				// required for interface, not used
		}
	};
		
	
	
	
	
		    
	public double getLatitude() {
		return location.getLatitude();
	}
	
	public double getLongitude() {
		return location.getLongitude();
	}
	
	
	
}
