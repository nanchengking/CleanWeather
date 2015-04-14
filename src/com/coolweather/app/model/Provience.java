package com.coolweather.app.model;

public class Provience {

	private int id;
	private String provienceQuName;
	private String proviencePyName;

	public int getId() {
		return id;
	}

	public String getProvienceQuName() {
		return provienceQuName;
	}

	public String getProviencePyName() {
		return proviencePyName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setProvienceQuName(String provienceName) {
		this.provienceQuName = provienceName;
	}

	public void setProviencePyName(String provienceCode) {
		this.proviencePyName = provienceCode;
	}

}
