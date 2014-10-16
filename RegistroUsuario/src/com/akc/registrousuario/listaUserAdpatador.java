package com.akc.registrousuario;

import java.io.File;
import java.util.List;

import com.akc.registrousuario.models.Usuario;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class listaUserAdpatador extends ArrayAdapter<Usuario>{
	
	private final Context contexto;
	private final List<Usuario> listaUsuario;
	final BitmapFactory.Options options = new BitmapFactory.Options();
	
	public listaUserAdpatador(Context contexto, List<Usuario> listaUsuario) {
		 super(contexto, R.layout.activity_listado_user, listaUsuario);
		 this.contexto = contexto;		
		 this.listaUsuario = listaUsuario;
	}
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent){

		//Paso 1. Accedemos al Layout
		LayoutInflater inflater = LayoutInflater.from(contexto);
	    View item = inflater.inflate(R.layout.activity_listado_user, null);
	    
	    TextView nombre = (TextView) item.findViewById(R.id.lbl_ListNombre);
	    nombre.setText(listaUsuario.get(position).getUsuanomb()+" ("+listaUsuario.get(position).getUsuasexo()+")");
	    
	    TextView fechaNacimiento = (TextView) item.findViewById(R.id.lbl_ListFechaNac);
	    fechaNacimiento.setText(listaUsuario.get(position).getUsuapais()+" - "+listaUsuario.get(position).getUsuafena());
		
	    //Referencias al xml fila_persona.xml.
	    ImageView imagen = (ImageView)item.findViewById(R.id.imgUser);
		//Se obtiene la ruta de la imagen.		
	    String ruta_imagen = listaUsuario.get(position).getUsuaimge();
	    System.out.println("ruta_imagen Adapater => "+ruta_imagen);
		File imagenArchivo = new  File(ruta_imagen);
		if(imagenArchivo.exists()){
			
			options.inSampleSize = 8;
			Bitmap bitmap = BitmapFactory.decodeFile(imagenArchivo.getAbsolutePath(),options);			
		    System.out.println("Alto Adapter REAL => "+bitmap.getHeight()+" Ancho Adapter REAL => "+bitmap.getWidth()+" bitmap::> "+bitmap);
		    
		    int alto  = 120;
	    	int ancho = 120;
	    	
            bitmap = Bitmap.createScaledBitmap(bitmap, alto, ancho, true);
            imagen.setImageBitmap(bitmap);            
            System.gc();
		}	    
		return item;		
	}
	/*http://www.informit.com/articles/article.aspx?p=2143148&seqNum=2
	 * http://tekeye.biz/2014/android-bitmap-loading *   https://coderwall.com/p/wzinww
	 * android bitmap Resizing
	 */
	
	/*@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	 
	    int size = 0;
	    int width = getMeasuredWidth();
	    int height = getMeasuredHeight();
	    int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
	    int heigthWithoutPadding = height - getPaddingTop() - getPaddingBottom();
	 
	    // set the dimensions
	    if (widthWithoutPadding &gt; heigthWithoutPadding) {
	        size = heigthWithoutPadding;
	    } else {
	        size = widthWithoutPadding;
	    }
	 
	    setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
	}*/
}
