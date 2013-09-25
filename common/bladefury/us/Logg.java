package common.bladefury.us;

import android.util.Log;

public class Logg {
	static final boolean LOG_ENABLED = true;
	static String LOG_TAG = "log";
	
	public static void setTag(String tag) {
		LOG_TAG = tag;
	}
	
	public static void v(String fmt, Object... args) {
		if (LOG_ENABLED) {
			Log.v(LOG_TAG, String.format(fmt, args));
		}
	}
	
	public static void i(String fmt, Object... args) {
		if (LOG_ENABLED) {
			Log.i(LOG_TAG, String.format(fmt, args));
		}
	}
	
	public static void d(String fmt, Object... args) {
		if (LOG_ENABLED) {
			Log.d(LOG_TAG, String.format(fmt, args));
		}
	}
	
	public static void w(String fmt, Object... args) {
		if (LOG_ENABLED) {
			Log.w(LOG_TAG, String.format(fmt, args));
		}
	}
	
	public static void e(String fmt, Object... args) {
		if (LOG_ENABLED) {
			Log.e(LOG_TAG, String.format(fmt, args));
		}
	}
}
