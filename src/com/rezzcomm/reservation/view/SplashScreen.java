package com.rezzcomm.reservation.view;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.rezzcomm.reservation.R;
import com.rezzcomm.reservation.util.IOUtils;

public class SplashScreen extends Activity {
	protected int _splashTime = 1000;

	SharedPreferences sharedPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		IOUtils.antiClose(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				finish();
				Intent mainIntent = new Intent().setClass(SplashScreen.this, MainFragmentActivity.class);
				startActivity(mainIntent);
			}
		};

		Timer timer = new Timer();
		timer.schedule(task, _splashTime);
	}
}