package SE_spring2013_g8.hal.Climate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

public class HCore {
	private static final String TAG = "HCore"; 
	private static final String PROX_ALERT_INTENT = "lash.happ.ProximityAlert";
	public static final String PREF_ID = "1089";
	public static final String DEV_STRINGS_ID = "devStrings";
	
	public static HCore me;	
	private Vector<Device> devs;
	private HashMap<String, Integer> name2i;
	private HComms hComms;
	private Location homeLoc; //will figure out a way to use with sharedpreferences
	private SharedPreferences sp;		//maybe in onCreate and onDestroy of application.
	public boolean atHome;
	
	
	
	private HCore(){
		devs = new Vector<Device>(5);
		devs.add(new Device("Lights", false, 1));
		devs.add(new Device("Windows", false, 1));
		devs.add(new Device("Garage", false, 1));
		devs.add(new Device("Blinds", false, 1));
		devs.add(new Device("Heater", false, 1));
		name2i = new HashMap<String, Integer>(5);
		for(int i = 0; i < devs.size(); i++)
			name2i.put(devs.get(i).name, i);
			
		hComms = new HComms();
		Log.d(TAG, "HCore created");
	}
	
	private HCore(Context c) {
		sp =  c.getSharedPreferences(PREF_ID, 0);
		Vector<String> devStrings = new Vector<String>(sp.getStringSet(DEV_STRINGS_ID,
											(Set<String>) new HashSet<String>()));
		if(!devStrings.isEmpty()){
			devs = new Vector<Device>(devStrings.size());
			String tmpS; int tmpN; boolean tmpB;
			for(String devStr : devStrings){
				tmpS = Device.parseInfo(devStr);
				tmpN = Device.n; tmpB = Device.bool;
				devs.add(new Device(tmpS,tmpB,tmpN));
			}
		} else {
			devs = new Vector<Device>(5);
			devs.add(new Device("Lights", false, 1));
			devs.add(new Device("Windows", false, 1));
			devs.add(new Device("Garage", false, 1));
			devs.add(new Device("Blinds", false, 1));
			devs.add(new Device("Heater", false, 1));
			
			updateStrings();
		}
		name2i = new HashMap<String, Integer>(5);
		for(int i = 0; i < devs.size(); i++)
				name2i.put(devs.get(i).name, i);
		hComms = new HComms();
	}

	public static void makeInstance(Context c) {
		me = new HCore(c);
		Log.d(TAG, "makeInstance");
	}
	
	public static HCore getInstance(){
		if(me == null)
			me = new HCore();
		return me;
	}
	
	//updates the strings stored in shared preferences
	public void updateStrings(){
		Log.d(TAG, "updateStrings start");
		Set<String> strs = new HashSet<String>(devs.size());
		for(Device d : devs)
			strs.add(d.name + "|" + d.state + "|" + d.num);
		SharedPreferences.Editor spe = sp.edit();
		spe.putStringSet(DEV_STRINGS_ID, strs);
		spe.apply();
		Log.d(TAG, "updateStrings end");
	}
	
	public Vector<String> getDevNames(){
		Vector<String> names = new Vector<String>(devs.size());
		for(Device d : devs)
			names.add(d.name);
		Log.d(TAG, "getDevNames end");
		return names;
	}
	
	
	//updates the server on some event using hcomms
	public void updateServer(String event){
		//if(hComms.setUpConnection())
		 	//hComms.sendMsg(event);
		int i = name2i.get("Lights").intValue();
		Device tmp = devs.get(i);
		tmp.state = event.equals("entering");
		devs.remove(i);
		devs.add(i, tmp);
		updateStrings();
	}
	
	
	public Location getHomeLoc() { return homeLoc; }
	public void setHomeLoc(Location l) {  homeLoc = l; Log.d(TAG,"setHomeB");}
	
	private class HComms {
		
		public boolean  setUpConnection(){
			return false;
		}
		
		public boolean sendMsg(String msg){
			return false;
		}
		
		//context needed for toast
		public boolean sendMsg(String msg, Context c){
			Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		public boolean requestUpdate(){
			return false;
		}
		
	}

	public void updateProxAlert(Context c) {
		LocationManager lm  = (LocationManager)c.getSystemService(Context.LOCATION_SERVICE);
		Intent intent = new Intent(PROX_ALERT_INTENT);
		PendingIntent proxIntent = PendingIntent.getBroadcast(c, 0, intent, 0);
		lm.removeProximityAlert(proxIntent); //remove the old alert
		lm.addProximityAlert(homeLoc.getLatitude(), homeLoc.getLongitude(), //alert co-ordinates 
							 10, -1, //radius and alert expiration time.
							 proxIntent);
		IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT); 
		c.registerReceiver(new ProximityIntentReceiver(), filter);
		Log.d(TAG, "updateProxAlert end");
	}

	
	
}
