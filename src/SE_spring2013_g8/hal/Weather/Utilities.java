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

public class Utilities {
	static float fToC(float temp)
	{
		return ((5.0f/9.0f)*(temp-32));
	}
	static float cToF(float temp)
	{
		return (((9.0f/5.0f)*(temp))+32);
	}
}
