package com.akc.registrousuario;

import java.io.File;

import utiles.Validation;

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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.TableRow.LayoutParams;


public class MainActivity extends Activity  implements OnClickListener{

	private usuarioDAO dao;
	private final static String TAG = "Debug";	
	//Variables para fecha
    private int mYear;
    private int mMonth;
    private int mDay;
    
    private EditText 	nombre;
	private EditText 	fechaNacimiento;
	private Spinner 	spnPaises;
	private RadioGroup 	sexo;
	private CheckBox 	hablaInlges;
	private Button 		botonRegistro;
	private Button 		botonVerReg;
	
	private ImageView 	imagenUsuario;
	private String 		ruta_imagen;
	private int SELECCIONAR_IMAGEN = 237487;
	
	//private static final Long usuacons = null;

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
		setContentView(R.layout.activity_main);

		/* Find Tablelayout defined in main.xml */
		TableLayout tl = (TableLayout) findViewById(R.id.TableLayout1);
		/* Create a new row to be added. */
		TableRow tr = new TableRow(this);
		tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
		tr.setId(9);
		tr.setBackgroundColor(Color.BLUE);
		
		/* Create */
		TextView caja = new TextView(this);
		caja.setId(1);
		caja.setWidth(100);
		caja.setHeight(30);
		caja.setText("Label");
        caja.setTextColor(Color.GREEN);

		EditText texto = new EditText(this);
		texto.setText("Dynamic Button");
		texto.setWidth(400);
		texto.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

		tr.addView(caja);
		tr.addView(texto);
		tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

		nombre 			= (EditText)findViewById(R.id.txtUser);
		fechaNacimiento = (EditText)findViewById(R.id.txtFechaNac);
		fechaNacimiento.setOnClickListener(this);
		
		//Se llena el Spinner con los nombres de paises
		String[] arrayPaises 		= getResources().getStringArray(R.array.paises);
		spnPaises 					= (Spinner) findViewById(R.id.spnpaises );
		ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayPaises );
		spnPaises.setAdapter(adapter);
		
		sexo 		= (RadioGroup)findViewById(R.id.rgrsexo);
		hablaInlges = (CheckBox)findViewById(R.id.chIdioma);
		
		imagenUsuario = (ImageView) findViewById(R.id.imagenPersona);
		imagenUsuario.setOnClickListener(this);
		
		botonRegistro 	= (Button)findViewById(R.id.btnRegistro);
		botonRegistro.setOnClickListener(this);
		botonVerReg 	= (Button)findViewById(R.id.btnVerReg);
		botonVerReg.setOnClickListener(this);
		
		/* VALIDAR CAMPOS */
		validarCampos();
		
		
	        
			
		dao = new usuarioDAO(this);
		//dao.abrir();
		
	}
	
	/**
	 * Evento clic sobre los controles: fecha, boton registrar
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch ( v.getId() ){
					case R.id.txtFechaNac: 
						verDatePicker(); 
						break;
					case R.id.btnRegistro:

						int radioButtonID = sexo.getCheckedRadioButtonId();
						View radioButton = sexo.findViewById(radioButtonID);
						int index = sexo.indexOfChild(radioButton);
						String sexoSelecc = (index == 0) ? "Hombre" : "Mujer";
						String hainSelecc = hablaInlges.isChecked() ? "Si" : "No";
						
						Usuario bt = new Usuario();
							bt.setUsuanomb(nombre.getText().toString());
							bt.setUsuafena(fechaNacimiento.getText().toString());
							bt.setUsuapais(spnPaises.getSelectedItem().toString());
							bt.setUsuasexo(sexoSelecc);
							bt.setUsuahain(hainSelecc);
							bt.setUsuaimge(ruta_imagen);
							
						if ( checkValidation () ){						
		                    //Long id = dao.insertar(nombre.getText().toString(), fechaNacimiento.getText().toString(), spnPaises.getSelectedItem().toString(), sexoSelecc, hainSelecc, ruta_imagen);
							Long id = dao.insertGenerico(bt);
							if(id != -1){							
								Toast.makeText(this, "El registro se agregó con éxito ID = "+ id, Toast.LENGTH_SHORT).show();
								i.setClass(this, ListadoUser.class);
								startActivity(i);
							}else{
								Toast.makeText(this, "Error => Fallo el registro de usuario "+nombre.getText().toString(), Toast.LENGTH_SHORT).show();
							}
						}
						break;
					case R.id.btnVerReg:
						Toast.makeText(this, "Listado Ver Registro", Toast.LENGTH_SHORT).show();
						i.setClass(this, ListadoUser.class);
						startActivity(i);
						break;
					case R.id.imagenPersona:
						ventanaImagen();
						break;
						
		}
	}
	
	/**
	 * Validacion Opcional
	 * Chequear Validacion controles EditText, Combo etc... esta validacion se hace antes de dar clic en el boton Agregar 
	**/
	private void validarCampos(){
		/**
		 * TextWatcher se utiliza para vigilar a EditarTexto al introducir datos en él. Podemos realizar la operación y vigilar a los que se introducen 
		 * los caracteres o el número de caracteres se introducen en el EditarTexto.
		 * TextWatcher clase tiene 3 métodos y necesitamos sustituir estos métodos para implementar nuestra propia funcionalidad.
		 *    addTextChangedListener, beforeTextChanged, onTextChanged
		 **/
		
		nombre.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(nombre);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
		
		fechaNacimiento.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(fechaNacimiento);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
		
	}
	
	/**
	 * Chequear validacion boton Agregar 
	 **/
    private boolean checkValidation() {
        boolean ret = true;
 
        if(ruta_imagen == null){
        	Toast.makeText(this, "Debe seleccionar una imagen", Toast.LENGTH_LONG).show();
        	ret = false;
        }
        
        if (!Validation.hasText(nombre)) ret = false;
        if (!Validation.hasText(fechaNacimiento)) ret = false;
        
        return ret;
    }
    
	/**
	 * Metodo para mostrar un DatePickerDialog
	 * */
	public void verDatePicker(){
		DatePickerDialog d = new DatePickerDialog( this , mDateSetListener, mYear, mMonth, mDay );
		d.show();
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
