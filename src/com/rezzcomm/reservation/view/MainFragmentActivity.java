package com.rezzcomm.reservation.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;

import com.rezzcomm.reservation.R;

public class MainFragmentActivity extends FragmentActivity {
	
	private MainFragment tabFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

//		Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler());

		FragmentManager fm = getSupportFragmentManager();
		tabFragment = (MainFragment) fm.findFragmentById(R.id.fragment_tab);
		tabFragment.loadMenu(1);
	}

//	private final class DefaultExceptionHandler implements UncaughtExceptionHandler {
//		@Override
//		public void uncaughtException(Thread thread, Throwable ex) {
//			// TODO Auto-generated method stub
//			System.exit(2);
//		}
//	}
	
	public boolean onKeyDown (int keyCode, KeyEvent event) { 
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			tabFragment.setVisibleMenu();
		}
		
		return super.onKeyDown(keyCode, event); 
	} 
}