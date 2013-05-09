package SE_spring2013_g8.hal.Lights;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * LightControl class
 * 
 * Class to control the lights by relaying user inputs to a server
 * 
 * @author Mike
 *
 */
public class LightControl extends Activity {
	/**
	 * nickname sent to server to confirm connection
	 */
	static final String NICKNAME = "HAL Online";
	/**
	 * type inetaddress used to create a socket
	 */
	InetAddress serverAddress;
	/**
	 * holds the location of the socket in memory
	 */
	Socket socket;
	/**
	 * holds the IP address input by the user
	 */
	String ipAddress;
	/**
	 * holds the position of the item selected in the spinner
	 */
	String lightNum;
	/**
	 * String to hold the blinds id
	 */
	String blindNum;
	/**
	 * holds the message received by the server
	 */
	static TextView txtMessagesReceived;
	/**
	 * holds the input from the edit text box
	 */
	EditText txtMessage;
	/**
	 * Holds the class that creates the socket and communicates across the created socket
	 */
	CommsThread commsThread;
	/**
	 * updates the UI on the main activity
	 */
	static Handler UIupdater = new Handler() {
	};
	/**
	 * spinner handler for lights
	 */
	ArrayAdapter<String> ladapter;
	
	File SavedIP;
	
	/**
	 * CreateCommThreadTask class
	 * 
	 * Class used to initialize the creation of the socket
	 * 
	 * @author Mike
	 *
	 */
	
