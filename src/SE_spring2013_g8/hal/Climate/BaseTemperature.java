package SE_spring2013_g8.hal.Climate;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import SE_spring2013_g8.hal.R;

/**
 * BaseTemperature class
 * 
 *  BaseTemperature is an activity class that sets temperature for vacation mode and day and night mode.
 *  
 * 
 * @author Mohak Tamhane
 *
 */
public class BaseTemperature extends Activity implements OnClickListener {

	private Button nightInc;
	private Button nightDec;
	private Button dayInc;
	private Button dayDec;
	private TextView dayView;
	private TextView nightView;
	private Button save;

	private float dayTemp = 25f;
	private float nightTemp = 25f;
	private DecimalFormat format = new DecimalFormat("0.0");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_temperature);

		nightInc = (Button) findViewById(R.id.nightInc);
		nightDec = (Button) findViewById(R.id.nightDec);
		dayInc = (Button) findViewById(R.id.dayInc);
		dayDec = (Button) findViewById(R.id.dayDec);
		dayView = (TextView) findViewById(R.id.dayViewTemp);
		nightView = (TextView) findViewById(R.id.nightViewTemp);
		save = (Button) findViewById(R.id.buttonSave);

		nightInc.setOnClickListener(this);
		nightDec.setOnClickListener(this);
		dayInc.setOnClickListener(this);
		dayDec.setOnClickListener(this);
		save.setOnClickListener(this);

		SharedPreferences settings = getSharedPreferences("thermostat", 0);
		dayTemp = settings.getFloat("dayTemp", 25f);
		nightTemp = settings.getFloat("nightTemp", 25f);
		updateDay();
		updateNight();
	}

	private void updateDay() {
		dayView.setText(format.format(dayTemp) + " ¡C");
	}

	private void updateNight() {
		nightView.setText(format.format(nightTemp) + " ¡C");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nightInc:
			nightTemp += 0.1;
			updateNight();
			if (30 - nightTemp <= 0.000001) {
				nightInc.setEnabled(false);
			}
			nightDec.setEnabled(true);
			break;
		case R.id.nightDec:
			nightTemp -= 0.1;
			updateNight();
			if (Math.abs(5 - nightTemp) <= 0.000001) {
				nightDec.setEnabled(false);
			}
			nightInc.setEnabled(true);
			break;
		case R.id.dayInc:
			dayTemp += 0.1;
			updateDay();
			if (30 - dayTemp <= 0.000001) {
				dayInc.setEnabled(false);
			}
			dayDec.setEnabled(true);
			break;
		case R.id.dayDec:
			dayTemp -= 0.1;
			updateDay();
			if (Math.abs(5 - dayTemp) <= 0.000001) {
				dayDec.setEnabled(false);
			}
			dayInc.setEnabled(true);
			break;
		case R.id.buttonSave:
			SharedPreferences settings = getSharedPreferences("thermostat", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putFloat("dayTemp", dayTemp);
			editor.putFloat("nightTemp", nightTemp);
			editor.commit();
			onBackPressed();
			break;
		}
	}

}
