package SE_spring2013_g8.hal.Intercom;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * BroadcastView class
 * 
 * BroadcastView is an activity class that displays the view that is shown when broadcast is clicked from the 
 * intercom home view 
 * 
 * @author Theophilus Mensah
 *
 */
public class BroadcastView extends Activity {

	/**
	 * onCreate prepares and displays the Broadcast view 
	 * @param savedInstanceState an instance of the BroadcastView activity class
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intercom_broadcast);
		
		}
	
	/**
	 * gotoAnnouncement redirects the user to the announcement activity when the announcement
	 * button is clicked in the intercom home view
	 * 
	 * @param view the view to be displayed
	 */
	public void gotoAnnouncement(View view) {
		   
		Intent intent = new Intent(this, AnnouncementView.class);
	    startActivity(intent);
	}
}
