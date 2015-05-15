package com.coolweather.app.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherOpenHelp;

public class CoolWeatherDB {

	/**
	 * ���ݿ�����
	 */
	public static final String DB_NAME = "cool_weather";
	/**
	 * ���ݿ�汾
	 */
	public static final int VERSION = 1;
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;

	/**
	 * �����캯��˽�л��� �ڴ���dbHelper��ʱ��������һ�����ݿ⣬����������dbHelper��gewriteableDatabase����ʱ��
	 * ���Դ��������ݿ⣬��ʱ����CoolWeatherOpenHelp��oncreate����������
	 * 
	 * @param context
	 */

	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelp dbHelper = new CoolWeatherOpenHelp(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * ��ȡcoolweatherDb��ʵ����
	 */

	public synchronized static CoolWeatherDB getInstance(Context context) {
		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}

	/**
	 * ��Provienceʵ�����浽���ݿ�
	 * 
	 * @param provience
	 */

	public void saveProvience(Provience provience) {
		if (provience != null) {
			ContentValues values = new ContentValues();
			values.put("provience_quname", provience.getProvienceQuName());
			values.put("provience_pyname", provience.getProviencePyName());
			db.insert("Provience", null, values);
		}
	}

	/**
	 * �����ݿ��ȡȫ������ʡ�ݵ�����
	 * 
	 * @return
	 */

	public List<Provience> loadProviences() {
		List<Provience> list = new ArrayList<Provience>();
		Cursor cursor = db.query("Provience", null, null, null, null, null,
				null);

		if (cursor.moveToFirst()) {
			do {
				Provience provience = new Provience();
				provience.setId(cursor.getInt(cursor.getColumnIndex("id")));
				provience.setProvienceQuName(cursor.getString(cursor
						.getColumnIndex("provience_quname")));
				provience.setProviencePyName(cursor.getString(cursor
						.getColumnIndex("provience_pyname")));
				list.add(provience);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}

	/**
	 * ��Cityʵ�����浽���ݿ�
	 * 
	 * @param provience
	 */

	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_quname", city.getCityQuName());
			values.put("city_pyname", city.getCityPyName());
			values.put("provience_pyname", city.getProviencePyName());
			db.insert("City", null, values);
		}
	}

	/**
	 * �����ݿ��ȡʡ�������г��е���Ϣ
	 * 
	 * @return
	 */

	public List<City> loadCities(String provienceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "provience_pyname=?",
				new String[] { provienceId }, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityQuName(cursor.getString(cursor
						.getColumnIndex("city_quname")));
				city.setCityPyName(cursor.getString(cursor
						.getColumnIndex("city_pyname")));
				city.setProviencePyName(provienceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}

	/**
	 * ��Countryʵ�����浽���ݿ�
	 * 
	 * @param provience
	 */

	public void saveCountry(Country country) {
		if (country != null) {
			ContentValues values = new ContentValues();
			values.put("country_quname", country.getCountryQuName());
			values.put("country_pyname", country.getCountryPyName());
			values.put("city_pyname", country.getCityPyName());
			db.insert("Country", null, values);
		}
	}

	/**
	 * �����ݿ��ȡ�����������ص���Ϣ
	 * 
	 * @return
	 */

	public List<Country> loadCountries(String cityId) {
		List<Country> list = new ArrayList<Country>();
		Cursor cursor = db.query("Country", null, "city_pyname=?",
				new String[] { cityId }, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				Country country = new Country();
				country.setId(cursor.getInt(cursor.getColumnIndex("id")));
				country.setCountryQuName(cursor.getString(cursor
						.getColumnIndex("country_quname")));
				country.setCountryPyName(cursor.getString(cursor
						.getColumnIndex("country_pyname")));
				country.setCityPyName(cityId);
				list.add(country);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}

}
