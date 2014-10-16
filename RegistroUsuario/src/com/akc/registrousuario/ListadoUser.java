package com.akc.registrousuario;

import java.util.List;

import com.akc.registrousuario.models.Usuario;
import com.akc.registrousuario.models.usuarioDAO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListadoUser extends Activity implements OnItemClickListener {

	List<Usuario> values;
	private usuarioDAO dao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_lista);
		
		dao = new usuarioDAO(this);
	    dao.abrir();
	    
	    values = dao.getAllUser();
	    //System.out.println(values.get(0).getUsuaimge());
	 
	    Button agregarUser = (Button)findViewById(R.id.btnAgregarUserListaUser);
	    agregarUser.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MainActivity.class);
				startActivity(intent);
			}
		});
	    
		ListView NuestroListView = (ListView) findViewById(R.id.milistview);
		 
		//Creación del adaptador, vamos a escoger el layout
	    //simple_list_item_1, que los mostr			
	    //Asociamos el adaptador a la vista.
		final listaUserAdpatador adaptador = new listaUserAdpatador(this, values);
		NuestroListView.setAdapter(adaptador);
		
		NuestroListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {		
		Toast.makeText(this, "Clic ID = "+ values.get(arg2).get_id(), Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.setClass(this, Registro.class);
		intent.putExtra("usuacons", Long.toString(values.get(arg2).get_id()));
		startActivity(intent);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listado_user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
}
