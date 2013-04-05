package SE_spring2013_g8.hal.Intercom;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * InCallView class
 * 
 * InCallView is an activity class that displays the view of an incoming call 
 * to the receiver.
 * 
 * @author Theophilus Mensah
 *
 */
public class InCallView extends Activity {

	/**
	 * onCreate prepares and displays the in coming call view
	 * 
	 * @param savedInstanceState an instance of the InCallView class
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intercom_incall);
		
		}
}
