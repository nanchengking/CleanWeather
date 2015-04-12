package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelp extends SQLiteOpenHelper {

	/**
	 * Provience表建表语句
	 */

	public static final String CREATE_PROVINCE = "create table Provience("
			+ "id integer primary key autoincrement," + "provience_name text,"
			+ "provience_code text)";
	/**
	 * City表建表语句
	 */

	public static final String CREATE_CITY = "create table City("
			+ "id integer primary key autoincrement," + "city_name text,"
			+ "city_code text," + "provience_id integer)";

	/**
	 * Country表建表语句
	 */

	public static final String CREATE_COUNTRY = "create table Country("
			+ "id integer primary key autoincrement," + "country_name text,"
			+ "country_code text," + "city_id integer)";

	public CoolWeatherOpenHelp(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTRY);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
