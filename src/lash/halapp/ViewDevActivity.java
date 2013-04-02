package lash.halapp;

import SE_spring2013_g8.hal.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.ListView;

public class ViewDevActivity extends Activity {

	public static final String TAG = "MainActivity";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdev);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
     	// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);   	
        
        setContentView(R.layout.activity_viewdev);
        
    	ProgressBar pBar = new ProgressBar(this);
    	pBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    	pBar.setIndeterminate(true);
    	((ListView) this.findViewById(R.id.appsListView)).setEmptyView(pBar);

        return true;
    }


	@Override
	protected void onResume() {
		super.onResume();
		
		Log.d(TAG, "onResume end");
	}
	
	public void launchSettings(View viewIn){
		Intent launchSett = new Intent(this, SettingsActivity.class);
		startActivity(launchSett);
	}
	
    
}
