package SE_spring2013_g8.hal.Intercom;

import SE_spring2013_g8.hal.R;
import SE_spring2013_g8.hal.Main.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * ConnCallView class
 * 
 * ConnCallView is an activity class that displays the view for a connected call
 * 
 * @author Theopholus Mensah
 *
 */
public class ConnCallView extends Activity {

	/**
	 * Prepares and displays the connected call view
	 * 
	 * @param savedInstanceState an instance of the ConnCallView class
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intercom_connect);
		
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
