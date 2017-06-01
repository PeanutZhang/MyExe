package com.cloudhome.webview_intercept.application;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;


public class MyApplication extends Application {

	public static String device_id;
	@Override
	public void onCreate() {
		super.onCreate();

		TelephonyManager tm = (TelephonyManager) getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		device_id = tm.getDeviceId() + "";
		if (device_id.equals("null") || device_id.equals("")) {
			device_id = Settings.Secure.getString(this.getContentResolver(),
					Settings.Secure.ANDROID_ID);
		}


	}



}
