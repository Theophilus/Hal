package lash.halapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class HALCore {

	public static final String TAG = "MainBS";
	
	private static HALCore instance;
	private static ArrayList<HomeDevice> devices; //used to populate item list in viewDevices activity
	private static HashMap<Integer,DevAutomation> devOrders; //Integer is used to identify event
	private static ChooChooTrain coms; //main communicator between home sever and GlobalBS

	
	public static HALCore getInstance(){
		if(instance == null){
			instance = new HALCore();
		}
		return instance;
	}
	
	
	public static void addOrder(Context c, String appName, String event){
		
		HALCore.addOrder(c, appName, event, new Bundle());
		
		Log.d(TAG, "end addApp(Str,Str)");
	}
			

	public static void addOrder(Context c, String appName, String event, Bundle extra){
		
		DevAutomation temp = new DevAutomation(appName,event, extra);
		temp.registerEvent(c, devOrders.size());
		devOrders.put(devOrders.size(), temp);
		
		Log.d(TAG, "end addApp(Str,Str,Bun)");
	}
	
	//this method should prob not be used...
	public static void registerOrders(Context c){
		for(Map.Entry<Integer, DevAutomation> i: devOrders.entrySet()){
			i.getValue().registerEvent(c, i.getKey().intValue());
		}
		Log.d(TAG, "registerOrders end");
	}
	
	
}
