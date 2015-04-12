package com.coolweather.app.model;

public class Country {

	private int id;
	private String countryName;
	private String countryCode;
	private int cityId;

	public int getId() {
		return id;
	}

	public String getCountryName() {
		return countryName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public int getCityId() {
		return cityId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCountryName(String cityName) {
		this.countryName = cityName;
	}

	public void setCountryCode(String cityCode) {
		this.countryCode = cityCode;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
}
