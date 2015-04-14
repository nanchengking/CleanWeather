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
	 * 省列表
	 */
	private List<Provience> provienceList;
	/**
	 * 市列表
	 */
	private List<City> cityList;
	/**
	 * 县列表
	 */
	private List<Country> countryList;
	/**
	 * 选中的省份
	 */
	private Provience selectedProvience;
	/**
	 * 选中的城市
	 */
	private City selectedCity;
	/**
	 * 选中的县
	 */
	private Country selectedCountry;
	/**
	 * 当前的级别
	 */
	private int currentLevel;
	/**
	 * 是否从weatherActivity跳转过来？
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
	 * 查询全国所有省份，优先从数据库查询，如果没有再去数据库查询。
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
			titleText.setText("中国(￣︶￣)↗ ");
			currentLevel = LEVEL_PROVIENCE;
		} else {
			queryFromServer(null, "provience");
		}
	}

	/**
	 * 查询选中省内所有的市，优先从数据库查询，如果没有再从服务器查询
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
	 * 查询选中市内所有的县，优先从数据库查询，如果没有查到，再去服务器查询。
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
	 * 根据传入代号从服务器查询省市县数据。
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
					// 通过runOnUiThread()方法回到主线程处理逻辑

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
				// 通过runOnuiThread（）方法回到主线程处理逻辑
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败",
								Toast.LENGTH_SHORT).show();

					}

				});

			}

		});
	}

	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载d(╯﹏╰)b 咕~~");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 关闭对话框
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	/**
	 * 捕获Back键，根据当前的级别来判断，此时应该返回省列表，市列表还是县列表，还是直接退出
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
