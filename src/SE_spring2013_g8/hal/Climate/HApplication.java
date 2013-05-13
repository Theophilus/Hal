package SE_spring2013_g8.hal.Climate;

import android.app.Application;
import android.util.Log;

public class HApplication extends Application {
	
	private static final String TAG = "HApplication";

	@Override
	public void onCreate() {
		super.onCreate();
		HCore.makeInstance(this); //make an instance
		Log.d(TAG, "onCreate end");
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		//SettingsActivity.rebelPls(this);
		Log.d(TAG, "onTerm");
	}

}
