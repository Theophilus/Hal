package lash.halapp;

import android.app.Application;

public class HALApp extends Application{

	public static final String TAG = "GlobalBSApp";
	
	protected HALCore mainBS;	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mainBS = HALCore.getInstance();
	}

}
