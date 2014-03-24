package com.android.itravel;

import com.android.itravel.R;

import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Calendar;


import model.EnvironmentVariables;
import model.ExifPositionUtil;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.widget.ImageButton;


public class MesNouvellesAjouter extends Activity {
	
	
	private final static String DEBUG_TAG = "MakePhotoActivity";
	private Camera camera;
	//private int cameraId = 0;
	  
	private File photoFile = null;		//Fichier d'image physique
	private Bitmap photo = null;		//Bitmap pour afficher en miniature
	//private Date imageDate = null;
	Timestamp photoTimestamp = null;
	
	private static final int CAMERA_REQUEST_CODE = 1;
	
	//private Bitmap bitmap;	//Image prise par l'utilisateur
	
	private ImageView i_image = null;
	private EditText e_comment = null;
	private TextView t_position = null;
	private Double photoLatitude = null;
	private Double photoLongitude = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		ImageButton takePicture;
		Button save;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mes_nouvelles_ajouter);
		
	///////////////////	
/*
		// do we have a camera?
	    if (!getPackageManager()
	        .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
	      Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
	          .show();
	    } else {
	      cameraId = findFrontFacingCamera();
	      if (cameraId < 0) {
	        Toast.makeText(this, "No front facing camera found.",
	            Toast.LENGTH_LONG).show();
	      } else {
	        camera = Camera.open(cameraId);
	      }
	    }
*/
	//////////////////    
	    
		
		i_image = (ImageView)findViewById(R.id.mes_nouvelles_ajouter_image);
		e_comment  = (EditText)findViewById(R.id.mes_nouvelles_ajouter_commentaire_text);
		t_position = (TextView)findViewById(R.id.mes_nouvelles_ajouter_position_textview);
		
		
		
		Intent intent = getIntent();
			
		//String title = intent.getStringExtra("title");
		//String description = intent.getStringExtra("description");
		//String icon_id = intent.getStringExtra("icon_id");
		
		//e_title.setText(title);
		//e_desc.setText(description);
		
		takePicture = (ImageButton)findViewById(R.id.mes_nouvelles_ajouter_photo_button);
		takePicture.setOnClickListener(onPictureTaken);
		
		save = (Button)findViewById(R.id.mes_nouvelles_ajouter_sauvegarder_button);
		save.setOnClickListener(onSave);
		
		
		
		
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
/*
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
*/
		return true;

	}
	
	
	private View.OnClickListener onPictureTaken = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
			
/*
			String path=Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
			File file = new File(path,"test111111111.jpg");
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Uri outputFileUri = Uri.fromFile(file);
	*/		
			
			
			//Vérifie si le folder de l'application existe
			File imageDirectory = new File(Environment.getExternalStorageDirectory() + EnvironmentVariables.IMAGE_FOLDER);
			if(!imageDirectory.isDirectory()) {
				Log.i("", "Folder NOT found : " + Environment.getExternalStorageDirectory() + EnvironmentVariables.IMAGE_FOLDER);
				
				//Crée le dossier d'image de l'application
				imageDirectory.mkdir();
			}
			else
			{
				Log.i("", "Folder found : " + Environment.getExternalStorageDirectory() + EnvironmentVariables.IMAGE_FOLDER);
			}
			
			
			//Chemin où sont stocké les images
			String path=Environment.getExternalStorageDirectory().getPath() + EnvironmentVariables.IMAGE_FOLDER;
			
			//Timestamp de la photo (utilisé seulement comme id, voir plus bas)
			Calendar calendar = Calendar.getInstance();
			photoTimestamp = new Timestamp(calendar.getTime().getTime());
			
			//Formatage pour donner un identifiant à l'image
			//NB : quand le serveur sera implanté, ce sera lui qui définira l'identifiant de l'image
			//Champ incrémental de la base de données
			String strTimestamp = photoTimestamp.toString();
			strTimestamp = strTimestamp.replaceAll("[\\s\\-.:]", "");
			
			//Nommage du fichier d'image
			photoFile = new File(path, strTimestamp + ".jpg");
			
			//Crée le fichier d'image (vide) sur le disque 
			try {
				photoFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	
			Uri outputFileUri = Uri.fromFile(photoFile);
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

			startActivityForResult(intent, CAMERA_REQUEST_CODE);
		    
		    
		}

	};
	
	
	private View.OnClickListener onSave = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
	
			Intent intent = getIntent();
			
			Calendar calendar = Calendar.getInstance();
			photoTimestamp = new Timestamp(calendar.getTime().getTime());

			
			String strTimestamp = photoTimestamp.toString();
			strTimestamp = strTimestamp.replaceAll("[\\s\\-.:]", "");

			//Identifiant de la nouvelle (il provindra éventuellement du serveur)
			intent.putExtra("nouvelle_id", Long.parseLong(strTimestamp));
			//photoTimestamp = new Timestamp(calendar.getTime().getTime());
			
			//Si aucune photo n'a été prise
			if(photoFile == null)
			{
				
				intent.putExtra("id_image", "0"); //Pas d'image de fournit
			}
			else
			{
				intent.putExtra("id_image", (String) photoFile.getName());
				
			}
			
			//Commentaire
			if(e_comment.getText().length() > 0)
			{
	Log.i("", "Le texte : " + e_comment.getText());
				intent.putExtra("commentaire", e_comment.getText().toString());
			}
			else
			{
				Log.i("", "Pas de texte");
				intent.putExtra("commentaire", "");
			}
			
			//Si les coordonnées ne peuvent être prise dans la photo
			if(photoLatitude == null || photoLongitude == null)
			{
				Double d = 22.22;
				//TODO implanter la recherche des coordonnées du GPS
				intent.putExtra("latitude", d);
				intent.putExtra("longitude", d);
			}
			else
			{
				
				//Coordonnée prise sur la photo
				intent.putExtra("latitude", photoLatitude);
				intent.putExtra("longitude", photoLongitude);
			}
			
			
			intent.putExtra("date",  String.valueOf(calendar.get(Calendar.DATE)));
			intent.putExtra("heure",  String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
		
			setResult(RESULT_OK, intent);
						
			finish();
		}

	};
	
