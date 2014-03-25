package model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

/*
 * Classe utilitaire pour le travail (rotation) sur les bitmaps
 */

public class BitmapRotator {
	
	private String filePath;
	private Bitmap bitmap;
	
	
	public BitmapRotator(String filePath)
	{
		this.filePath = filePath;
		
		bitmap = BitmapFactory.decodeFile(filePath);
		
		
	}
	
	public void doRotaionIfNeeded()
	{
		ExifInterface exif2;
		try {
			exif2 = new ExifInterface(filePath);
			int exifOrientation = exif2.getAttributeInt(
	          ExifInterface.TAG_ORIENTATION,
	          ExifInterface.ORIENTATION_NORMAL);

	          int rotate = 0;

	          switch (exifOrientation) {
	          case ExifInterface.ORIENTATION_ROTATE_90:
	          rotate = 90;
	          break; 

	         case ExifInterface.ORIENTATION_ROTATE_180:
	         rotate = 180;
	         break;

	         case ExifInterface.ORIENTATION_ROTATE_270:
	         rotate = 270;
	         break;
	         }

	           if (rotate != 0) {
	          int w = bitmap.getWidth();
	          int h = bitmap.getHeight();

	          // Setting pre rotate
	          Matrix mtx = new Matrix();
	          mtx.preRotate(rotate);

	         // Rotating Bitmap & convert to ARGB_8888, required by tess
	          bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
	          bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
	          
	          
	       }
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
	}
	
	public Bitmap getBitmap()
	{
		return this.bitmap;
	}
	
	public void save()
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
		
		try
		{
			//you can create a new file name "test.jpg" in sdcard folder.
			File f = new File(filePath);
			f.createNewFile();
			//write the bytes in file
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
	
			// remember close de FileOutput
			fo.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
