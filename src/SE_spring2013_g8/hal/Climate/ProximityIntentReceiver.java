package SE_spring2013_g8.hal.Climate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

public class ProximityIntentReceiver extends BroadcastReceiver{

	private static final String TAG = "ProximityIntentReceiver";
	private static final int NOTIF_ID = 1089;
	public static final String PREF_ID = "1089";
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive start");
		HCore hc = HCore.getInstance();
		boolean entering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
		if (entering) {
            Log.d(TAG, "entering");
            hc.atHome = true;
            hc.updateServer("entering");
        } else {
            Log.d(TAG, "exiting");
            hc.atHome = false;
            hc.updateServer("exiting");
        }
		Log.d(TAG, "onReceive end");
	}

}
