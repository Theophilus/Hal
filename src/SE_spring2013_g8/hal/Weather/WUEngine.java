/**
 * WUengine.java
 * Implementing JSON parsing for weather underground API.
 */

package SE_spring2013_g8.hal.Weather;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;


public class WUEngine {
private static final String API_KEY = "26cbe5d65c74086f";
public boolean isAutoComplete = false;
public  Object getWeather(String location,Context context)
{
	WeatherCollection weatherCollectionToReturn = new WeatherCollection();
	ArrayList<ForecastWeather> forecastArrayList = new ArrayList<ForecastWeather>();
	String requestURL = "http://api.wunderground.com/api/"+API_KEY+"/conditions/q/"+location.replace(" ", "%20")+".json";
	String forecastURL = "http://api.wunderground.com/api/"+API_KEY+"/forecast/q/"+location.replace(" ", "%20")+".json";
	try{
	JSONObject object = getJSONFromURL(requestURL,context);
	JSONObject response = object.getJSONObject("response");
	try{
		JSONArray autoComplete = response.getJSONArray("results");
		this.isAutoComplete = true;
		return autoComplete;
	}
	catch(JSONException e){
		
	}

	JSONObject currentObject = object.getJSONObject("current_observation");
	JSONObject currentLocation = currentObject.getJSONObject("display_location");
	JSONObject forecastRequest = getJSONFromURL(forecastURL,context);
	JSONObject forecastObject = forecastRequest.getJSONObject("forecast");
	JSONArray simpleForecastArray = forecastObject.getJSONObject("simpleforecast").getJSONArray("forecastday");
	if(simpleForecastArray.length() == 0)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage("Unable to retreive weather.");
		AlertDialog weatherAlert = builder.create();
		weatherAlert.show();
	}
	else{
		Log.v("Current Observation",currentObject.toString());
		Log.v("current location",currentLocation.getString("full"));
		
		WeatherCurrentCondition currentConditions = new WeatherCurrentCondition();
		String currentDate = currentObject.getString("observation_time_rfc822");
		currentConditions.setDayOfWeek(currentDate.substring(0, currentDate.indexOf(",")));
		currentConditions.setTemp(Float.parseFloat(currentObject.getString("temp_f")));
		currentConditions.setCondition(currentObject.getString("weather"));
		currentConditions.setIconPath(currentObject.getString("icon_url"));
		currentConditions.setCity(currentLocation.getString("full"));
		currentConditions.setHumidity(currentObject.getString("relative_humidity"));
		currentConditions.setWind(currentObject.getString("wind_string"));
		weatherCollectionToReturn.setCurrentCondtions(currentConditions);
		}
	for(int i = 0; i < simpleForecastArray.length();i++)
		{
			/**
			 * I don't currently care about overnight forecasts, since nights are odd forecast dates, I ignore them.
			 * 
			 */
				ForecastWeather newForecast = new ForecastWeather();
				JSONObject currentSimpleForecastObject = simpleForecastArray.getJSONObject(i);
				JSONObject currentForecastDateObject = currentSimpleForecastObject.getJSONObject("date");
				newForecast.setIconPath(currentSimpleForecastObject.getString("icon_url"));
				newForecast.setCondition(currentSimpleForecastObject.getString("conditions"));
				newForecast.setDay(Integer.toString(currentForecastDateObject.getInt("month"))+"/"+Integer.toString(currentForecastDateObject.getInt("day"))+"/"+Integer.toString(currentForecastDateObject.getInt("year")) );
				newForecast.setHighTemp(Float.parseFloat(currentSimpleForecastObject.getJSONObject("high").getString("fahrenheit")));
				newForecast.setLowTemp(Float.parseFloat(currentSimpleForecastObject.getJSONObject("low").getString("fahrenheit")));
				newForecast.setDayOfWeek(currentForecastDateObject.getString("weekday_short"));
				forecastArrayList.add(newForecast);
		}
	weatherCollectionToReturn.setForecastConditions(forecastArrayList);
	}
	catch(Exception e)
		{
			
		}
	return weatherCollectionToReturn;
}
private static JSONObject getJSONFromURL(String requestURL,Context context)
{
	JSONObject theObjectToReturn = null;
	String query = requestURL.replace(" ", "%20");

	String conditionsJSON = "";
	try
	{
		URL weatherURL = new URL(query);
		HttpURLConnection weatherRequest = (HttpURLConnection) weatherURL.openConnection();
		String weatherResponse =weatherRequest.getResponseMessage();
		if(!(weatherResponse.equalsIgnoreCase("OK")))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage("Unable to retreive weather."+weatherResponse);
			AlertDialog weatherAlert = builder.create();
			weatherAlert.show();
		}
		else
		{
			try{
				InputStream conditionsStream = new BufferedInputStream( weatherRequest.getInputStream());
				Log.v("Response String",conditionsStream.toString());
				BufferedReader conditionsReader =new BufferedReader(new InputStreamReader(conditionsStream));
				StringBuilder conditionsBuilder = new StringBuilder();
				String currentLine = null;
				while ((currentLine = conditionsReader.readLine())!=null)
				{
					conditionsBuilder.append(currentLine+"\n");
					
				}
				conditionsJSON = conditionsBuilder.toString();
				theObjectToReturn = new JSONObject(conditionsJSON);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return theObjectToReturn;
}
}
