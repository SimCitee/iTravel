package com.android.itravel.listadaptor;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import model.Commentaire;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.itravel.R;

public class ListeCommentairesAdapteur extends ArrayAdapter<LinkedHashMap<String, Commentaire>> {
	
	private final ArrayList data;
	
	public ListeCommentairesAdapteur(Context context, int resource, ArrayList<LinkedHashMap<String, Commentaire>> data) {
		super(context, resource, data);
	
		this.data = data;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		if (v == null) {
			LayoutInflater vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.liste_commentaire, null);
		}
		
		LinkedHashMap<String, Commentaire> item = (LinkedHashMap<String, Commentaire>) data.get(position);
		Object key = item.keySet().iterator().next();
		Commentaire c = item.get(key);
		
		if (c != null) {
			TextView dateHeure = (TextView) v.findViewById(R.id.txtDetailListeDateHeure);
			TextView commentaire= (TextView) v.findViewById(R.id.txtDetailListeCommentaire);
			
			if (commentaire != null) {
				commentaire.setText(c.getCommentaireTexte());
			}
			
			if (dateHeure != null) {
				dateHeure.setText(c.getDateEmission());
			}
		
		}
		
		return v;
	}
	
}