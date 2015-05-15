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
	 * ���������ص�ʡ������
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
					String nodeName = xmlPullParser.getName();// ÿһ���ڵ����ƶ������
					Provience provience = new Provience();
					switch (eventType) {
					// ��ʼ����ĳ���ڵ�
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
	 * ���������ص��м�����
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
					String nodeName = xmlPullParser.getName();// ÿһ���ڵ����ƶ������
					City city = new City();
					switch (eventType) {
					// ��ʼ����ĳ���ڵ�
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
	 * ���������ص��ؼ�����
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
					String nodeName = xmlPullParser.getName();// ÿһ���ڵ����ƶ������
					Country country = new Country();
					switch (eventType) {
					// ��ʼ����ĳ���ڵ�
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
	 * ���������ص���������
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

				String quName = "";// ��������
				String temp1 = "";// 9�¶�����
				String temp2 = "";// 10�¶�����
				String stateDetailed = "";// 8����״��
				String windState = "";// 12����
				String publishTime = "";// 16����ʱ��
				String temNow="";//11��ǰ�¶�

				while (eventType != XmlPullParser.END_DOCUMENT) {
					String nodeName = xmlPullParser.getName();// ÿһ���ڵ����ƶ������
					Country country = new Country();
					switch (eventType) {
					// ��ʼ����ĳ���ڵ�
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

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��", Locale.CHINA);
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
