package com.coolweather.app.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
			values.put("provience_name", provience.getProvienceName());
			values.put("provience_code", provience.getProvienceCode());
			db.insert("Provience", null, values);
		}
	}

	/**
	 * �����ݿ��ȡȫ������ʡ�ݵ�����
	 * 
	 * @return
	 */

	public List<Provience> loadProvience() {
		List<Provience> list = new ArrayList<Provience>();
		Cursor cursor = db.query("Provience", null, null, null, null, null,
				null);

		if (cursor.moveToNext()) {
			do {
				Provience provience = new Provience();
				provience.setId(cursor.getInt(cursor.getColumnIndex("id")));
				provience.setProvienceName(cursor.getString(cursor
						.getColumnIndex("provience_name")));
				provience.setProvienceCode(cursor.getString(cursor
						.getColumnIndex("provience_code")));
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

	public void saveCitye(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("provienceId", city.getProvienceId());
			db.insert("City", null, values);
		}
	}

	/**
	 * �����ݿ��ȡʡ�������г��е���Ϣ
	 * 
	 * @return
	 */

	public List<City> loadCity(int provienceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "provience_id=?",
				new String[] { String.valueOf(provienceId) }, null, null, null);

		if (cursor.moveToNext()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor
						.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor
						.getColumnIndex("city_code")));
				city.setProvienceId(provienceId);
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
			values.put("counrey_name", country.getCountryName());
			values.put("country_code", country.getCountryCode());
			values.put("cityId", country.getCityId());
			db.insert("Country", null, values);
		}
	}

	/**
	 * �����ݿ��ȡ�����������ص���Ϣ
	 * 
	 * @return
	 */

	public List<Country> loadCountry(int cityId) {
		List<Country> list = new ArrayList<Country>();
		Cursor cursor = db.query("Country", null, "city_id=?",
				new String[] { String.valueOf(cityId) }, null, null, null);

		if (cursor.moveToNext()) {
			do {
				Country city = new Country();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCountryName(cursor.getString(cursor
						.getColumnIndex("country_name")));
				city.setCountryCode(cursor.getString(cursor
						.getColumnIndex("country_code")));
				city.setCityId(cityId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}

}
