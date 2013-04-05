package SE_spring2013_g8.hal.Intercom;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * P2PView class
 * 
 * P2PView is an activity class that displays the P2P view
 * 
 * @author Theophilus Mensah
 *
 */
public class P2PView extends Activity {
	
	/**
	 * onCreate prepares and displays the P2P view
	 * 
	 * @param savedInstanceState an instance of the P2PView Class
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intercom_p2p);
		
		}
	
	/**
	 * gotoHome redirects the user to the home view of the intercom system
	 * when cancel is clicked.
	 * 
	 * @param view the view to be displayed
	 */

	public void gotoHome(View view) {
		Intent intent = new Intent(this, HomeView.class);
	    startActivity(intent);
	}
	
	/**
	 * gotoConnCall redirects the user to the Connected call view when a
	 * call is accepted by the receiver.
	 * 
	 * @param view the view to be displayed
	 */
	public void gotoConnCall(View view) {
		Intent intent = new Intent(this, ConnCallView.class);
	    startActivity(intent);
	}
	/**
	 * gotoDail redirects the user to the dialing view when call is clicked 
	 * in the P2P view
	 * @param view the view to be displayed
	 */
	public void gotoDial(View view) {
		Intent intent = new Intent(this, DialView.class);
	    startActivity(intent);
	}
	
}
