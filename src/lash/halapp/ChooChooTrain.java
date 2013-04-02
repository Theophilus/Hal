package lash.halapp;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ChooChooTrain {
	
	public static final String TAG = "ChooChooTrain";
	
	private static ChooChooTrain cct;
	private ArrayList<String> msgs;
	/**********************************************
	 * messages to be sent to home server
	 * General message Format:
	 * 	"$" + <DeviceName> + "^" + <command> + "$"
	 **********************************************/
	
	public static ChooChooTrain getInstance(){
		if(cct == null)
			cct = new ChooChooTrain();
		return cct;
	}
	
	private String buildMsg(){
		
		//TO-DO use msgs to build a single String
		
		Log.d(TAG, "end buildMsg");
		return "";
	}
	
	//returns true if success
	//NOTE: empties msgs if success
	public boolean sendMsgs(Context c){
		String cargo = buildMsg();
		
		//TO-DO establish connection and send msg, return true
		//may want to start service
		
		Toast toast = Toast.makeText(c, "msg sent", Toast.LENGTH_SHORT);
		toast.show();
		
		Log.d(TAG, "end sendMsgs, unsuccess");
		return false;
	}

	public void addMsg(String m){
		msgs.add(m);
	}
	
}