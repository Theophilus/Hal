package SE_spring2013_g8.hal.Lights;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
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
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_light_control);
		Spinner spinner = (Spinner) findViewById(R.id.Lights_list);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Lights_list, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
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