package SE_spring2013_g8.hal.Intercom;

import SE_spring2013_g8.hal.R;
import SE_spring2013_g8.hal.Main.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * HomeView class
 * 
 * HomeView is an activity class displays the home view of the intercom subsystem.
 * 
 * @author Theophilus Mensah
 *
 */
public class HomeView extends Activity {

	/**
	 * onCreate prepares and displays the home view
	 * 
	 * @param savedInstanceState an instance of the home view activity class
	 */
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.intercom_homeview);
	
	}
	
	/**
	 * gotoBroadcast redirects the user to the broadcast view when broadcast is clicked in the
	 * intercom home view.
	 * 
	 * @param view the view to be displayed
	 */
	public void gotoBroadcastView(View view) {
	   
		Intent intent = new Intent(this, BroadcastView.class);
	    startActivity(intent);
	}
	
	/**
	 * gotoP2PView redirects the user to the P2P view when P2P is clicked in the
	 * intercom home view
	 * 
	 * @param view the view to be displayed
	 */
	
	public void gotoP2PView(View view) {
		Intent intent = new Intent(this, P2PView.class);
	    startActivity(intent);
	}
	
	/**
	 * gotoConferenceView redirects the user to the conference view when conference 
	 * is clicked in the intercom home view.
	 * 
	 * @param view
	 */
	public void gotoConferenceView(View view) {
		
		Intent intent = new Intent(this, ConferenceView.class);
	    startActivity(intent);
	}
	
	/**
	 * exit_intercom redirects the user to the home view of the application
	 * 
	 * @param view the view to be displayed
	 */
	public void exit_intercom(View view) {
		
		Intent intent = new Intent(this, MainActivity.class);
	    startActivity(intent);
	}
}
