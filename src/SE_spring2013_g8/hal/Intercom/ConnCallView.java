package SE_spring2013_g8.hal.Intercom;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.os.Bundle;

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
}
