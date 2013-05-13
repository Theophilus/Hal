package SE_spring2013_g8.hal.Intercom;

import SE_spring2013_g8.hal.R;
import SE_spring2013_g8.hal.Main.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

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
