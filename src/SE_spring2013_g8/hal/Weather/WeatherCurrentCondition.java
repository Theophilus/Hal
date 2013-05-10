//  Copyright 2012 Doug Mason
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

package SE_spring2013_g8.hal.Weather;

public class WeatherCurrentCondition {
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
