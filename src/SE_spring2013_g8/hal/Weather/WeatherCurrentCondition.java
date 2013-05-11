package SE_spring2013_g8.hal.Weather;

public class WeatherCurrentCondition {
	
	/**
	 * 
	 * WeatherCurrentConditions.java
	 * @author Mohak Tamhane
	 * Gets current weather.
	 */
	
	private String day,iconPath,humidity,condition,wind,city;
	private float temp;
	public void setDayOfWeek(String data) {
		// TODO Auto-generated method stub
		day = data;
	}

	public void setIconPath(String data) {
		// TODO Auto-generated method stub
		iconPath = data;
	}

	public void setCondition(String data) {
		// TODO Auto-generated method stub
		condition = data;
	}
	public void setTemp(float data)
	{
		temp = data;
	}
	public void setHumidity(String data)
	{
		humidity=data;
	}
	public void setWind(String data)
	{
		wind = data;
	}
	public String getDay()
	{
		return day;
	}
	public String getIconPath()
	{
		return iconPath;
	}
	public String getCondition(){
		return condition;
	}
	public String getWind()
	{
		return wind;
	}
	public String getHumidity(){
		return humidity;
	}
	public float getTemp()
	{
		return temp;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
