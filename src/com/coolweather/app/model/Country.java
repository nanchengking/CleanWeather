package com.coolweather.app.model;

public class Country {

	private int id;
	private String countryQuName;
	private String countryPyName;
	private String cityPyName;

	public int getId() {
		return id;
	}

	public String getCountryQuName() {
		return countryQuName;
	}

	public String getCountryPyName() {
		return countryPyName;
	}

	public String getCityPyName() {
		return cityPyName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCountryQuName(String cityName) {
		this.countryQuName = cityName;
	}

	public void setCountryPyName(String cityCode) {
		this.countryPyName = cityCode;
	}

	public void setCityPyName(String cityId) {
		this.cityPyName = cityId;
	}
}
