package SE_spring2013_g8.hal.Climate;

import java.util.ArrayList;
import java.util.List;
import SE_spring2013_g8.hal.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleActivity extends Activity implements OnClickListener {
	/**
	 * ScheduleActivity class
	 * 
	 *  ScheduleActivity is an activity class that creates schedule for climate control.
	 *  
	 * 
	 * @author Mohak Tamhane
	 *
	 */

  private int day = 0;
  private ScheduleAdapter adapter = null;
  private ListView list;
  private TextView text;
  Button buttonAdd = null;
  private Button buttonSend;
  String r = null;
  List<String> values = new ArrayList<String>();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.schedule);

    buttonAdd = (Button) findViewById(R.id.buttonAdd);
    buttonSend = (Button) findViewById(R.id.buttonSend);
    
    list = (ListView) findViewById(R.id.listViewSchedule);
    text = (TextView) findViewById(R.id.textViewNoSchedul);
    list.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> a, View v, int position,
          long id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(
            ScheduleActivity.this);
        adb.setTitle("Delete?");
        adb.setMessage("Are you sure you want to delete this time?");
        final int positionToRemove = position;
        adb.setNegativeButton("Cancel", null);
        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            r = values.get(positionToRemove);
            values.remove(positionToRemove);
            adapter.notifyDataSetChanged();
          }
        });
        adb.show();
      }
    });
    
    day = getIntent().getIntExtra("day", 0);
    SharedPreferences prefs = getSharedPreferences("schedule", MODE_PRIVATE);

    values = Util.parseSchedule(prefs, day);
    if (values != null && values.size() == 10) {
      buttonAdd.setEnabled(false);
    }

    if (values != null) {
      adapter = new ScheduleAdapter(ScheduleActivity.this,
          R.layout.add_schedule, values);
      list.setAdapter(adapter);
    } else {
      list.setVisibility(View.GONE);
      text.setVisibility(View.VISIBLE);
    }
    
    buttonAdd.setOnClickListener(this);
    buttonSend.setOnClickListener(this);

  }
  
  @Override
  public void onClick(View v) {
    SharedPreferences prefs;
    String str[] = new String[10];
    prefs = getSharedPreferences("schedule", MODE_PRIVATE);
    switch (v.getId()) {
    case R.id.buttonAdd:
      Intent i = new Intent(ScheduleActivity.this,
          AddScheduleActivity.class);
      i.putExtra("day", day);
      startActivity(i);
      break;
    case R.id.buttonSend:
      if (values!=null){
      SharedPreferences settings = getSharedPreferences("schedule", 0);
      Editor e = settings.edit();
      e.clear();
      e.commit();
      
      for (int j = 0; j<values.size(); j++)
      {
        boolean day_night;
        char buf = values.get(j).charAt(0);
        if  (buf=='1')
        {
          day_night = true;
          CheckSchedule.DtN[day]--;
        }
        else{
          day_night = false;
          CheckSchedule.NtD[day]--;
        }
        str = values.get(j).split(":");
        String s = str[0].replace(str[0].charAt(0), '0');
        int hour = Integer.parseInt(s);
        int minute = Integer.parseInt(str[1]);
        Util.addShedule(prefs, day, day_night, hour, minute);
      }
      Toast toast = Toast.makeText(this, "Schedule has been sent",
          Toast.LENGTH_SHORT);
      toast.show();
      }
      else
      {
        Toast toast = Toast.makeText(this, "Schedule is not set, set at least one transition",
            Toast.LENGTH_SHORT);
        toast.show();
      }
      if (values!=null && values.size() < 10) {
        buttonAdd.setEnabled(true);
      }
      
      break;
    }

  }

  @Override
  protected void onResume() {
    SharedPreferences prefs = getSharedPreferences("schedule", MODE_PRIVATE);
    CheckSchedule.flag = true;
    values = Util.parseSchedule(prefs, day);
    if (values != null) {
      adapter = new ScheduleAdapter(ScheduleActivity.this,
          R.layout.add_schedule, values);
      list.setAdapter(adapter);
      adapter.notifyDataSetChanged();
      list.setVisibility(View.VISIBLE);
      text.setVisibility(View.GONE);
    }
    if (values != null && values.size() == 10) {
      buttonAdd.setEnabled(false);
    }

    super.onResume();
  }

  public class ScheduleAdapter extends ArrayAdapter<String> {
    public ScheduleAdapter(Context context, int textViewResourceId,
        List<String> objects) {
      super(context, textViewResourceId, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView,
        ViewGroup parent) {
      return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
        ViewGroup parent) {
      LayoutInflater inflater = getLayoutInflater();
      View row = inflater.inflate(R.layout.schedule_row, parent, false);
      TextView rowTime = (TextView) row.findViewById(R.id.rowTime);
      TextView rowDayNight = (TextView) row
          .findViewById(R.id.rowDayNight);
      if (values != null) {
        if (values.get(position).charAt(0) == '1') {

          rowDayNight.setText("Day to night");
        } else {

          rowDayNight.setText("Night to day");
        }
        rowTime.setText(values.get(position).substring(1));
      }

      return row;
    }
  }

}