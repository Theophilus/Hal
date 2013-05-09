package SE_spring2013_g8.hal.Climate;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import SE_spring2013_g8.hal.R;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ThermostatActivity extends Activity implements OnClickListener {

	private TextView textTemp;
	private SeekBar barTemp;
	private Button buttonInc;
	private Button buttonDec;
	private Button buttonSettings;
	private Button buttonSend;
	private boolean changedByButton = false;
	private boolean vocationMod;
	private ImageView imageView;
	private boolean night;
	private ToggleButton vocation;
	
	/**
	 * Update daytime and temperature
	 */
	private void updateTemp() {
		List<String> values = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		int day = (7 + calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY) % 7;
		if (day == 0) {
			day = 7;
		}
		SharedPreferences prefs = getSharedPreferences("schedule", MODE_PRIVATE);
		values = Util.parseSchedule(prefs, day);
		night = true;
		boolean dayTime; // night = true, day = false
		String localMax = "00:00";
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		String shour = hour > 9 ? String.valueOf(hour) : "0"
				+ String.valueOf(hour);
		String sminute = minute > 9 ? String.valueOf(minute) : "0"
				+ String.valueOf(minute);
		String time = shour + ":" + sminute;
		if (values != null) {
			for (String value : values) {
				dayTime = value.charAt(0) == '1' ? true : false;
				if (value.substring(1).compareTo(localMax) >= 0
						&& value.substring(1).compareTo(time) <= 0) {
					localMax = value.substring(1);
					night = dayTime;
				}
			}
		}
		
		float temp;
		SharedPreferences settings = getSharedPreferences("thermostat", 0);
		if (night) {
			temp = settings.getFloat("nightTemp", 25f);
			imageView.setImageResource(R.drawable.moon);
		} else {
			temp = settings.getFloat("dayTemp", 25f);
			imageView.setImageResource(R.drawable.sun);
		}

		vocation.setOnCheckedChangeListener(vocationListener);
		vocationMod = settings.getBoolean("vocation", false);
		if (vocationMod) {
			temp = settings.getFloat("temp", 25f);
			imageView.setImageResource(R.drawable.aeroplane);
		}
		DecimalFormat format = new DecimalFormat("0.0");
		textTemp.setText(format.format(temp));
		barTemp.setProgress((int) temp - 5);
		if (vocationMod) {
			vocation.setChecked(true);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);				

		buttonInc = (Button) findViewById(R.id.buttonInc);
		buttonDec = (Button) findViewById(R.id.buttonDec);
		buttonSettings = (Button) findViewById(R.id.buttonSettings);
		barTemp = (SeekBar) findViewById(R.id.seekBarTemp);
		textTemp = (TextView) findViewById(R.id.TextViewTemp);
		buttonSend = (Button) findViewById(R.id.buttonSend);
		Button buttonMon = (Button) findViewById(R.id.buttonMon);
		Button buttonTue = (Button) findViewById(R.id.buttonTue);
		Button buttonWed = (Button) findViewById(R.id.buttonWed);
		Button buttonThi = (Button) findViewById(R.id.buttonThi);
		Button buttonFri = (Button) findViewById(R.id.buttonFri);
		Button buttonSat = (Button) findViewById(R.id.buttonSat);
		Button buttonSun = (Button) findViewById(R.id.buttonSun);
		imageView = (ImageView) findViewById(R.id.imageWeather);
		vocation = (ToggleButton) findViewById(R.id.toggleVocation);

		buttonInc.setOnClickListener(this);
		buttonDec.setOnClickListener(this);
		buttonMon.setOnClickListener(this);
		buttonTue.setOnClickListener(this);
		buttonWed.setOnClickListener(this);
		buttonThi.setOnClickListener(this);
		buttonFri.setOnClickListener(this);
		buttonSat.setOnClickListener(this);
		buttonSun.setOnClickListener(this);
		buttonSend.setOnClickListener(this);
		buttonSettings.setOnClickListener(this);
		imageView.setOnClickListener(this);


		updateTemp();
		barTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				String value = String.valueOf(progress + 5);
				textTemp.setText(value);
				if (barTemp.getProgress() == 0 && !changedByButton) {
					buttonDec.setEnabled(false);
				} else {
					buttonDec.setEnabled(true);
				}
				if (barTemp.getProgress() == 25) {
					buttonInc.setEnabled(false);
				} else {
					buttonInc.setEnabled(true);
				}
			}
		});

		
	}
	
	@Override
	protected void onResume() {
		updateTemp();
		super.onResume();
	};
	
	OnCheckedChangeListener vocationListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			SharedPreferences settings = getSharedPreferences("thermostat", 0);
			SharedPreferences.Editor editor = settings.edit();

			if (isChecked) {
				buttonInc.setEnabled(false);
				buttonDec.setEnabled(false);
				buttonSend.setEnabled(false);
				barTemp.setEnabled(false);
				vocationMod = true;
				imageView.setImageResource(R.drawable.aeroplane);
			} else {
				buttonInc.setEnabled(true);
				buttonDec.setEnabled(true);
				buttonSend.setEnabled(true);
				barTemp.setEnabled(true);
				vocationMod = false;
				if (night) {
					imageView.setImageResource(R.drawable.moon);
				} else {
					imageView.setImageResource(R.drawable.sun);
				}
			}
			float temp = Float.parseFloat(textTemp.getText().toString()
					.replace(',', '.'));
			editor.putBoolean("vocation", vocationMod);
			editor.putFloat("temp", temp);
			editor.commit();
		}
	};

	@Override
	public void onClick(View v) {
		DecimalFormat format = new DecimalFormat("0.0");
		float temp = Float.parseFloat(textTemp.getText().toString()
				.replace(',', '.'));
		Intent i = new Intent(ThermostatActivity.this, ScheduleActivity.class);
		switch (v.getId()) {
		case R.id.buttonInc:
			if (temp < 30) {
				temp += 0.1;
				changedByButton = true;
				barTemp.setProgress((int) temp - 5);
				changedByButton = false;
				textTemp.setText(format.format(temp));
				buttonDec.setEnabled(true);
			} else {
				buttonInc.setEnabled(false);
			}
			break;
		case R.id.buttonDec:
			if (temp > 5.1) {
				temp -= 0.1;
				changedByButton = true;
				barTemp.setProgress((int) temp - 5);
				changedByButton = false;
				textTemp.setText(format.format(temp));
				buttonInc.setEnabled(true);
			} else {
				if (temp == 5.1) {
					temp -= 0.1;
					textTemp.setText(format.format(temp));
				}
				buttonDec.setEnabled(false);
			}
			break;
		case R.id.buttonSend:
			Toast toast = Toast.makeText(this, "Temperature has been changed",
					Toast.LENGTH_SHORT);
			toast.show();
			break;
		case R.id.buttonMon:
			i.putExtra("day", 1);
			startActivity(i);
			break;
		case R.id.buttonTue:
			i.putExtra("day", 2);
			startActivity(i);
			break;
		case R.id.buttonWed:
			i.putExtra("day", 3);
			startActivity(i);
			break;
		case R.id.buttonThi:
			i.putExtra("day", 4);
			startActivity(i);
			break;
		case R.id.buttonFri:
			i.putExtra("day", 5);
			startActivity(i);
			break;
		case R.id.buttonSat:
			i.putExtra("day", 6);
			startActivity(i);
			break;
		case R.id.buttonSun:
			i.putExtra("day", 7);
			startActivity(i);
			break;
		case R.id.buttonSettings:
			i = new Intent(ThermostatActivity.this, BaseTemperature.class);
			startActivity(i);
			break;
		case R.id.imageWeather:
			updateTemp();
			break;
		}
	}
}