	private class CreateCommThreadTask extends AsyncTask
	<Void, Integer, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				serverAddress =
				InetAddress.getByName(ipAddress);
				socket = new Socket(serverAddress, 500);
				commsThread = new CommsThread(socket);
				commsThread.start();
				sendToServer(NICKNAME);
			} catch (UnknownHostException e) {
				Log.d("Sockets", e.getLocalizedMessage());
			} catch (IOException e) {
				Log.d("Sockets", e.getLocalizedMessage());
			}
			return null;
		}
	}
	
	/**
	 * method used to display the ip address as of now.  Will be changed later to add a light to the system
	 * @param view
	 */
	
	public void addLight(View view) {
		Toast prName = Toast.makeText(this,ipAddress,Toast.LENGTH_LONG);
		prName.setGravity(0,0,0);
		prName.show();
	}
	
	/**
	 * WriteToServerTask class
	 * 
	 * Used to send data to the server through the commsThread class
	 * 
	 * @author Mike
	 *
	 */
	
	private class WriteToServerTask extends AsyncTask
	<byte[], Void, Void> {
		protected Void doInBackground(byte[]...data) {
			commsThread.write(data[0]);
			return null;
		}
	}
	
	/**
	 * CloseSocketTask
	 * 
	 * Starts the process of closing the socket when the program finishes
	 * 
	 * @author Mike
	 *
	 */
	private class CloseSocketTask extends AsyncTask
	<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			if(ipAddress != null){
				try {
					socket.close();
				} catch (IOException e) {
					Log.d("Sockets", e.getLocalizedMessage());
				}
			}
			return null;
		}
	}
	
	/**
	 * Method used to set the IP address of the server
	 * 
	 * @param view
	 */
	
	public void set_ip(View view) {
		EditText editText = (EditText) findViewById(R.id.edit_ip);
		ipAddress = editText.getText().toString();
		new CreateCommThreadTask().execute();
		Toast prName = Toast.makeText(this,R.string.confirm_ip,Toast.LENGTH_LONG);
		prName.setGravity(0,0,0);
		prName.show();
		FileOutputStream outputStream;
		try{
			outputStream = openFileOutput(getString(R.string.ip_file_name), Context.MODE_PRIVATE);
			outputStream.write(ipAddress.getBytes());
			outputStream.close();
		} catch(Exception e){
		
		}
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_light_control);
		Spinner spinner = (Spinner) findViewById(R.id.Lights_list);
		ladapter = new ArrayAdapter<String>(this, R.array.Lights_list, android.R.layout.simple_spinner_item);
		ladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(ladapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        lightNum = Integer.toString(position);
		    }
		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        lightNum = null;
		    }

		});
		
		Spinner blinds = (Spinner) findViewById(R.id.Blinds_list);
		ArrayAdapter<CharSequence> adapterb = ArrayAdapter.createFromResource(this, R.array.Blinds_list, android.R.layout.simple_spinner_item);
		adapterb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		blinds.setAdapter(adapterb);
		blinds.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        blindNum = Integer.toString(position);
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        blindNum = null;
		    }

		});
		
		
		File find = getBaseContext().getFileStreamPath(getString(R.string.ip_file_name));
		if(!find.exists()){
			SavedIP = new File(this.getFilesDir(), getString(R.string.ip_file_name));
		}
		else {
			SavedIP = find;
			try {
				FileInputStream fis = openFileInput(getString(R.string.ip_file_name));
				InputStreamReader inReader = new InputStreamReader(fis);
				BufferedReader bufReader = new BufferedReader(inReader);
				StringBuilder sb = new StringBuilder();
				String line;
				while((line = bufReader.readLine()) != null){
					sb.append(line);
				}
				ipAddress = sb.toString();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * sends message to turn the light on to the server when the button is clicked
	 * @param view
	 */
	
	public void onClickSendOn(View view) {
		if (ipAddress != null) {
			sendToServer("Light_on" + " " + lightNum);
		}
		else {
			Toast prName = Toast.makeText(this,R.string.no_ip_error,Toast.LENGTH_LONG);
			prName.setGravity(0,0,0);
			prName.show();
		}
	}
	/**
	 * method used to turn the light off when the button is selected
	 * @param view
	 */
	public void onClickSendOff(View view) {
		if (ipAddress != null) {
			sendToServer("Light_off" + " " + lightNum);
		}
		else {
			Toast prName = Toast.makeText(this,R.string.no_ip_error,Toast.LENGTH_LONG);
			prName.setGravity(0,0,0);
			prName.show();
		}
	}
	
	/**
	 * method used to open the blind selected
	 */
	public void openBlind(View view){
		if (ipAddress != null) {
			sendToServer("Blind_open" + " " + blindNum);
		}
		else {
			Toast prName = Toast.makeText(this,R.string.no_ip_error,Toast.LENGTH_LONG);
			prName.setGravity(0,0,0);
			prName.show();
		}
	}
	
	/**
	 * method used to close the blind selected
	 */
	public void closeBlind(View view){
		if (ipAddress != null) {
			sendToServer("Blind_close" + " " + blindNum);
		}
		else {
			Toast prName = Toast.makeText(this,R.string.no_ip_error,Toast.LENGTH_LONG);
			prName.setGravity(0,0,0);
			prName.show();
		}
	}
	
	/**
	 * method used to toggle the lights on and off
	 */
	public void LightsT(View view){
		if (ipAddress != null) {
			sendToServer("ToggleLights" + " " + 0);
		}
		else {
			Toast prName = Toast.makeText(this,R.string.no_ip_error,Toast.LENGTH_LONG);
			prName.setGravity(0,0,0);
			prName.show();
		}
	}
	
	/**
	 * method used to toggle the blinds on and off
	 */
	public void BlindsT(View view){
		if (ipAddress != null) {
			sendToServer("ToggleBlinds" + " " + 0);
		}
		else {
			Toast prName = Toast.makeText(this,R.string.no_ip_error,Toast.LENGTH_LONG);
			prName.setGravity(0,0,0);
			prName.show();
		}
	}
	
	/**
	 * method to bring user to edit screen
	 */
	public void AddRemove(View view){
		Intent intent = new Intent(this, LightManage.class);
    	startActivity(intent);
	}
	
	/**
	 * method used to send a message to the server to be displayed
	 * @param message
	 */
	private void sendToServer(String message) {
		byte[] theByteArray =
		message.getBytes();
		new WriteToServerTask().execute(theByteArray);
	}
	/**
	 * method that tells the program what to do when the app resumes.  Re-initializes the socket
	 */
	@Override
	public void onResume() {
		super.onResume();
		new CreateCommThreadTask().execute();
	}
	/**
	 * closes the socket on pause.
	 */
	@Override
	public void onPause() {
		super.onPause();
		new CloseSocketTask().execute();
	}
}