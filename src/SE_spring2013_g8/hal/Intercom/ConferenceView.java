package SE_spring2013_g8.hal.Intercom;

import SE_spring2013_g8.hal.R;
import SE_spring2013_g8.hal.Main.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**
 * ConferenceView class
 * 
 * ConferenceView is an activity class that displays the view of the conference option
 * 
 * @author Theophilus Mensah
 *
 */
public class ConferenceView extends Activity {
	
	/**
	 * onCreate prepares and displays the conference view 
	 * @param savedInstanceState an instance of the ConferenceView activity class
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intercom_conference);
		
		}
	/**
	 * gotoHome redirects the user to the home view when cancel is clicked
	 * 
	 * @param view the view to be displayed
	 */
	
	public void gotoHome(View view) {
		Intent intent = new Intent(this, HomeView.class);
	    startActivity(intent);
	}
	
	/**
	 * gotoConnCall redirects the user to the connected call view when a call is accepted
	 * 
	 * @param view the view to be displayed
	 */
	
	public void gotoConnCall(View view) {
		Intent intent = new Intent(this, ConnCallView.class);
	    startActivity(intent);
	}
	
	@Override
	/**
	 * Method to go back to photo list if back button is pressed
	 */
	public boolean onKeyDown(int keycode, KeyEvent event){
		if (keycode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent(this, HomeView.class);
			startActivity(intent);
	        return true;
	    }
		
		return false;
		
	}

}
