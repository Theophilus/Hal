package SE_spring2013_g8.hal.Climate;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

/**
 * Climate Control Class
 * 
 * This class contains functionality for controlling climate
 * 
 * @author Mohak Tamhane
 *@version 1.0
 */

public class ClimateControl extends Activity {
	
/**
 * Connects the CLimate control module to the main.	
 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.climate_control);
	}

}
