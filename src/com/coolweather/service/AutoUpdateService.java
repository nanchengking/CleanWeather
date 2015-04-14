package com.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.coolweather.app.receiver.AutoUpdateReceiver;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

public class AutoUpdateService extends Service {

	private boolean isFinish;
	@Override
	public IBinder onBind(Intent intent) {
		isFinish=intent.getBooleanExtra("isFinish", false);
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				updateWeather();

			}

		}).start();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 3*60*60*1000;
		long tringgerAtTime = SystemClock.elapsedRealtime() + anHour;
		Intent i = new Intent(this, AutoUpdateReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		if(!isFinish){
		
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, tringgerAtTime, pi);// 每隔一段时间就自动唤醒自己一次
		}else if(isFinish){
			manager.cancel(pi);
			
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 更新天气
	 */
	private void updateWeather() {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		final String cityPyName = prefs.getString("cityPyName", "");
		final String country_name = prefs.getString("country_name", "");
		String address = "http://flash.weather.com.cn/wmaps/xml/" + cityPyName
				+ ".xml";

		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				Utility.handleWeatherRequest(AutoUpdateService.this, response,
						cityPyName, country_name);

			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}

		});
	}

}
