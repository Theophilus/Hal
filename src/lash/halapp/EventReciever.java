package lash.halapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class EventReciever extends BroadcastReceiver{
	public static final String TAG = "EventReciever";
	public static final String LOCATION = "lash.globalbs.LOCATION";
	public static final String TIME = "lash.globalbs.TIME";
	
	@Override
	public void onReceive(Context con, Intent intent) {
		String iAction = intent.getAction();
		
		if(iAction.equals(LOCATION)){
			Log.d(TAG, "acting on loc");
			String msg = "$" + intent.getStringExtra(DevAutomation.DEVICE_NAME) + "^";
			Bundle alert = intent.getBundleExtra(DevAutomation.ALERT);
			if(alert!= null)
						msg += alert.getBoolean(DevAutomation.SET_STATE, false) + "$";
			ChooChooTrain.getInstance().addMsg(msg);
		} else if(iAction.equals(TIME)){
			Log.d(TAG, "acting on time");
			
		}
		else
			Log.w(TAG, "nonBSAct: " + iAction);
		
		ChooChooTrain.getInstance().sendMsgs(con);
		
		Log.d(TAG, "onReceive end");
	}

}
