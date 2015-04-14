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
	 * 数据库名称
	 */
	public static final String DB_NAME = "cool_weather";
	/**
	 * 数据库版本
	 */
	public static final int VERSION = 1;
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;

	/**
	 * 将构造函数私有化， 在创建dbHelper的时候，申明了一个数据库，接下来调用dbHelper的gewriteableDatabase方法时候，
	 * 可以创建该数据库，此时调用CoolWeatherOpenHelp的oncreate（）方法。
	 * 
	 * @param context
	 */

	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelp dbHelper = new CoolWeatherOpenHelp(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * 获取coolweatherDb的实例。
	 */

	public synchronized static CoolWeatherDB getInstance(Context context) {
		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}

	/**
	 * 将Provience实例储存到数据库
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
	 * 从数据库读取全国所有省份的数据
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
	 * 将City实例储存到数据库
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
	 * 从数据库读取省份下所有城市的信息
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
	 * 将Country实例储存到数据库
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
	 * 从数据库读取城市下所有县的信息
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
