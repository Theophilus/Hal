
package SE_spring2013_g8.hal.Weather;
import java.util.ArrayList;

public class WeatherCollection {
private WeatherCurrentCondition current = null;
private ArrayList<ForecastWeather> forecast = new ArrayList<ForecastWeather>(4);

public WeatherCurrentCondition getCurrentConditions()
{
	return this.current;
	
}
public void setCurrentCondtions(WeatherCurrentCondition curr)
{
	this.current = curr;
}
public ArrayList<ForecastWeather> getForecastCondtions()
{
	return this.forecast;
}
public void setForecastConditions(ArrayList<ForecastWeather> list){
	this.forecast = list;
}
public ForecastWeather getLastForecastCondition() {
    return this.forecast.get(this.forecast.size() - 1);
}
}
