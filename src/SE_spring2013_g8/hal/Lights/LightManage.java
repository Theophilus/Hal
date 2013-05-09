package SE_spring2013_g8.hal.Lights;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class LightManage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_light_manage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.light_manage, menu);
		return true;
	}

}
