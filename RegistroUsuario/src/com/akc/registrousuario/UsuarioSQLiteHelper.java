package com.akc.registrousuario;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsuarioSQLiteHelper extends SQLiteOpenHelper{	
	
	// Nombre de nuestro archivo de base de datos
	private static final String DB_NAME = "usuarios.sqlite";
	// Version de nuestra base de datos
	private static final int DB_SCHEME_VERSION = 1;
	// Nombre de la tabla
	public static String TABLE_NAME = "usuarios";
	
	// CONSTRUCTOR de la clase
	public UsuarioSQLiteHelper(Context context) {
		super(context, DB_NAME, null, DB_SCHEME_VERSION);
	}

	public final static String USUA_CONS = "_id";
	public final static String USUA_NOMB = "usuanomb";
	public final static String USUA_FENA = "usuafena";
	public final static String USUA_PAIS = "usuapais";
	public final static String USUA_SEXO = "usuasexo";
	public final static String USUA_HAIN = "usuahain";
	public final static String USUA_IMAG = "usuaimge";
	
	//Sentencia para crear la base de datos
	public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+ " ( " +
			USUA_CONS + " integer primary key autoincrement, " +
			USUA_NOMB + " text not null, " +
			USUA_FENA + " text not null, " +
			USUA_PAIS + " text not null, " +
			USUA_SEXO + " text not null, " +
			USUA_HAIN + " text not null, " +
			USUA_IMAG + " text not null); ";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//Se ejecuta la sentencia SQL de creación de la tabla
		db.execSQL(CREATE_TABLE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//elimina tabla
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		//y luego creamos la nueva tabla
		db.execSQL(CREATE_TABLE);
	}

}
