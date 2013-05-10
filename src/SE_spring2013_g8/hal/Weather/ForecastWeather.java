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
