package model;

/*
 * Crédit : http://android-er.blogspot.ca/2010/01/convert-exif-gps-info-to-degree-format.html
 */
import android.media.ExifInterface;

/*
 * Contient des méthodes pour récupérer la position gps d'une image (exif) en string
 * et la changer en double (position GPS)
 */
public class ExifPositionUtil {
	
	private ExifInterface exif;
	private double latitude;
	private double longitude;
	
	public ExifPositionUtil(ExifInterface exif)
	{
		String LATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
		String LATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
		String LONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
		String LONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
		
		if((LATITUDE !=null)
				&& (LATITUDE_REF !=null)
				&& (LONGITUDE != null)
				&& (LONGITUDE_REF !=null))
		{

		  if(LATITUDE_REF.equals("N")){
			  latitude = convertToDegree(LATITUDE);
		  }
		  else{
			  latitude = 0 - convertToDegree(LATITUDE);
		  }

		  if(LONGITUDE_REF.equals("E")){
			  longitude = convertToDegree(LONGITUDE);
		  }
		  else{
			  longitude = 0 - convertToDegree(LONGITUDE);
		  }

		}

	}
	
	public double getLatitude() {
		return latitude;
	}

	

	public double getLongitude() {
		return longitude;
	}
	
	@Override
	public String toString() {
	 
	 return (String.valueOf(latitude)
	   + ", "
	   + String.valueOf(longitude));
	}

	public int getLatitudeE6(){
		return (int)(latitude*1000000);
	}

	public int getLongitudeE6(){
		return (int)(longitude*1000000);
	}
	
	private Float convertToDegree(String stringDMS){
		Float result = null;
		String[] DMS = stringDMS.split(",", 3);

		String[] stringD = DMS[0].split("/", 2);
		Double D0 = new Double(stringD[0]);
		Double D1 = new Double(stringD[1]);
		Double FloatD = D0/D1;

		String[] stringM = DMS[1].split("/", 2);
		Double M0 = new Double(stringM[0]);
		Double M1 = new Double(stringM[1]);
		Double FloatM = M0/M1;
		  
		String[] stringS = DMS[2].split("/", 2);
		Double S0 = new Double(stringS[0]);
		Double S1 = new Double(stringS[1]);
		Double FloatS = S0/S1;
		  
		result = new Float(FloatD + (FloatM/60) + (FloatS/3600));
		  
		return result;
	}
}
