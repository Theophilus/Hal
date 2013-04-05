package SE_spring2013_g8.hal.Intercom;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * AnnouncementView class
 * 
 * AnnouncementView is an activity class displays the view that is shown on the receiving device when 
 * an announcement is running 
 * 
 * @author Theophilus Mensah
 *
 */
public class AnnouncementView extends Activity {
	/** 
	 * onCreate prepares and displays the Announcement view 
	 * @param savedInstanceState an instance of the AnnouncementView activity class
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intercom_announcement);
		
		}

}
