package SE_spring2013_g8.hal.Climate;

import SE_spring2013_g8.hal.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TimePicker;

public class AddScheduleActivity extends Activity implements OnClickListener {

	private TimePicker timePicker;
	private RadioButton radioDay;
	private int day;
	Button buttonAdd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_schedule);

		timePicker = (TimePicker) findViewById(R.id.timePicker);
		buttonAdd = (Button) findViewById(R.id.buttonAddShedule);
		radioDay = (RadioButton) findViewById(R.id.radioDay);
		timePicker.setIs24HourView(true);

		radioDay.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked && CheckSchedule.DtN[day] < 5) {
					buttonAdd.setEnabled(true);
				} else {
					if (!isChecked && CheckSchedule.NtD[day] < 5) {
						buttonAdd.setEnabled(true);
					} else {
						buttonAdd.setEnabled(false);
					}
				}
			}
		});

		day = getIntent().getIntExtra("day", 0);
		if (CheckSchedule.DtN[day] >= 5 && radioDay.isChecked()) {
			buttonAdd.setEnabled(false);
		}

		if (CheckSchedule.NtD[day] >= 5 && !radioDay.isChecked()) {
			buttonAdd.setEnabled(false);
		}

		buttonAdd.setOnClickListener(this);
		;

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.buttonAddShedule:
			int hour = timePicker.getCurrentHour();
			int minute = timePicker.getCurrentMinute();
			SharedPreferences prefs = getSharedPreferences("schedule",
					MODE_PRIVATE);
			boolean day_night = radioDay.isChecked();

			Util.addShedule(prefs, day, day_night, hour, minute);
			onBackPressed();
			break;
		}
	}

}