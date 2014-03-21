package com.android.itravel;

import java.util.ArrayList;
import com.android.itravel.R;

import model.MesNouvellesListeAdapteur;
import model.Nouvelle;
import model.Utilisateur;
import model.UtilisateurActif;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class MesNouvelles extends Activity {
	
	static final int EDIT_ITEM_REQUEST = 10;
	static final int ADD_ITEM_REQUEST = 20;
	
	protected ActionMode mActionMode;
	private MesNouvellesListeAdapteur adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mes_nouvelles);
		
		
		//Set l'utilisateur actif
		Utilisateur u = new Utilisateur();
		u.setUtilisateurId(1000);
		u.setCourriel("pl@gmail.com");
		u.setNom("Handfield");
		u.setPrenom("Pierre-Luc");
		UtilisateurActif.getInstance().setUtilisateur(u);
		
		
		ArrayList<Nouvelle> myList = new ArrayList<Nouvelle>();
		
		Nouvelle n1 = new Nouvelle();
		Nouvelle n2 = new Nouvelle();
		Nouvelle n3 = new Nouvelle();
		
		n1.setNouvelleTexte("Commentaire de la nouvelle 1");
		n2.setNouvelleTexte("Commentaire de la nouvelle 2");
		n3.setNouvelleTexte("Commentaire de la nouvelle 3");
		n1.setImageId(0);
		n2.setImageId(0);
		n3.setImageId(0);
		
		//Liste d'items
		myList.add(n1);
		myList.add(n2);
		myList.add(n3);
		Log.e("dddd", "Test log");
		
		//1er param  : référence au contexte (toujours this)
		//2eme param : Activité qui définit le layout à utilisé
		//3eme param : Array ou liste d'objets utilisé pour créer la liste
		adapter = new MesNouvellesListeAdapteur(this, R.layout.liste_mes_nouvelles_layout, myList);
		
		//Cherche la liste à laquel on veut ajoute les données
		ListView listView = (ListView) findViewById(R.id.mesNouvelleslistView);
		 
		//Bind la liste avec l'adapter (qui contient le layout et les données)
		listView.setAdapter(adapter);
				
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
 
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long arg3) {     	
            	if (mActionMode != null) { 
		        	 return false; 
	        	 } 
				
	        	 mActionMode = startActionMode(mActionModeCallback); 
	        	 mActionMode.setTag(adapter.getItem(position));
	        	 view.setSelected(true); 
	        	 return true;
            }

        });

		
		///
		//getFragmentManager().beginTransaction()
        //.replace(android.R.id.content, new SettingsFragment())
        //.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mes_nouvelles_action_bar_no_action, menu);
		return true;
	}
	
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.mes_nouvelles_action_bar, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }


        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
 
            switch (item.getItemId()) {
            
                case R.id.mes_nouvelles_modifier:             
                	editSelectedItem((Nouvelle)mode.getTag(), adapter.getPosition((Nouvelle) mode.getTag()));
                    return true;
                
                case R.id.mes_nouvelle_supprimer :

                	deleteSelectedItem((Nouvelle)mode.getTag());
                	mode.finish();
                    return true;
             
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };
  
   
    private void deleteSelectedItem(Nouvelle itemSelected) {
    	adapter.remove(itemSelected);
    	adapter.notifyDataSetChanged();
    }
  
   
    private void editSelectedItem(Nouvelle nouvelle, int position) {
 	
    	Intent intent = new Intent(this, MesNouvellesModifier.class);

		intent.putExtra("id", String.valueOf(position));
		intent.putExtra("image_id", nouvelle.getImageId());
		intent.putExtra("commentaire", nouvelle.getNouvelleTexte());
		intent.putExtra("position_pays", nouvelle.getPays());
		intent.putExtra("position_ville", nouvelle.getVille());
		
		startActivityForResult(intent, EDIT_ITEM_REQUEST);
		

    }
    
 
   
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	
    	//Retour de modification
    	if(requestCode == EDIT_ITEM_REQUEST && resultCode == RESULT_OK)
    	{
 
    		Bundle dataMap = data.getExtras();
    	
    		Integer position = Integer.parseInt((String) dataMap.get("id"));
    		
    		//Integer imageId = Integer.parseInt((String) dataMap.get("image_id"));
    		
    		//TODO modifier pour vrai id de l'image
    		Integer imageId = 0;
    		
    		
    		String commentaire = (String) dataMap.get("commentaire");
    		
    		Nouvelle nouvelle = adapter.getItem(position);
    		nouvelle.setImageId(imageId);
    		nouvelle.setNouvelleTexte(commentaire);

    		adapter.notifyDataSetChanged();
    		
    		mActionMode.finish();
    		
    	}
    	
    	else{
    		
    		//Retour d'ajout
    		if(requestCode == ADD_ITEM_REQUEST && resultCode == RESULT_OK)
    		{
    			Bundle dataMap = data.getExtras();
   	    	
        		//Integer position = Integer.parseInt((String) dataMap.get("id"));
        		
        		
       		
        		//Integer imageId = Integer.parseInt((String) dataMap.get("image_id"));
        		
        		//TODO modifier pour vrai id de l'image
        		Integer imageId = 0;
        		
        		
        		String commentaire = (String) dataMap.get("commentaire");
        		
        		Nouvelle nouvelle = new Nouvelle();
        		nouvelle.setNouvelleId(1);
        		nouvelle.setNouvelleTexte(commentaire);
        		
        		adapter.add(nouvelle);
        		//Nouvelle nouvelle = adapter.getItem(position);
        		//nouvelle.setImageId(imageId);
        		//nouvelle.setNouvelleTexte(commentaire);

        		adapter.notifyDataSetChanged();
      		
    		}
    	}
    	
    }
    
   
   
    public boolean onPrepareOptionsMenu(Menu menu) {
        //  preparation code here
        return super.onPrepareOptionsMenu(menu);
    }
    
   
    //Sélection dans la barre de menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	//Ajout
        if (item.getItemId() == R.id.mes_nouvelles_actionbar_add_id) {
            //startActivity(new Intent(this, CoursesActivity.class));
        	
        	
        	startActivityForResult(new Intent(this, MesNouvellesAjouter.class), ADD_ITEM_REQUEST);
        	
        }
        
        
       
        
        return super.onOptionsItemSelected(item);
    }
  
}
