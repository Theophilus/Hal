

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
