package SE_spring2013_g8.hal.Intercom;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * DailView class
 * 
 * DailView is an activity class that is displays to the receiver when there is an incoming call. 
 * 
 * @author Theophilus Mensah
 *
 */
public class DialView extends Activity {

	/**
	 * onCreate prepares and displays the Dial view
	 * 
	 * @param savedInstanceState an instance of the dail view class
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intercom_dail);
		
		}
	
	/**
	 * gotoHome redirects the user to the home view when cancel is clicked.
	 * 
	 * @param view the view to be displayed
	 */
	public void gotoHome(View view) {
		Intent intent = new Intent(this, HomeView.class);
	    startActivity(intent);
	}
}
