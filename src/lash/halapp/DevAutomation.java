package lash.halapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class DevAutomation extends HomeDevice{
	
	private static final String TAG = "DevAutomation";
	
	Bundle alert; //message to send home when event triggered, extra info
	/********************************************************************
	 * Currently only uses boolean SET_STATE to imply ON/OFF
	 ********************************************************************/
	String event; //event which triggers msg to be sent.
	/*********************************************************************
	 * Format for Event:
	 * Location: 
	 * 	-> Proximity: "$Location:%" + <latitude> + "%" + <longitude>
	 * 					+ "%" + <radius> + "%" + <enter?true:false> + "%$"
	 * Time:
	 * 	-> Next Alarm: "$nAlarm$"
	 * 	-> Specific Time: "$Time:%" + <hour> + "%" + <Min> + "%"
	 * 					+ <sec> + "%" +[next Day (t,th,f,s,su) + "%"]+ "$"
	 * 
	 * 
	 * 
	 *********************************************************************/
	
	//keys
	public static final String ALERT = "alert"; //get 
	public static final String DEVICE_NAME = "deviceName";
	public static final String SET_STATE = "setState"; // 
	
	DevAutomation(String name, String event){
		this.devName = name;
		this.event = event;
	}
	
	DevAutomation(String name, String event, Bundle extra){
		this.devName = name;
		this.alert = extra;
		this.event = event;
	}

//	 it's better if something like this isn't used:
//	homeApp(String name, String event, int extra){
//		this.appName = name;
//		this.event = event;
//		Bundle tmp = new Bundle();
//		tmp.putInt("extra", extra);
//		this.alert = tmp;
//	}
//	
	
	
	public void registerEvent(Context c, int reqNum){
		if(event.contains("Location")){
			String event = this.event;
			
			int tmp = event.indexOf("$Location:")+9;
			event = event.substring(tmp, event.indexOf('$', tmp));
			double lat = DevAutomation.getDouble(event, -1);
			if(lat == -1){Log.e(TAG, "invalid event format"); return;}
			event = event.substring(event.indexOf('%',event.indexOf('%')+1));
			double log = DevAutomation.getDouble(event, -1);
			if(log == -1){Log.e(TAG, "invalid event format"); return;}
			event = event.substring(event.indexOf('%',event.indexOf('%')+1));
			float rad = DevAutomation.getFloat(event, -1);
			if(rad == -1){Log.e(TAG, "invalid event format"); return;}
			event = event.substring(event.indexOf('%',event.indexOf('%')+1));
			
			
			Intent intent = new Intent(EventReciever.LOCATION);
			intent.putExtra(DevAutomation.ALERT, this.alert);
			intent.putExtra(DevAutomation.DEVICE_NAME, this.devName);
			PendingIntent pi = PendingIntent.getBroadcast(c, reqNum, intent, 0);
			
			
			LocationManager lm = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
			lm.addProximityAlert(lat, log, rad, -1, pi);
		}
	}
		
	public static String proxToString(double lat, double lon, float rad, boolean enter){
			return "$Location:%"+lat+"%"+lon+"%"+rad+"%"+enter+"%$";
	}
	
	//return a double found between the first set of %, returns the failcase otherwise
	public static double getDouble(String d, double failCase){
		int tmp = d.indexOf('%')+1;
		if(tmp != -1){
			int tmp2 = d.indexOf('%', tmp);
			if(tmp2 != -1)
				return Double.valueOf(d.substring(tmp,tmp2));
		}
		
		return failCase;
	}
	
	//return a float found between the first set of %, returns the failcase otherwise
		public static float getFloat(String d, float failCase){
			int tmp = d.indexOf('%')+1;
			if(tmp != -1){
				int tmp2 = d.indexOf('%', tmp);
				if(tmp2 != -1)
					return Float.valueOf(d.substring(tmp,tmp2));
			}
			
			return failCase;
		}
	
		
		
}
