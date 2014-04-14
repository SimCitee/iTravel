package com.android.itravel.listadaptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.NameValuePair;

import model.Nouvelle;

import com.android.itravel.ListeNouvelles;
import com.android.itravel.R;
import com.android.itravel.R.id;
import com.android.itravel.R.layout;
import com.android.itravel.constant.DataURL;
import com.android.itravel.util.DataAccessController;
import com.android.itravel.util.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListeNouvellesAdapteur extends ArrayAdapter<LinkedHashMap<String, Nouvelle>> {
	
	private final ArrayList mData;
	
	public ListeNouvellesAdapteur(Context context, int resource, ArrayList<LinkedHashMap<String, Nouvelle>> data) {
		super(context, resource, data);
	
		mData = data;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		if (v == null) {
			LayoutInflater vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.list_post, null);
		}
		
		LinkedHashMap<String, Nouvelle> item = (LinkedHashMap<String, Nouvelle>) mData.get(position);
		Object key = item.keySet().iterator().next();
		Nouvelle n = item.get(key);
		
		if (n != null) {
			TextView nomContact = (TextView) v.findViewById(R.id.txtListeNomContact);
			TextView commentaire= (TextView) v.findViewById(R.id.txtListeCommentaire);
			TextView minute     = (TextView) v.findViewById(R.id.txtListePostTime);
			ImageView image		= (ImageView) v.findViewById(R.id.imvListeImage);
			
			
			if (minute != null) {
				minute.setText(n.getAffichageTemps());
			}
			
			if (nomContact != null) {
				nomContact.setText(n.getUtilisateur().getFullName());
			}
			
			if (commentaire != null) {
				commentaire.setText(n.getNouvelleTexte());
			}
			
			if (image != null) {
				// si la nouvelle contient une image
				if (!n.getImageId().isEmpty() && !n.getImageId().equalsIgnoreCase("")) {
					ImageLoader imgLoader = new ImageLoader(parent.getContext());
					
					Log.d("image url", DataURL.SERVER_URL+n.getImageId());
					
					imgLoader.DisplayImage(DataURL.SERVER_URL+n.getImageId(), image);
				}
			}
		
		}
		
		return v;
	}
	
}
