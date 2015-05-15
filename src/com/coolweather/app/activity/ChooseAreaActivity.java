package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.app.model.City;
import com.coolweather.app.model.CoolWeatherDB;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Provience;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import com.example.coolweather.R;

public class ChooseAreaActivity extends Activity {

	public static final int LEVEL_PROVIENCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTRY = 2;

	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>();

	/**
	 * ʡ�б�
	 */
	private List<Provience> provienceList;
	/**
	 * ���б�
	 */
	private List<City> cityList;
	/**
	 * ���б�
	 */
	private List<Country> countryList;
	/**
	 * ѡ�е�ʡ��
	 */
	private Provience selectedProvience;
	/**
	 * ѡ�еĳ���
	 */
	private City selectedCity;
	/**
	 * ѡ�е���
	 */
	private Country selectedCountry;
	/**
	 * ��ǰ�ļ���
	 */
	private int currentLevel;
	/**
	 * �Ƿ��weatherActivity��ת������
	 */
	private boolean isFromWheatherActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		isFromWheatherActivity = getIntent().getBooleanExtra(
				"from_weather_activity", false);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (prefs.getBoolean("country_selected", false)
				&& !isFromWheatherActivity) {
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}

		setContentView(R.layout.choose_area);

		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		Log.d("Test", "number :" + 1);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (currentLevel == LEVEL_PROVIENCE) {
					selectedProvience = provienceList.get(arg2);
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(arg2);
					queryCountries();
				} else if (currentLevel == LEVEL_COUNTRY) {
					String cityPyName = countryList.get(arg2).getCityPyName();
					String countryQuName = countryList.get(arg2)
							.getCountryQuName();

					Intent intent = new Intent(ChooseAreaActivity.this,
							WeatherActivity.class);
					intent.putExtra("countryQuName", countryQuName);
					intent.putExtra("cityPyName", cityPyName);
					startActivity(intent);
					finish();
				}

			}

		});
		queryProviences();
	}

	/**
	 * ��ѯȫ������ʡ�ݣ����ȴ����ݿ��ѯ�����û����ȥ���ݿ��ѯ��
	 */

	private void queryProviences() {
		provienceList = coolWeatherDB.loadProviences();
		if (provienceList.size() > 0) {
			dataList.clear();
			for (Provience provience : provienceList) {
				dataList.add(provience.getProvienceQuName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�(�����)�J ");
			currentLevel = LEVEL_PROVIENCE;
		} else {
			queryFromServer(null, "provience");
		}
	}

	/**
	 * ��ѯѡ��ʡ�����е��У����ȴ����ݿ��ѯ�����û���ٴӷ�������ѯ
	 */
	private void queryCities() {
		cityList = coolWeatherDB.loadCities(selectedProvience
				.getProviencePyName());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityQuName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvience.getProvienceQuName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvience.getProviencePyName(), "city");
		}
	}

	/**
	 * ��ѯѡ���������е��أ����ȴ����ݿ��ѯ�����û�в鵽����ȥ��������ѯ��
	 */
	private void queryCountries() {
		countryList = coolWeatherDB.loadCountries(selectedCity.getCityPyName());
		if (countryList.size() > 0) {
			dataList.clear();
			for (Country country : countryList) {
				dataList.add(country.getCountryQuName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityQuName());
			currentLevel = LEVEL_COUNTRY;
		} else {
			queryFromServer(selectedCity.getCityPyName(), "country");
		}
	}

	/**
	 * ���ݴ�����Ŵӷ�������ѯʡ�������ݡ�
	 */
	private void queryFromServer(final String code, final String type) {
		String address;
		if (TextUtils.isEmpty(code)) {
			address = "http://flash.weather.com.cn/wmaps/xml/" + "china"
					+ ".xml";
		} else {
			address = "http://flash.weather.com.cn/wmaps/xml/" + code + ".xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				boolean result = false;
				if ("provience".equals(type)) {
					result = Utility.handleProvincesRequest(coolWeatherDB,
							response);
				} else if ("city".equals(type)) {
					result = Utility.handleCitysRequest(coolWeatherDB,
							response, selectedProvience.getProviencePyName());
				} else if ("country".equals(type)) {
					result = Utility.handleallCountriesRequest(coolWeatherDB,
							response, selectedCity.getCityPyName());
				}
				if (result) {
					// ͨ��runOnUiThread()�����ص����̴߳����߼�

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							closeProgressDialog();
							if ("provience".equals(type)) {
								queryProviences();
							
							} else if ("city".equals(type)) {
								queryCities();
								
							} else if ("country".equals(type)) {
								queryCountries();
								
							}

						}

					});
				}

			}

			@Override
			public void onError(Exception e) {
				// ͨ��runOnuiThread���������ص����̴߳����߼�
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��",
								Toast.LENGTH_SHORT).show();

					}

				});

			}

		});
	}

	/**
	 * ��ʾ���ȶԻ���
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���d(�s�n�t)b ��~~");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * �رնԻ���
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	/**
	 * ����Back�������ݵ�ǰ�ļ������жϣ���ʱӦ�÷���ʡ�б����б������б�����ֱ���˳�
	 */
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTRY) {
			queryCities();
		} else if (currentLevel == LEVEL_CITY) {
			queryProviences();
		} else {
			if (isFromWheatherActivity) {
				Intent intent = new Intent(this, WeatherActivity.class);
				startActivity(intent);
			}
			finish();
		}
	}
}
