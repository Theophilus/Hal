package SE_spring2013_g8.hal.Weather;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;
import SE_spring2013_g8.hal.R;
public class AboutActivity extends Activity {
@Override
public void onCreate(Bundle b){
	super.onCreate(b);
	setContentView(R.layout.activity_about);
	TextView version = (TextView)findViewById(R.id.versionLabel);
	PackageManager pm = getBaseContext().getPackageManager();
	PackageInfo info;
	try {
		info = pm.getPackageInfo(getBaseContext().getPackageName(), 0);
		version.setText(info.versionName);
	} catch (NameNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
}
