package com.akc.registrousuario;

import java.io.File;
import java.util.Arrays;

import com.akc.registrousuario.models.Usuario;
import com.akc.registrousuario.models.usuarioDAO;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UsuarioDetalle extends Activity implements OnClickListener{
	private int 	mYear;
    private int 	mMonth;
    private int 	mDay;    
    private String 	usuacons = null;
	
	private TextView 	nombre;
	private TextView 	fechaNacimiento;
	private Spinner 	spnPaises;
	private RadioGroup 	sexo;
	private CheckBox 	hablaInlges;
	private Button 		botonActualizar;
	private Button 		botonVerReg;
	private Button 		botonNuevoDet;
	private String 		ruta_imagen;
	private int SELECCIONAR_IMAGEN = 237487;
	private ImageView imagenUsuario;
	
	final BitmapFactory.Options options = new BitmapFactory.Options();

	//DatePickerDialog
		private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;		             
				fechaNacimiento.setText( ((mDay<10)?"0"+mDay:mDay) + "/" + ((mMonth<10)?"0"+mMonth:mMonth) + "/" + mYear );
			}
		};
		
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usuario_detalle);
		
		Bundle b = new Bundle();
		b = getIntent().getExtras();
		usuacons = b.getString("usuacons");	
		System.out.println("key => "+usuacons);
		
		if ( b != null ) {
			Usuario usuario = new Usuario();
			usuarioDAO dao = new usuarioDAO(getApplicationContext());			
	        usuario = dao.getStudentById(Long.parseLong(usuacons));

	        System.out.println(usuario.getUsuanomb());
	        System.out.println(usuario.getUsuafena());
	        System.out.println(usuario.getUsuapais());
	        System.out.println(usuario.getUsuasexo());
	        System.out.println(usuario.getUsuahain());
			
	        nombre 	= (TextView)findViewById(R.id.txtUserDet);
	        nombre.setText(usuario.getUsuanomb());
	        
	        fechaNacimiento = (TextView)findViewById(R.id.txtFechaNacDet);
	        fechaNacimiento.setText(usuario.getUsuafena());
	        fechaNacimiento.setOnClickListener(this);
	        
	        String[] arrayPaises 		= getResources().getStringArray(R.array.paises);
			spnPaises 					= (Spinner) findViewById(R.id.spnpaisesDet);
			ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayPaises );
			spnPaises.setAdapter(adapter);
			spnPaises.setSelection(Arrays.asList(arrayPaises).indexOf(usuario.getUsuapais()));
			
			sexo = (RadioGroup)findViewById(R.id.rgrsexoDet);
			if(usuario.getUsuasexo().equals("Hombre"))
				sexo.check(R.id.radMasculinoDet);
			else 
				sexo.check(R.id.radFemeninoDet);
			
			hablaInlges = (CheckBox)findViewById(R.id.chIdiomaDet);
			if(usuario.getUsuahain().equals("Si"))
				hablaInlges.setChecked(true);
			else
				hablaInlges.setChecked(false);
			
			botonActualizar 	= (Button)findViewById(R.id.btnRegistroDet);
			botonActualizar.setOnClickListener(this);
			
			botonVerReg 	= (Button)findViewById(R.id.btnVerRegDet);
			botonVerReg.setOnClickListener(this);
			
			botonNuevoDet 	= (Button)findViewById(R.id.btnNuevoDet);
			botonNuevoDet.setOnClickListener(this);
			
			//Referencias al xml fila_persona.xml.
		    imagenUsuario = (ImageView)findViewById(R.id.imagenPersonaDet);
		    imagenUsuario.setOnClickListener(this);
		    ruta_imagen = usuario.getUsuaimge();
		    System.out.println("ruta_imagen Adapater => "+ruta_imagen);
			File imagenArchivo = new  File(ruta_imagen);
			if(imagenArchivo.exists()){
				
				options.inSampleSize = 8;
				Bitmap bitmap = BitmapFactory.decodeFile(imagenArchivo.getAbsolutePath(),options);			
			    System.out.println("Alto Adapter REAL => "+bitmap.getHeight()+" Ancho Adapter REAL => "+bitmap.getWidth()+" bitmap::> "+bitmap);
			    
			    int alto  = 120;
		    	int ancho = 120;
		    	
	            bitmap = Bitmap.createScaledBitmap(bitmap, alto, ancho, true);
	            imagenUsuario.setImageBitmap(bitmap);            
	            System.gc();
			}	    
		}
	}

	/**
	 * Metodo para mostrar un DatePickerDialog
	 * */
	public void verDatePicker(){
		DatePickerDialog d = new DatePickerDialog( this , mDateSetListener, mYear, mMonth, mDay );
		d.show();
	}	
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.usuario_detalle, menu);
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

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch ( v.getId() ){
		case R.id.btnRegistroDet:
			usuarioDAO dao = new usuarioDAO(this);
			
			int radioButtonID = sexo.getCheckedRadioButtonId();
			View radioButton = sexo.findViewById(radioButtonID);
			int index = sexo.indexOfChild(radioButton);
			String sexoSelecc = (index == 0) ? "Hombre" : "Mujer";
			String hainSelecc = hablaInlges.isChecked() ? "Si" : "No";
			
			int id = dao.updateUser(Long.parseLong(usuacons), nombre.getText().toString(), fechaNacimiento.getText().toString(), spnPaises.getSelectedItem().toString(), sexoSelecc, hainSelecc, ruta_imagen);
			
			if(id != -1){							
				Toast.makeText(this, "Se actualizó con éxito [USUARIO] = "+ nombre.getText().toString(), Toast.LENGTH_SHORT).show();
				intent.setClass(this, ListadoUser.class);
				startActivity(intent);
			}else{
				Toast.makeText(this, "Error => Fallo el registro de usuario "+nombre.getText().toString(), Toast.LENGTH_SHORT).show();
			}
				break;			
		case R.id.btnVerRegDet:
			intent.setClass(this, ListadoUser.class);
			startActivity(intent);
			break;
		case R.id.btnNuevoDet:
			intent.setClass(this, MainActivity.class);
			startActivity(intent);
			break;
		case R.id.txtFechaNacDet: 
			verDatePicker(); 
			break;
		case R.id.imagenPersonaDet:
			ventanaImagen();
			break;
		
		}			
	}

	/**
	 * Metodo privado que abre la ventana para seleccionar a imagen.
	 */
	private void ventanaImagen() {
		try {
			final CharSequence[] items = { "Seleccionar de la galería" };

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Seleccionar una foto");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					switch (item) {
						case 0:							
							Intent intentSeleccionarImagen = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
							intentSeleccionarImagen.setType("image/*");
							startActivityForResult(intentSeleccionarImagen, SELECCIONAR_IMAGEN);
							break;
						}
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		} catch (Exception e) {
			Toast.makeText(this, "El error es: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Metodo que visualiza la imagen seleccionada en la interfaz
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		try {
			if (requestCode == SELECCIONAR_IMAGEN) {
				if (resultCode == Activity.RESULT_OK) {
					Uri selectedImage = data.getData();
					ruta_imagen = obtieneRuta(selectedImage);
					imagenUsuario.setImageURI(selectedImage);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Toast.makeText(this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
		}

	}
	
	/**
	 * Metodo para obtener datos de la imagen como alto, ancho etc...
	 * @param ruta_imagen
	 * @return
	 */
	@SuppressWarnings("unused")
	private Bitmap getBitmap(String ruta_imagen) {
		// Objetos.
		File imagenArchivo = new File(ruta_imagen);
		Bitmap bitmap = null;
		if (imagenArchivo.exists()) {
			bitmap = BitmapFactory.decodeFile(imagenArchivo.getAbsolutePath());
		}
		return bitmap;
	}
	
	/**
	 * Metodo privado obtiene la ruta de la imagen seleccionada
	 * 
	 * @param uri
	 * @return
	 */
	private String obtieneRuta(Uri uri) {
		String[] projection = { android.provider.MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
}
