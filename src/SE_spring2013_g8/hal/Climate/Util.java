package SE_spring2013_g8.hal.Climate;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;

public class Util {
	
	/**
	 * Util class
	 * 
	 * 
	 * @author Mohak Tamhane
	 *
	 */

	public static void addShedule(SharedPreferences prefs, int day,
			boolean day_night, int hour, int minute) {
		// day - 1, night - 0
		String s = prefs.getString(String.valueOf(day), "");
		String shour = hour > 9 ? String.valueOf(hour) : "0"
				+ String.valueOf(hour);
		String sminute = minute > 9 ? String.valueOf(minute) : "0"
				+ String.valueOf(minute);
		String time = shour + ":" + sminute;
		int flag = 0;
		if (day_night) {
			flag = 1;
		}
		if (s.equals("")) {
			prefs.edit()
					.putString(String.valueOf(day),
							String.valueOf(flag) + time + " ").commit();
		} else {
			prefs.edit()
					.putString(String.valueOf(day),
							s + String.valueOf(flag) + time + " ").commit();
		}
	}

	public static List<String> parseSchedule(SharedPreferences prefs, int day) {
		List<String> result = new ArrayList<String>();

		String s = prefs.getString(String.valueOf(day), "");
		CheckSchedule.DtN[day] = 0;
		CheckSchedule.NtD[day] = 0;
		if (s.equals("")) {
			return null;
		} else {
			String[] str = s.split(" ");
			for (int i = 0; i < str.length; i++) {
				result.add(str[i]);
				if (CheckSchedule.flag) {
					if (result.get(i).charAt(0) == '1') {
						CheckSchedule.DtN[day]++;
					} else {
						CheckSchedule.NtD[day]++;
					}
				}
			}
			CheckSchedule.flag = false;
			return result;
		}
	}
}