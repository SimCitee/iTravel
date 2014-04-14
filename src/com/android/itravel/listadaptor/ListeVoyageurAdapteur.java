package com.android.itravel.listadaptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import model.Utilisateur;

import com.android.itravel.ListeNouvelles;
import com.android.itravel.R;
import com.android.itravel.R.id;
import com.android.itravel.R.layout;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListeVoyageurAdapteur extends ArrayAdapter<LinkedHashMap<String, Utilisateur>> {
	
	private final ArrayList mData;
	
	public ListeVoyageurAdapteur(Context context, int resource, ArrayList<LinkedHashMap<String, Utilisateur>> data) {
		super(context, resource, data);
	
		mData = data;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		if (v == null) {
			LayoutInflater vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.list_post, null);
		}
		
		LinkedHashMap<String, Utilisateur> item = (LinkedHashMap<String, Utilisateur>) mData.get(position);
		Object key = item.keySet().iterator().next();
		Utilisateur u = item.get(key);
		
		if (u != null) {
			TextView nomContact = (TextView) v.findViewById(R.id.txtListeNomContact);
			
			if (nomContact != null) {
				nomContact.setText(u.getPrenom() +" " + u.getNom());
			}
		
		}
		
		return v;
	}
}
