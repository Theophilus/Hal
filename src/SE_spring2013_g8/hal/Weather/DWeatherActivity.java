package SE_spring2013_g8.hal.Weather;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView.OnEditorActionListener;
import android.location.*;
public class DWeatherActivity extends Activity implements  OnClickListener,OnEditorActionListener,OnCheckedChangeListener {
    private WUEngine engine;
    /**
	 * @return the engine
	 */
	public WUEngine getEngine() {
		return engine;
	}


	/**
	 * @param engine the engine to set
	 */
	public void setEngine(WUEngine engine) {
		this.engine = engine;
	}

	final String PREFS_FILE = "DWeatherPreferences";
    final String WEATHER_PREF = "WEATHER_PREF";
    private Object col;
    /**
	 * @return the col
	 */
	public Object getCol() {
		return col;
	}


	/**
	 * @param col the col to set
	 */
	public void setCol(Object col) {
		this.col = col;
	}

	private String city;
    /**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}


	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	LocationManager manager;
    private CurrentConditionsAdapter conditionsAdapter;
    private ForecastConditionsAdapter forecastAdapter;
    ArrayList<String> autoComplete;
    /**
	 * @return the autoComplete
	 */
	public ArrayList<String> getAutoComplete() {
		return autoComplete;
	}


	/**
	 * @param autoComplete the autoComplete to set
	 */
	public void setAutoComplete(ArrayList<String> autoComplete) {
		this.autoComplete = autoComplete;
	}

	Button submit;
    EditText cityText;
    ListView currentList,forecastList;
    CheckBox inC,currentLocation;
    SharedPreferences preference;
    TextView cityLabel;
    class WeatherEngineAsyncTask extends AsyncTask<DWeatherActivity,Void,DWeatherActivity>{

		@Override
		protected DWeatherActivity doInBackground(DWeatherActivity... activity) {
			// TODO Auto-generated method stub
			DWeatherActivity dw = activity[0];
			final WUEngine engine = dw.getEngine();
			Object col = dw.getCol();
			try
			{
				
				city = cityText.getText().toString();
				col = engine.getWeather(dw.getCity(),dw);
				if(engine.isAutoComplete){
					JSONArray cities = (JSONArray)col;
					Log.v("Test",cities.get(0).toString());
					autoComplete = new ArrayList<String>();
					for(int i = 0; i < ((JSONArray)col).length();i++){
						JSONObject object = (JSONObject) cities.get(i);
						autoComplete.add(object.get("city").toString()+" "+object.get("state").toString());
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(dw);
					builder.setTitle("Which city did you want the weather for?");
					CharSequence[] chars = autoComplete.toArray(new CharSequence[autoComplete.size()]);
					builder.setItems(chars,new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog, int item) {
					         cityText.setText(autoComplete.get(item)) ;
					         engine.isAutoComplete = false;
					        fetchWeather();
					    }
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
				else{
					updateDisplay();
					//Save city to shared preferences
				    SharedPreferences.Editor edit = preference.edit();
				    edit.putString(WEATHER_PREF,cityText.getText().toString() );
				    edit.commit();
					
					
				}

				//Current
				
			}
			catch(Exception e)
			{
				Log.e("ERROR!","Weather query error",e);
			}
			dw.setCol(col);
			return dw;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(DWeatherActivity result) {
			// TODO Auto-generated method stub
			result.updateDisplay();
		}
		
    	
    }
    private class CurrentConditionsAdapter extends ArrayAdapter<WeatherCurrentCondition>{

    	public CurrentConditionsAdapter(Context context, int resource,
    			int textViewResourceId, List<WeatherCurrentCondition> objects) {
    		super(context, resource, textViewResourceId, objects);
    		// TODO Auto-generated constructor stub
    		
    	}

    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		// TODO Auto-generated method stub
    		TextView currentTemp,currentWind,currentDay,currentHumidity,currentCondition;
    		View row = convertView;
    		if(row == null){
    			LayoutInflater inflater = getLayoutInflater();
    			row = inflater.inflate(R.layout.current_conditions, parent, false);
    			row.setTag("CurrentCondtions");
    		}
    		ImageView im = (ImageView) row.findViewById(R.id.currentImage);
            currentWind = (TextView) row.findViewById(R.id.currentWindText);
            currentHumidity=(TextView)row.findViewById(R.id.currentHumidityText);
            currentCondition=(TextView)row.findViewById(R.id.currentConditionText);
            currentDay = (TextView)row.findViewById(R.id.currentDayText);
            currentTemp = (TextView)row.findViewById(R.id.currentDegreeText); 
    		if(!(inC.isChecked())){
    		currentTemp.setText(String.format("%.2f",((WeatherCollection)col).getCurrentConditions().getTemp())+"¡F");
    		currentDay.setText(((WeatherCollection)col).getCurrentConditions().getDay());
    		currentWind.setText("Wind is: "+((WeatherCollection)col).getCurrentConditions().getWind());
    		currentHumidity.setText("Current humidity: "+((WeatherCollection)col).getCurrentConditions().getHumidity());
    		currentCondition.setText(((WeatherCollection)col).getCurrentConditions().getCondition());

    		}
    		else
    		{
    			currentTemp.setText(String.format("%.2f",Utilities.fToC(((WeatherCollection)col).getCurrentConditions().getTemp()))+"¡C");
    			currentDay.setText(((WeatherCollection)col).getCurrentConditions().getDay());
    			currentWind.setText("Wind is: "+((WeatherCollection)col).getCurrentConditions().getWind());
    			currentHumidity.setText("Current humidity: "+((WeatherCollection)col).getCurrentConditions().getHumidity());
    			currentCondition.setText(((WeatherCollection)col).getCurrentConditions().getCondition());
    			
    		}
    		setImageForURL(im,((WeatherCollection)col).getCurrentConditions().getIconPath());
    		return row;
    		
    	}
    	
    }
    private class ForecastConditionsAdapter extends ArrayAdapter<ForecastWeather>{
    	
    	public ForecastConditionsAdapter(Context context, int resource,
    			int textViewResourceId, List<ForecastWeather> objects) {
    		super(context, resource, textViewResourceId, objects);
    		// TODO Auto-generated constructor stub
    	}

    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		View row = convertView;
    		ForecastWeather forecast = this.getItem(position);
    		if(row == null){
    			LayoutInflater inflater = getLayoutInflater();
    			row = inflater.inflate(R.layout.forecast_conditions, parent, false);
    			row.setTag("Forecast "+Integer.toString(position));
    		}
            TextView forecastDay = (TextView)row.findViewById(R.id.forecastLabel);
            TextView forecastHigh = (TextView)row.findViewById(R.id.forecastHighText); 
            TextView forecastLow = (TextView)row.findViewById(R.id.forecastLowText);
            TextView forecastCondtion = (TextView)row.findViewById(R.id.forecastConditionText);
            ImageView forecastImage = (ImageView)row.findViewById(R.id.forecastImage);
    		
            forecastDay.setText(forecast.getDay());
            if(!inC.isChecked()){
    		forecastLow.setText("Low: "+ String.format("%.2f",forecast.getLowTemp())+"¡F");
    		forecastHigh.setText("High: "+String.format("%.2f",forecast.getHighTemp())+"¡F");
            }
            else{
    			forecastLow.setText("Low: "+ String.format("%.2f",Utilities.fToC(forecast.getLowTemp())+"¡C"));
    			forecastHigh.setText("High: "+String.format("%.2f",Utilities.fToC(forecast.getHighTemp())+"¡C"));
            }
            forecastCondtion.setText(forecast.getCondition());
            setImageForURL(forecastImage,forecast.getIcon());
    		return row;
    		
    	}
    }

/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weatherlists);
        engine = new WUEngine();
        autoComplete = new ArrayList<String>();
        submit = (Button)findViewById(R.id.goButton);
        submit.setOnClickListener(this);
        cityText = (EditText)findViewById(R.id.cityText);
        inC = (CheckBox)findViewById(R.id.inCCheck);
        inC.setOnCheckedChangeListener(this);
        manager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean hasGPS =  manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!hasGPS){
        	
        }
        else{
        	
        }
        currentLocation = (CheckBox)findViewById(R.id.locationCheckBox);
        currentLocation.setOnCheckedChangeListener(this);
        preference = getSharedPreferences(PREFS_FILE,MODE_PRIVATE);
        cityLabel=(TextView)findViewById(R.id.cityWeatherLabel);
        cityText.setOnEditorActionListener(this);
        currentList = (ListView)findViewById(R.id.currentForecast);
        forecastList= (ListView)findViewById(R.id.forecastCondtionsList);
        //    	public CurrentConditionsAdapter(Context context, int resource,int textViewResourceId, List<WeatherCurrentCondition> objects)

	}


