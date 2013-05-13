package SE_spring2013_g8.hal.Climate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.sun.tools.javac.util.List;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.R.layout;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class ViewDevsActivity extends Activity {

	private static final String TAG = "ViewDevsActivity";
	public static final String PREF_ID = "1089";
	public static final String DEV_STRINGS_ID = "devStrings";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_devs);
		
		ListView lv = (ListView)this.findViewById(R.id.listView1);
		ProgressBar pBar = new ProgressBar(this);
    	pBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    	pBar.setIndeterminate(true);
		lv.setEmptyView(pBar);
		ViewGroup root = (ViewGroup)this.findViewById(android.R.id.content);
		root.addView(pBar);
		
		Log.d(TAG, "onCreate end");
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		ListView lv = (ListView)this.findViewById(R.id.listView1);
		SharedPreferences sp = this.getSharedPreferences(PREF_ID, 0);
		Vector<String> strs = new Vector<String>(sp.getStringSet(DEV_STRINGS_ID, 
													(Set<String>) new HashSet<String>()));
		
		if(strs.isEmpty()){ //pop an alert to tell the user there are no devices :/
			Log.w(TAG, "names empty");
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("No Devices currently in application, contact server.");
			builder.setTitle("No Devices!");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
				 public void onClick(DialogInterface dialog, int id) {dialog.dismiss();}});
			builder.setNegativeButton("IGNORE", new DialogInterface.OnClickListener(){
		         public void onClick(DialogInterface dialog, int id){dialog.dismiss();}});
			AlertDialog dialog = builder.create(); dialog.show();
		} else {
			Vector<String> dispStrs = new Vector<String>(strs.size());
			for(int i = 0; i < strs.size(); i++)
				dispStrs.add((Device.parseInfo(strs.get(i))) + " | " + (Device.bool ? "ON" : "OFF"));
			Log.d(TAG,"Here!");
			MyArrayAdapter adp = new MyArrayAdapter(this, android.R.layout.simple_list_item_1, dispStrs);
			lv.setAdapter(adp);
		}
		
		Log.d(TAG,"onResume end");
	}

	private class MyArrayAdapter extends ArrayAdapter<String> {
	
	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
	
	    public MyArrayAdapter(Context context, int textViewResourceId,
	    											Vector<String> dispStrs) {
	      super(context, textViewResourceId, dispStrs);
	      
	      for (int i = 0; i < dispStrs.size(); ++i) {//assign numbers
	        mIdMap.put(dispStrs.get(i), i);
	      }
	    }
	
	    @Override
	    public long getItemId(int position) {
	      String item = getItem(position);
	      return mIdMap.get(item);
	    }
	
	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }
	
	  }

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_devs, menu);
		return true;
	}
	
	public void refreshList(View v){
		//possibly check with server when implementing with server.
		onResume();
	}
	
	public void settsClicked(View v){
		Log.d(TAG, "settsClicked");
		Intent launchSett = new Intent(this, SettingsActivity.class);
		startActivity(launchSett);
	}

}
