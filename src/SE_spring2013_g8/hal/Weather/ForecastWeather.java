package SE_spring2013_g8.hal.Weather;

public class ForecastWeather {
	private float lowTemp,highTemp;
	private String day,icon,condition;
	
	public void setDayOfWeek(String data) {
		// TODO Auto-generated method stub
		this.day = data;
	}

	public void setIconPath(String data) {
		// TODO Auto-generated method stub
		this.icon = data;
	}

	public void setCondition(String data) {
		// TODO Auto-generated method stub
		this.condition = data;
	}
	
	public void setTempLow(float data)
	{
		this.setLowTemp(data);
	}
	public void setTempHigh(float data)
	{
		this.setHighTemp(data);
	}

	public float getLowTemp() {
		return lowTemp;
	}

	public void setLowTemp(float lowTemp) {
		this.lowTemp = lowTemp;
	}

	public float getHighTemp() {
		return highTemp;
	}

	public void setHighTemp(float highTemp) {
		this.highTemp = highTemp;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String getCondition()
	{
		return this.condition;
	}
}
