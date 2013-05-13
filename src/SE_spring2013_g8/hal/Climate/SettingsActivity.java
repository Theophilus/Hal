package SE_spring2013_g8.hal.Climate;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	
	private static final String TAG = "SettingsActivity";
	private Location currLoc;
	private MyLocListener mLL;
	private boolean listening;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_settings);
		
		LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		Criteria cri = new Criteria(); cri.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = lm.getBestProvider(cri, true);
		
		mLL = new MyLocListener();
		if (provider != null){
			lm.requestLocationUpdates(provider, 1000, 0, mLL);
			listening = false;
			if(lm.getProvider(provider) == null || lm.getProvider(provider).getAccuracy() != Criteria.ACCURACY_FINE){
				// pop an alert to tell the user Accuracy is bad
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.badAcc_message);
				builder.setTitle(R.string.badAcc_title);
				
				Log.d(TAG, "flag1");
				
				builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			           }
			       });
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			           {public void onClick(DialogInterface dialog, int id){dialog.dismiss();}});
				
				AlertDialog dialog = builder.create(); dialog.show();			
			}
		} else {
			// pop an alert to tell the user GPS/locationi providers are off
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.badPro_message);
			builder.setTitle(R.string.badPro_title);
			
			Log.d(TAG, "flag2");
			
			builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			       public void onClick(DialogInterface dialog, int id) {
			    	   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			       }
			   });
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			       {public void onClick(DialogInterface dialog, int id){dialog.dismiss();}});
			
			AlertDialog dialog = builder.create(); dialog.show();
		}
		
		Log.d(TAG, "onCreate mid");
		
		this.getCurrentLocation(this.findViewById(R.id.getLocButton));
		currLoc = new Location(LocationManager.PASSIVE_PROVIDER);
		
		Log.d(TAG, "onCreate end");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		lm.removeUpdates(mLL);
		listening = false;
		//rebelPls(this);
	}
	
	//stops listening location updates
//	public static void rebelPls(Context c){
//		LocationManager lm = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
//		lm.removeUpdates(mLL);
//		listening = false;
//	}

	@Override
	protected void onResume() {
		super.onResume();
		showHomeLoc();
		if(listening)
			return;
		LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		Criteria cri = new Criteria(); cri.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = lm.getBestProvider(cri, true);
		if(provider != null)
			lm.requestLocationUpdates(provider, 1000, 0, mLL);
		listening = true;
	}

	public class MyLocListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			if(location == null)
				return;
			currLoc.setLatitude(location.getLatitude());
			currLoc.setLongitude(location.getLongitude());
			Log.d(TAG, "Location changed");
			updateCurrLoc();
			updateDist();
		}

		@Override
		public void onProviderDisabled(String provider) {}

		@Override
		public void onProviderEnabled(String provider) {}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		
	}
	
	public void getCurrentLocation(View v){
		if(currLoc == null || (currLoc.getLatitude() == 0 && currLoc.getLongitude() == 0))
			updateCurrLoc();
		Log.d(TAG,"getCurrentLoc endish");
		showHomeLoc();
	}
	
	public void setHome(View v){
		Location tmp = new Location(LocationManager.PASSIVE_PROVIDER);
		
		try{
			tmp.setLatitude(Double.parseDouble(((EditText)this.findViewById(R.id.latEditText)).getText().toString()));
			tmp.setLongitude(Double.parseDouble(((EditText)this.findViewById(R.id.lonEditText)).getText().toString()));
		}catch(NumberFormatException e){
			Toast.makeText(this, "Bad Location input!", Toast.LENGTH_SHORT).show();
			return;
		} catch(Exception e){
			Toast.makeText(this, "boogers!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return;
		}
		HCore hc = HCore.getInstance();
		hc.atHome = true;
		Log.d(TAG,"setHome1");
		hc.setHomeLoc(tmp);
		hc.updateProxAlert(this);
		Log.d(TAG,"setHome endish");
		showHomeLoc();
		Toast.makeText(this, "Home Location set!", Toast.LENGTH_SHORT).show();
		
//		if(currLoc != null){
//			HCore.getInstance().setHomeLoc(currLoc);
//			HCore.getInstance().updateProxAlert(this);
//			Toast.makeText(this, "Home Location set!", Toast.LENGTH_SHORT).show();
//		} else {
//			Toast.makeText(this, "Home Location NOT set!", Toast.LENGTH_LONG).show();
//		}
	}
	
	public void showHomeLoc(){
		Log.d(TAG,"showHomeLoc");
		HCore hc = HCore.getInstance();
		if(hc.getHomeLoc() != null){
			Location tmp = HCore.getInstance().getHomeLoc();
			TextView tv = (TextView)this.findViewById(R.id.vlatTV);
			tv.setText(Location.convert(tmp.getLatitude(), Location.FORMAT_DEGREES));
			TextView tv2 = (TextView)this.findViewById(R.id.vlonTV);
			tv2.setText(Location.convert(tmp.getLongitude(), Location.FORMAT_DEGREES));
			if(currLoc == null || (currLoc.getLatitude() == 0 && currLoc.getLongitude() == 0))
				updateCurrLoc();
			if(currLoc != null){
				updateDist();
			} else {
				Toast.makeText(this, "can't find current loc :(", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void updateDist(){
		HCore hc = HCore.getInstance();
		Location tmp = hc.getHomeLoc();
		if(tmp == null || (currLoc == null || (currLoc.getLatitude() == 0 && currLoc.getLongitude() == 0)))
			return;
		
		TextView tv3 = (TextView)this.findViewById(R.id.vDistTV);
		float d = tmp.distanceTo(currLoc);
		tv3.setText(d + "m away");
		if(d > 15){
			hc.atHome = false;
			hc.updateServer("entering");
		} else {
			hc.atHome = true;
			hc.updateServer("exiting");
		}
		TextView tv4 = (TextView)this.findViewById(R.id.vHomeTV);
		tv4.setText(HCore.getInstance().atHome ? "At home" : "Not Home");
	}
	
	public void updateCurrLoc(){
		LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		Criteria cri = new Criteria(); cri.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = lm.getBestProvider(cri, true);
		currLoc = lm.getLastKnownLocation(provider);
		if(currLoc == null || (currLoc.getLatitude() == 0 && currLoc.getLongitude() == 0))
			currLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(currLoc == null || (currLoc.getLatitude() == 0 && currLoc.getLongitude() == 0)) 
			currLoc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		Log.d(TAG,"updateCurrLoc mid");
		if(currLoc != null && currLoc.getLatitude() != 0 && currLoc.getLongitude() != 0){
			EditText latET = (EditText)this.findViewById(R.id.latEditText);
			latET.setText(currLoc.getLatitude() + "");
			EditText lonET = (EditText)this.findViewById(R.id.lonEditText);
			lonET.setText(currLoc.getLongitude() + "");
		} else {
			Toast.makeText(this, "No location fix", Toast.LENGTH_SHORT).show();
		}
		Log.d(TAG,"updateCurrLoc end");
	}
	
	
//	public void tglHome(View v){
//		HCore hc = HCore.getInstance();
//		hc.atHome = !hc.atHome;
//		hc.updateServer(hc.atHome ? "IM BACKKK" : "LATER LOSERS");
//		Log.d(TAG,"tglHome endish");
//		showHomeLoc();
//	}
}