/*	
	@Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		 InputStream stream = null;
		 if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK)
		 {
			 try
			 {
				 stream = getContentResolver().openInputStream(data.getData());
				 bitmap = BitmapFactory.decodeStream(stream);

				 i_image.setImageBitmap(bitmap);
		     
			 }catch(FileNotFoundException e)
			 {
				 e.printStackTrace();
				 
			 }
			 finally
			 {
				 if (stream != null)
				 {
					 try
					 {
						 stream.close();
					 }
					 catch(IOException f)
					 {
						 f.printStackTrace();
					 }
				 }
			 }
		 }
	  }
	  
	  
	*/
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		 InputStream stream = null;
		 if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK)
		 {
		
			 Log.i("", "Longeur de l'image " + photoFile.length());
			 
			 
			 //photo = (Bitmap) data.getExtras().get("data");
			 //i_image.setImageBitmap(photo);
			 
			 
			 Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
			 i_image.setImageBitmap(myBitmap);
			 
			 
			 ExifInterface exif;
			try {
				exif = new ExifInterface(photoFile.getAbsolutePath());
				
				ExifPositionUtil exifPositionUtil = new ExifPositionUtil(exif);
				
				//Garde en mémoire la latitude/longitude de l'image (si activé)
				photoLatitude =  exifPositionUtil.getLatitude();
				photoLongitude = exifPositionUtil.getLongitude();
				
				if(photoLatitude != null && photoLongitude != null)
				{
					//Écrit la position dans le textview
					t_position.setText(photoLatitude.toString() + ", " + photoLongitude.toString());
				}
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			 
			 
			 Log.i("", "Longeur de l'image " + photoFile.length());
			 
		 /*    


		     // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
		     Uri tempUri = getImageUri(getApplicationContext(), photo);

		     // CALL THIS METHOD TO GET THE ACTUAL PATH
		     File finalFile = new File(getRealPathFromURI(tempUri));

		     Log.i("", "Filenameeee : " + getRealPathFromURI(tempUri));
		     
		     
		*/	 
			 
			 /*
			 try
			 {
				 stream = getContentResolver().openInputStream(data.getData());
				 bitmap = BitmapFactory.decodeStream(stream);

				 i_image.setImageBitmap(bitmap);
		     
			 }catch(FileNotFoundException e)
			 {
				 e.printStackTrace();
				 
			 }
			 finally
			 {
				 if (stream != null)
				 {
					 try
					 {
						 stream.close();
					 }
					 catch(IOException f)
					 {
						 f.printStackTrace();
					 }
				 }
			 }
			 
			 */
		 }
		 
		 //Opération annulée
		 else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED)
		 {
			 //Supprime le fichier d'image si l'opération de prise de photo a été annulée
			 Log.i("", "Longeur de l'image " + photoFile.length());
			 photoFile.delete();
			 photoFile = null;
			 
		 }
	  }
	
	
	private int findFrontFacingCamera() {
	    int cameraId = -1;
	    // Search for the front facing camera
	    int numberOfCameras = Camera.getNumberOfCameras();
	    for (int i = 0; i < numberOfCameras; i++) {
	      CameraInfo info = new CameraInfo();
	      Camera.getCameraInfo(i, info);
	      if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
	        Log.d(DEBUG_TAG, "Camera found");
	        cameraId = i;
	        break;
	      }
	    }
	    return cameraId;
	  }
	
	@Override
	  protected void onPause() {
	    if (camera != null) {
	      camera.release();
	      camera = null;
	    }
	    super.onPause();
	  }
	
	
	public Uri getImageUri(Context inContext, Bitmap inImage) {
	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	    String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
	    return Uri.parse(path);
	}

	public String getRealPathFromURI(Uri uri) {
	    Cursor cursor = getContentResolver().query(uri, null, null, null, null); 
	    cursor.moveToFirst(); 
	    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
	    return cursor.getString(idx); 
	}
	
	
	/*
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	    	if(photo != null){
	    		i_image.setImageBitmap(photo);
	    	}
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	    	
	    	if(photo != null){
	    		i_image.setImageBitmap(photo);
	    	}
	    }
	}
	
	*/
}