private void setImageForURL(ImageView image, String iconPath) {
	// TODO Auto-generated method stub
	try
	{
	URL toConnect = new URL(iconPath);
	URLConnection connect = toConnect.openConnection();
	connect.connect();
	InputStream in = connect.getInputStream();
	BufferedInputStream bufferedStream = new BufferedInputStream(in);
	Bitmap map = BitmapFactory.decodeStream(bufferedStream);
	bufferedStream.close();
	in.close();
	image.setImageBitmap(map);
	}
	catch(Exception e)
	{
		Log.v("Exception!",e.toString());
		image.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
	}
	
}
@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	// TODO Auto-generated method stub
	switch(buttonView.getId()){
	case R.id.locationCheckBox:
		break;
	case R.id.inCCheck:
		break;
	}
	
}
@Override
public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	// TODO Auto-generated method stub
	return false;
}
@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch(v.getId())
	{
	case R.id.goButton:
		new WeatherEngineAsyncTask().execute(this);
		break;
	}
}


private void fetchWeather(){
	
}

private void updateDisplay()
{
	cityLabel.setText("Weather for "+((WeatherCollection)col).getCurrentConditions().getCity());
	if(this.conditionsAdapter==null){
		ArrayList<WeatherCurrentCondition> current = new ArrayList<WeatherCurrentCondition>();
		current.add(((WeatherCollection)col).getCurrentConditions());
		conditionsAdapter= new CurrentConditionsAdapter(this,R.layout.current_conditions,android.R.layout.simple_list_item_1,current );
        currentList.setAdapter(conditionsAdapter);
	}
	else{
		conditionsAdapter.notifyDataSetChanged();
	}
	if(this.forecastAdapter == null){
		forecastAdapter = new ForecastConditionsAdapter(this,R.layout.forecast_conditions,android.R.layout.simple_list_item_1,((WeatherCollection)col).getForecastCondtions());
        forecastList.setAdapter(forecastAdapter);
	}
	else {
		forecastAdapter.notifyDataSetChanged();
	}

	
}
}
