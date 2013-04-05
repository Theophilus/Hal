package SE_spring2013_g8.hal.Surveillance;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

/**
 * SurveillanceMainActivity Class
 * 
 * This class is the main screen which prompts the user to select between viewing surveillance video and streaming surveillance video
 * 
 * @author Steve
 *
 */
public class SurveillanceMainActivity extends Activity {

	/**
	 * Creates the main activity of the surveillance module
	 * 
	 * @param savedInstanceState the saved instance state
	 */	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.surveillance_activity_main);
	}

	/**
	 * 
	 * Creates the options menu
	 * 
	 * @param menu Accepts a menu to be inflated
	 * 
	 */	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * 
	 * @param view The button for launching the video streaming activity 
	 */	
	public void onClickClient(View view) {
		startActivity(new Intent(getBaseContext(), ClientActivity.class));
	}
	
	/**
	 * 
	 * @param view The button for launching the video viewing activity
	 */	
	public void onClickServer(View view) {
		startActivity(new Intent(getBaseContext(), ServerActivity.class));
	}

}
