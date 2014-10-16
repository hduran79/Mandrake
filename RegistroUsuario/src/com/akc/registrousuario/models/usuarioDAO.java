package com.akc.registrousuario.models;

import java.util.ArrayList;
import java.util.List;

import com.akc.registrousuario.UsuarioSQLiteHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author      hugo.duan
 * @descripcion Esta clase se encargar de la estructura y modelo de la tabla y operaciones que vamos a realizar sobre la tabla
 */

public class usuarioDAO {
	
	private final static String TAG = "Debug";	
	private UsuarioSQLiteHelper helper;
	private static SQLiteDatabase db;
	private String status_message = "";
	
	public usuarioDAO(Context context){
		helper = new UsuarioSQLiteHelper(context);
	}
	
	
	/** Abre conexion a base de datos */
	public void abrir(){		
		db = helper.getWritableDatabase();
		if ( db != null) {			
			Log.i("Informativo", "Se abre conexion a la base de datos " + helper.getDatabaseName() );
		}else{
			Log.i("Error", "Error al abrir la a la base de datos " + helper.getDatabaseName() );
		}
	}
	
	/** Cierra conexion a la base de datos */
	public void cerrar(){
		Log.i("Informativo", "Se cierra conexion a la base de datos " + helper.getDatabaseName() );
		helper.close();		
	}
	
	
	/**
	 * Contenedor de valores 
	* @param String nombre Nombre completo
	 * @param String fecha fecha de nacimiento de la forma 12/05/1900
	 * @param String pais
	 * @param String sexo
	 * @param String ingles si habla ingles
	 * @return array de valores de tipo ContentValues
	 */
	public ContentValues generarContentValues(String nombre, String fechaNacimiento, String pais, String sexo, String hablaIngles, String ruta_imagen){
		ContentValues valores = new ContentValues();
		valores.put(helper.USUA_NOMB, nombre);
		valores.put(helper.USUA_FENA, fechaNacimiento);
		valores.put(helper.USUA_PAIS, pais);
		valores.put(helper.USUA_SEXO, sexo);
		valores.put(helper.USUA_HAIN, hablaIngles);		
		valores.put(helper.USUA_IMAG, ruta_imagen);		
		return valores;
	}
	
	/**
	 * Metodo para agregar un nuevo registro
	 * @param String nombre Nombre completo
	 * @param String fecha fecha de nacimiento de la forma 12/05/1900
	 * @param String pais
	 * @param String sexo
	 * @param String ingles si habla ingles
	 * @return BOOLEAN TRUE si tuvo exito FALSE caso contrario
	 * */
	public Long insertGenerico(Usuario usuario){
		 
		ContentValues values = new ContentValues();
	    values.put(helper.USUA_NOMB, usuario.getUsuanomb()); // Contact Name
	    values.put(helper.USUA_FENA, usuario.getUsuafena()); // Contact Name
	    values.put(helper.USUA_PAIS, usuario.getUsuapais()); // Contact Name
	    values.put(helper.USUA_SEXO, usuario.getUsuasexo()); // Contact Name
	    values.put(helper.USUA_HAIN, usuario.getUsuahain()); // Contact Name
	    values.put(helper.USUA_IMAG, usuario.getUsuaimge()); // Contact Name
	 
	    // Inserting Row
	    Long id = db.insert(helper.TABLE_NAME, null, values);

	    // Closing database connection	    
	    db.close(); 
		return id;
	}
	
	public Long insertar(String nombre, String fechaNacimiento, String pais, String sexo, String hablaIngles, String ruta_imagen){
		/*String sql = "delete from "+helper.TABLE_NAME;
		db.execSQL(sql);*/
		
		//Insert retorna -1 si hubo error al momento de insertar el registro
		Long id = (long) db.insert(helper.TABLE_NAME, null, generarContentValues(nombre, fechaNacimiento, pais, sexo, hablaIngles, ruta_imagen));
		System.out.println("ID insert =>"+id);
		
		/*Cursor a =  db.rawQuery("select count(0) from usuarios", null);
		System.out.println(a.getCount());*/
		
		//cerrar();
		return id;
	} 
	
	
	public int eliminarUser(Long id){
		int row = db.delete(helper.TABLE_NAME, helper.USUA_CONS + "= ?", new String[]{Long.toString(id)});
		cerrar();
		return row;
		//String sql = "delete from "+helper.TABLE_NAME+" where "+helper.USUA_CONS+ "= "+id+"";
		//db.execSQL(sql);
	}
	
	public int updateUser(Long id, String nombre, String fechaNacimiento, String pais, String sexo, String hablaIngles, String ruta_imagen){
		int row = db.update(helper.TABLE_NAME, generarContentValues(nombre, fechaNacimiento, pais, sexo, hablaIngles, ruta_imagen), helper.USUA_CONS + " = ?", new String[]{Long.toString(id)});
		cerrar();
		return row;		
	}
	
