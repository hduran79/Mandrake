package com.akc.registrousuario.models;

public class Usuario {
		
	private int _id;
	private String usuanomb, usuafena, usuapais, usuasexo, usuahain, usuaimge;
	
	public Integer get_id() {
		return _id;
	}

	public void set_id(Integer _id) {
		this._id = _id;
	}

	public String getUsuanomb() {
		return usuanomb;
	}

	public void setUsuanomb(String usuanomb) {
		this.usuanomb = usuanomb;
	}

	public String getUsuafena() {
		return usuafena;
	}

	public void setUsuafena(String usuafena) {
		this.usuafena = usuafena;
	}

	public String getUsuapais() {
		return usuapais;
	}

	public void setUsuapais(String usuapais) {
		this.usuapais = usuapais;
	}

	public String getUsuasexo() {
		return usuasexo;
	}

	public void setUsuasexo(String usuasexo) {
		this.usuasexo = usuasexo;
	}

	public String getUsuahain() {
		return usuahain;
	}

	public void setUsuahain(String usuahain) {
		this.usuahain = usuahain;
	}
	
	public String getUsuaimge() {
		return usuaimge;
	}

	public void setUsuaimge(String usuaimge) {
	// Si ruta_imagen es null entonces se pone un valor predeterminado.
		if(usuaimge == null){
			this.usuaimge = "No tiene imagen.";
		}else{
			this.usuaimge = usuaimge;
		}
	}



}
