package com.akc.registrousuario;

import java.util.ArrayList;

import com.akc.registrousuario.models.Usuario;
import com.akc.registrousuario.models.usuarioDAO;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Registro extends Activity implements OnClickListener{

	private usuarioDAO dao;
	private TextView datos;
	private Button btn_Registro;
	private Button btn_Eliminar;
	//private Button btn_ListaUser;
	private Button btn_Update;
	private Cursor cursor;
	private String usuacons;
	private ArrayList<String> reg =  new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);
		
		Bundle b = new Bundle();
		b = getIntent().getExtras();
		usuacons = b.getString("usuacons");		
		Toast.makeText(this, "ID: "+usuacons, Toast.LENGTH_SHORT).show();
		
		btn_Registro = (Button)findViewById(R.id.btnNuvo);
		btn_Registro.setOnClickListener(this);
		
		btn_Eliminar = (Button)findViewById(R.id.btnEliminar);
		btn_Eliminar.setOnClickListener(this);
		
		btn_Update = (Button)findViewById(R.id.btnUpdate);
		btn_Update.setOnClickListener(this);
		
		datos = (TextView)findViewById(R.id.txtResultado);
		datos.setText("");
		
		if ( b != null ) {
			dao = new usuarioDAO(this);		
			//dao.abrir();
			cursor = dao.buscarUsuario(Long.parseLong(usuacons));
			reg = dao.getFormatListUniv(cursor);
			datos.setText(reg.get(0));
		}		 
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch ( v.getId() ){
		case R.id.btnNuvo:				
				intent.setClass(this, MainActivity.class);
				startActivity(intent);
			break;			
		case R.id.btnEliminar:
			String nombreUsuario = "";
			//String paisUsuario = "";
			//System.out.println("==> "+cursor.getColumnName(0)+" ==> "+cursor.getColumnIndex(cursor.getColumnName(0))+" ==> "+cursor.getColumnIndex("usuanomb"));
			int posicionColumnNombre = cursor.getColumnIndex("usuanomb");
			//int posicionColumnPais = cursor.getColumnIndex("usuapais");
						
			if (cursor.moveToFirst()) {
	            do {
	                nombreUsuario = cursor.getString(posicionColumnNombre);
	                //paisUsuario = cursor.getString(posicionColumnPais);
	            } while (cursor.moveToNext());
	        }
			
			//Recorremos el cursor for
			/*for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
			   nombreUsuario = cursor.getString(posicionColumnNombre);
			   paisUsuario = cursor.getString(posicionColumnPais);
			}*/
				
			this.showDialog(nombreUsuario);
			cursor.close();
			//Toast.makeText(this, "Clic ID = "+ Long.parseLong(usuacons)+" => "+nombreUsuario, Toast.LENGTH_SHORT).show();			
			break;
		case R.id.btnUpdate:						
			intent.setClass(this, UsuarioDetalle.class);
			intent.putExtra("usuacons", usuacons);
			startActivity(intent);	        
			break;
		}		
	}
	
	private void showDialog(String nombreUsuario){
	    String title = "Eliminar";
	    String message = "Esta seguro de eliminar el usuario => "+nombreUsuario;

	    AlertDialog.Builder ad = new AlertDialog.Builder(this);
	         ad.setTitle(title);
	         ad.setMessage(message);
	 
	         ad.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        	 public void onClick(DialogInterface dialog, int arg1) {
	        		 doPositiveClick();
	        		 
	        	 }
	         });
	 
	         ad.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
	        	 public void onClick(DialogInterface dialog, int arg1) {
	        		 doNegativeClick();
	        	 }
	         });
	         
	         ad.show();
	}
	
	public void doPositiveClick(){
		usuarioDAO dao = new usuarioDAO(this);
		int row = dao.eliminarUser(Long.parseLong(usuacons));
		
		if(row == 1){
			Intent intent = new Intent();
			intent.setClass(this, ListadoUser.class);
			startActivity(intent);
		}else{
			Toast.makeText(this, "Error al eliminar el usuario => "+Long.parseLong(usuacons), Toast.LENGTH_SHORT).show();
		}
		//System.out.println("ROW => "+row);
	    //Toast.makeText(this, "Ha pulsado OK", Toast.LENGTH_SHORT).show();
	}
	
	public void doNegativeClick(){
	    Toast.makeText(this, "Ha pulsado Cancelar", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registro, menu);
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
