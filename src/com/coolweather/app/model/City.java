package com.coolweather.app.model;

public class City {
	
	private int id;
	private String cityQuName;
	private String cityPyName;
	private String proviencePyName;
	public int getId() {
		return id;
	}
	public String getCityQuName() {
		return cityQuName;
	}
	public String getCityPyName() {
		return cityPyName;
	}
	public String getProviencePyName() {
		return proviencePyName;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setCityQuName(String CityQuName) {
		this.cityQuName = CityQuName;
	}
	public void setCityPyName(String CityPyName) {
		this.cityPyName = CityPyName;
	}
	public void setProviencePyName(String provienceId) {
		this.proviencePyName = provienceId;
	}

}
