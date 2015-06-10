package com.coolweather.app.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import com.coolweather.service.AutoUpdateService;
import com.example.coolweather.R;

public class WeatherActivity extends Activity implements OnClickListener {
	private LinearLayout weatherInfoLayout;
	private TextView countryName;
	private TextView publishText;
	private TextView stateDetailed;
	private TextView temp1Text;
	private TextView temp2Text;
	private TextView current_date;
	private Button switchCity;
	private Button refreshWeather;
	private TextView temNow;
	private ImageButton closeWindow;
	private int i = 0;
	// 用来发短信的
	private SmsManager sms;
	private MediaPlayer mp;
	private String message = null;
	private PendingIntent pi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		int j=0;
		Log.d("Test", ""+j++);
		sms = SmsManager.getDefault();
		pi = PendingIntent.getActivity(this, 0, new Intent(this,
				WeatherActivity.class), 0);

		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		countryName = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		stateDetailed = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		current_date = (TextView) findViewById(R.id.current_date);
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		temNow = (TextView) findViewById(R.id.tempNow);
		closeWindow = (ImageButton) findViewById(R.id.close_window);

		mp = MediaPlayer.create(this, R.raw.music);

		Log.d("Test", "" + closeWindow.isSoundEffectsEnabled());

		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		closeWindow.setOnClickListener(this);

		String quName = getIntent().getStringExtra("countryQuName");
		String pyName = getIntent().getStringExtra("cityPyName");

		if (!TextUtils.isEmpty(pyName)) {
			publishText.setText("同步中……");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			countryName.setVisibility(View.INVISIBLE);
			queryWeatherPyName(pyName, quName);
		} else {
			/**
			 * 没有intent传递县级拼音，直接显示本地天气
			 */
			showWeather();
		}

	}

	/**
	 * 查询县级拼音代表的天气
	 */
	private void queryWeatherPyName(String pyName, String quName) {
		String address = "http://flash.weather.com.cn/wmaps/xml/" + pyName
				+ ".xml";
		queryFromServer(address, pyName, quName);
	}

	/**
	 * 根据查询县级拼音联网查询天气
	 */
	private void queryFromServer(final String address, final String pyName,
			final String quName) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				Utility.handleWeatherRequest(WeatherActivity.this, response,
						pyName, quName);

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						showWeather();

					}

				});
			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						publishText.setText("同步失败(┬＿┬)");

					}

				});
			}

		});

	}

	/**
	 * 从Sharedpreferences文件中读取天气信息
	 */
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		countryName.setText(prefs.getString("country_name", ""));
		publishText.setText("今天：" + prefs.getString("publishTime", "")
				+ "发布的呢~");
		stateDetailed.setText(prefs.getString("stateDetailed", ""));
		
		int tempNum1=Integer.parseInt(prefs.getString("temp1", ""));
		int tempNum2=Integer.parseInt(prefs.getString("temp2", ""));
		if(tempNum1>tempNum2){
			temp1Text.setText(tempNum2 + "℃");
			temp2Text.setText(tempNum1+ "℃");
		}else{
			temp1Text.setText(tempNum1 + "℃");
			temp2Text.setText(tempNum2+ "℃");
		}
		
		
		
		current_date.setText(prefs.getString("current_date", "") + "\n"
				+ prefs.getString("windState", ""));
		temNow.setText(prefs.getString("temNow", "") + "℃");
		weatherInfoLayout.setVisibility(View.VISIBLE);
		countryName.setVisibility(View.VISIBLE);

		Intent intent = new Intent(this, AutoUpdateService.class);
		startService(intent);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("同步中呢……");
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			String quName = prefs.getString("country_name", "");
			String pyName = prefs.getString("cityPyName", "");

			if (!TextUtils.isEmpty(pyName)) {
				queryWeatherPyName(pyName, quName);
			}
			break;
		case R.id.close_window:
			if (!mp.isPlaying()) {
				mp.start();
			}
			if (i == 0) {
				message = "好像要下雨了，机器人，请帮我把窗户关好，谢谢！Y(^_^)YY(^_^)Y";
				closeWindow.setBackgroundResource(0);
				closeWindow.setBackgroundResource(R.drawable.openwindow);
				i = 1;
				Toast.makeText(this, "您的窗户已经关好！", Toast.LENGTH_SHORT).show();
			} else {
				message = "好像天气很好，机器人，请帮我把窗户打开，谢谢！Y(^_^)YY(^_^)Y";
				i = 0;
				closeWindow.setBackgroundResource(0);
				closeWindow.setBackgroundResource(R.drawable.close_window);
				Toast.makeText(this, "您的窗户已经打开！", Toast.LENGTH_SHORT).show();
			}
			sms.sendTextMessage("10086", null, message, pi, null);
		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		Intent intent = new Intent(this, AutoUpdateService.class);
		intent.putExtra("isFinish", true);
		stopService(intent);
		super.onDestroy();

	}

}
