package com.android.itravel;

import java.util.ArrayList;

import model.ListeNouvellesAdapteur;
import model.Nouvelle;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class ListeNouvelles extends Activity {

	private ListView vListe;
	private ListeNouvellesAdapteur adapteur;
	private ArrayList<Nouvelle> liste;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liste_nouvelles);
		
		initViews();
		
		liste  = new ArrayList<Nouvelle>();
		
		adapteur = new ListeNouvellesAdapteur(this, R.layout.list_post, liste);
              
        vListe.setAdapter(adapteur);
                
        Nouvelle Nouvelle1 = new Nouvelle("Tomates", "Acheter 5 tomates", R.drawable.ic_launcher);
        Nouvelle Nouvelle2 = new Nouvelle("Carottes", "Bio", R.drawable.ic_launcher);
        Nouvelle Nouvelle3 = new Nouvelle("Cereales", "Corn Flake", R.drawable.ic_launcher);
        Nouvelle Nouvelle4 = new Nouvelle("Lait", "Quebon", R.drawable.ic_launcher);
        Nouvelle Nouvelle5 = new Nouvelle("Pain", "Gadoua", R.drawable.ic_launcher);
        Nouvelle Nouvelle6 = new Nouvelle("Tomates", "Acheter 5 tomates", R.drawable.ic_launcher);
        Nouvelle Nouvelle7 = new Nouvelle("Carottes", "Bio", R.drawable.ic_launcher);
        Nouvelle Nouvelle8 = new Nouvelle("Cereales", "Corn Flake", R.drawable.ic_launcher);
        Nouvelle Nouvelle9 = new Nouvelle("Lait", "Quebon", R.drawable.ic_launcher);
        Nouvelle Nouvelle10 = new Nouvelle("Pain", "Gadoua", R.drawable.ic_launcher);
        Nouvelle Nouvelle11 = new Nouvelle("Tomates", "Acheter 5 tomates", R.drawable.ic_launcher);
        Nouvelle Nouvelle12 = new Nouvelle("Carottes", "Bio", R.drawable.ic_launcher);
        Nouvelle Nouvelle13 = new Nouvelle("Cereales", "Corn Flake", R.drawable.ic_launcher);
        Nouvelle Nouvelle14 = new Nouvelle("Lait", "Quebon", R.drawable.ic_launcher);
        Nouvelle Nouvelle15 = new Nouvelle("Pain", "Gadoua", R.drawable.ic_launcher);
		
        liste.add(Nouvelle1);
        liste.add(Nouvelle2);
        liste.add(Nouvelle3);
        liste.add(Nouvelle4);
        liste.add(Nouvelle5);
        liste.add(Nouvelle6);
        liste.add(Nouvelle7);
        liste.add(Nouvelle8);
        liste.add(Nouvelle9);
        liste.add(Nouvelle10);
        liste.add(Nouvelle11);
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.liste_nouvelles, menu);
		return true;
	}

	
	private void initViews() {
		vListe = (ListView)findViewById(R.id.lvListeNouvelles);
	}
}
