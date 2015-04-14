package com.coolweather.app.util;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.model.City;
import com.coolweather.app.model.CoolWeatherDB;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Provience;

public class Utility {

	/**
	 * 解析处理返回的省级数据
	 */
	public synchronized static boolean handleProvincesRequest(
			CoolWeatherDB coolWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			try {
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				XmlPullParser xmlPullParser = factory.newPullParser();
				xmlPullParser.setInput(new StringReader(response));
				int eventType = xmlPullParser.getEventType();

				String quName = "";
				String pyName = "";

				while (eventType != XmlPullParser.END_DOCUMENT) {
					String nodeName = xmlPullParser.getName();// 每一个节点名称都是这个
					Provience provience = new Provience();
					switch (eventType) {
					// 开始解析某个节点
					case XmlPullParser.START_TAG: {
						if ("city".equals(nodeName)) {
							quName = xmlPullParser.getAttributeValue(0);
							pyName = xmlPullParser.getAttributeValue(1);
						}
						break;
					}
					case XmlPullParser.END_TAG: {
						if ("city".equals(nodeName)) {
							provience.setProvienceQuName(quName);
							provience.setProviencePyName(pyName);
							coolWeatherDB.saveProvience(provience);
						}
						break;

					}
					default:
						break;

					}
					eventType = xmlPullParser.next();
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 解析处理返回的市级数据
	 */
	public synchronized static boolean handleCitysRequest(
			CoolWeatherDB coolWeatherDB, String response, String proviencePyName) {
		if (!TextUtils.isEmpty(response)) {
			try {
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				XmlPullParser xmlPullParser = factory.newPullParser();
				xmlPullParser.setInput(new StringReader(response));
				int eventType = xmlPullParser.getEventType();

				String quName = "";
				String pyName = "";

				while (eventType != XmlPullParser.END_DOCUMENT) {
					String nodeName = xmlPullParser.getName();// 每一个节点名称都是这个
					City city = new City();
					switch (eventType) {
					// 开始解析某个节点
					case XmlPullParser.START_TAG: {
						if ("city".equals(nodeName)) {
							quName = xmlPullParser.getAttributeValue(2);
							pyName = xmlPullParser.getAttributeValue(5);
						}
						break;
					}
					case XmlPullParser.END_TAG: {
						if ("city".equals(nodeName)) {
							city.setCityQuName(quName);
							city.setCityPyName(pyName);
							city.setProviencePyName(proviencePyName);
							coolWeatherDB.saveCity(city);
						}
						break;

					}
					default:
						break;

					}
					eventType = xmlPullParser.next();
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 解析处理返回的县级数据
	 */
	public synchronized static boolean handleallCountriesRequest(
			CoolWeatherDB coolWeatherDB, String response, String cityPyName) {
		if (!TextUtils.isEmpty(response)) {
			try {
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				XmlPullParser xmlPullParser = factory.newPullParser();
				xmlPullParser.setInput(new StringReader(response));
				int eventType = xmlPullParser.getEventType();

				String quName = "";
				String pyName = "";

				while (eventType != XmlPullParser.END_DOCUMENT) {
					String nodeName = xmlPullParser.getName();// 每一个节点名称都是这个
					Country country = new Country();
					switch (eventType) {
					// 开始解析某个节点
					case XmlPullParser.START_TAG: {
						if ("city".equals(nodeName)) {
							quName = xmlPullParser.getAttributeValue(2);
							pyName = xmlPullParser.getAttributeValue(5);
						}
						break;
					}
					case XmlPullParser.END_TAG: {
						if ("city".equals(nodeName)) {
							country.setCountryQuName(quName);
							country.setCountryPyName(pyName);
							country.setCityPyName(cityPyName);
							coolWeatherDB.saveCountry(country);
						}
						break;

					}
					default:
						break;

					}
					eventType = xmlPullParser.next();
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 解析处理返回的天气数据
	 */
	public synchronized static boolean handleWeatherRequest(Context context,
			String response, String cityPyName, String countryQuName) {
		if (!TextUtils.isEmpty(response)) {
			try {
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				XmlPullParser xmlPullParser = factory.newPullParser();
				xmlPullParser.setInput(new StringReader(response));
				int eventType = xmlPullParser.getEventType();

				String quName = "";// 城市名字
				String temp1 = "";// 9温度下线
				String temp2 = "";// 10温度上限
				String stateDetailed = "";// 8天气状况
				String windState = "";// 12风向
				String publishTime = "";// 16数据时间
				String temNow="";//11当前温度

				while (eventType != XmlPullParser.END_DOCUMENT) {
					String nodeName = xmlPullParser.getName();// 每一个节点名称都是这个
					Country country = new Country();
					switch (eventType) {
					// 开始解析某个节点
					case XmlPullParser.START_TAG: {
						if ("city".equals(nodeName)
								&& countryQuName.equals(xmlPullParser
										.getAttributeValue(2))) {
							quName = xmlPullParser.getAttributeValue(2);
							temp1 = xmlPullParser.getAttributeValue(9);
							temp2 = xmlPullParser.getAttributeValue(10);
							stateDetailed = xmlPullParser.getAttributeValue(8);
							windState = xmlPullParser.getAttributeValue(12);
							publishTime = xmlPullParser.getAttributeValue(16);
							temNow = xmlPullParser.getAttributeValue(11);

						}
						break;
					}
					case XmlPullParser.END_TAG: {
						if ("city".equals(nodeName)
								&& countryQuName.equals(xmlPullParser
										.getAttributeValue(2))) {
							saveWeatherInfo(context, cityPyName, quName, temp1,
									temp2, stateDetailed, windState,
									publishTime,temNow);
						}
						break;

					}
					default:
						break;

					}
					eventType = xmlPullParser.next();
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static void saveWeatherInfo(Context context, String cityPyName,
			String quName, String temp1, String temp2, String stateDetailed,
			String windState, String publishTime,String temNow) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putString("cityPyName", cityPyName);
		editor.putBoolean("country_selected", true);
		editor.putString("country_name", quName);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("stateDetailed", stateDetailed);
		editor.putString("windState", windState);
		editor.putString("publishTime", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.putString("temNow", temNow);
		editor.commit();

	}
}
