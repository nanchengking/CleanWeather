package com.coolweather.app.util;

import android.text.TextUtils;

import com.coolweather.app.model.City;
import com.coolweather.app.model.CoolWeatherDB;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Provience;

public class Utility {

	/**
	 * ���������ص��м�����
	 */
	public synchronized static boolean handleProvincesRequest(
			CoolWeatherDB coolWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Provience provience = new Provience();
					provience.setProvienceCode(array[0]);
					provience.setProvienceName(array[1]);
					// ���������������ݴ��浽Provience��
					coolWeatherDB.saveProvience(provience);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * ���������ص��м�����
	 */
	public synchronized static boolean handleCitysRequest(
			CoolWeatherDB coolWeatherDB, String response, int provienceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length > 0) {
				for (String p : allCities) {
					String[] array = p.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvienceId(provienceId);
					// ���������������ݴ��浽City��
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * ���������ص��ؼ�����
	 */
	public synchronized static boolean handleallCountriesRequest(
			CoolWeatherDB coolWeatherDB, String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCountries = response.split(",");
			if (allCountries != null && allCountries.length > 0) {
				for (String p : allCountries) {
					String[] array = p.split("\\|");
					Country country = new Country();
					country.setCountryCode(array[0]);
					country.setCountryName(array[1]);
					country.setCityId(cityId);
					// ���������������ݴ��浽Country��
					coolWeatherDB.saveCountry(country);
				}
				return true;
			}
		}
		return false;
	}

}