	/**
	 * Buscar un usuario campo USUACONS llave primaria
	 * @param id
	 * @return
	 */
	public Cursor buscarUsuario(Long id){
		
		//Opcion 1
		/*String[] columnas = new String[]{helper.USUA_NOMB, helper.USUA_FENA, helper.USUA_PAIS, helper.USUA_SEXO, helper.USUA_HAIN};
		Cursor datos = db.query(helper.TABLE_NAME, columnas, helper.USUA_CONS + " = ?", new String[]{Long.toString(id)}, null, null, null);*/	
		
		//Opcion 2.
		String[] args = new String[] {Long.toString(id)};
		Cursor datos = db.rawQuery(" SELECT usuanomb, usuafena, usuapais, usuasexo, usuahain, usuaimge FROM Usuarios WHERE _id = ? ", args);
		//cerrar();
		return datos;
	}
	
	/**
	 * Dado un Cursor con los registros de la base de datos, da formato y retorna resultado
	 * @return ArrayList<String>
	 * */
	public ArrayList<String> getFormatListUniv(Cursor cursor){
		ArrayList<String> listData = new ArrayList<String>();
		String item = "";
			if( cursor.moveToFirst() ){
				do{
					item += "Nombre: " + cursor.getString(0) + "\r\n";
					item += "Fecha de Nacimiento: " + cursor.getString(1) + "\r\n";
					item += "Pais: " + cursor.getString(2) + "\r\n";
					item += "Sexo: " + cursor.getString(3) + "\r\n";
					item += "Habla ingles: " + cursor.getString(4) + "";
					listData.add( item );
					item="";
		            
				} while ( cursor.moveToNext() );
			}		
		return listData;		
	}
	
		
	/**
	 * Obtiene todos los registros de la unica tabla de la base de datos
	 * @return Cursor
	 * */        
	public List<Usuario> getAllUser() {
	    List<Usuario> registro = new ArrayList<Usuario>();
	   
	    String table      		= helper.TABLE_NAME;         //tabla a consultar (FROM)
	    String[] columnas 		= new String[]{helper.USUA_CONS, helper.USUA_NOMB, helper.USUA_FENA, helper.USUA_PAIS, helper.USUA_SEXO, helper.USUA_HAIN, helper.USUA_IMAG}; //columnas a devolver (SELECT)     
        String selection  		= null; //consulta (WHERE) ---- String selection = helper.USUA_CONS + " = ? and "+helper.USUA_PAIS + " = ?"; 
        String[] selectionArgs 	= null; //reemplaza “?” de la consulta ----- String[] selectionArgs = new String[]{"1","Colombia"};      
        String groupBy 			= null; //agrupado por (GROUPBY)
        String having 			= null; //condición para agrupación
        String orderBy 			= null; //ordenado por 
        String limit 			= null; //cantidad máx. de registros*/
        	    
		Cursor rs = db.query(table, columnas, selection, selectionArgs, groupBy, having, orderBy, limit);

	    rs.moveToFirst();
	    while (!rs.isAfterLast()) {
	    	Usuario dato = cursorToComment(rs);
	    	registro.add(dato);
	      rs.moveToNext();
	    }
	    // make sure to close the cursor
	    rs.close();
	    return registro;
	  }

	  private Usuario cursorToComment(Cursor cursor) {
		  Usuario dao = new Usuario();
		  dao.set_id(cursor.getInt(0));
		  dao.setUsuanomb(cursor.getString(1));
		  dao.setUsuafena(cursor.getString(2));
		  dao.setUsuapais(cursor.getString(3));
		  dao.setUsuasexo(cursor.getString(4));
		  dao.setUsuahain(cursor.getString(5));
		  dao.setUsuaimge(cursor.getString(6));
	    return dao;
	  }
	  
	  
	  public Usuario getStudentById(Long id){
		  Cursor c = buscarUsuario(id);
		  Usuario usuario = new Usuario();
		    
		    if (c.moveToFirst()) {
	            do {
	            	usuario.setUsuanomb(c.getString(c.getColumnIndex(helper.USUA_NOMB)));
	            	usuario.setUsuafena(c.getString(c.getColumnIndex(helper.USUA_FENA)));
	            	usuario.setUsuapais(c.getString(c.getColumnIndex(helper.USUA_PAIS)));	              
	            	usuario.setUsuasexo(c.getString(c.getColumnIndex(helper.USUA_SEXO)));	              
	            	usuario.setUsuahain(c.getString(c.getColumnIndex(helper.USUA_HAIN)));	              	            		              
	            	usuario.setUsuaimge(c.getString(c.getColumnIndex(helper.USUA_IMAG)));	              	            		              
	            } while (c.moveToNext());
	        }	 
	        c.close();
	        cerrar();	        
		    return usuario;	 
	}

}
