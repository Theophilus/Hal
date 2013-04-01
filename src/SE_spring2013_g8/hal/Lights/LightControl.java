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


public class LightControl extends Activity {
	static final String NICKNAME = "HAL Online";
	InetAddress serverAddress;
	Socket socket;
	String ipv4;
	String ipAddress;
	String lightNum;
	//---all the Views---
	static TextView txtMessagesReceived;
	EditText txtMessage;
	//---thread for communicating on the socket---
	CommsThread commsThread;
	//---used for updating the UI on the main activity---
	static Handler UIupdater = new Handler() {
	};
	
	private class CreateCommThreadTask extends AsyncTask
	<Void, Integer, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				//---create a socket---
				serverAddress =
				InetAddress.getByName(ipAddress);
				//--remember to change the IP address above to match your own--
				socket = new Socket(serverAddress, 500);
				commsThread = new CommsThread(socket);
				commsThread.start();
				//---sign in for the user; sends the nick name---
				sendToServer(NICKNAME);
			} catch (UnknownHostException e) {
				Log.d("Sockets", e.getLocalizedMessage());
			} catch (IOException e) {
				Log.d("Sockets", e.getLocalizedMessage());
			}
			return null;
		}
	}
	
	public void addLight(View view) {
		Toast prName = Toast.makeText(this,ipAddress,Toast.LENGTH_LONG);
		prName.setGravity(0,0,0);
		prName.show();
	}
	
	private class WriteToServerTask extends AsyncTask
	<byte[], Void, Void> {
		protected Void doInBackground(byte[]...data) {
			commsThread.write(data[0]);
			return null;
		}
	}
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
		//---get the views---
	}
	public void onClickSendOn(View view) {
		//---send the message 'on' to the server---
		if (ipAddress != null) {
			sendToServer("Light_on" + " " + lightNum);
		}
		else {
			Toast prName = Toast.makeText(this,R.string.no_ip_error,Toast.LENGTH_LONG);
			prName.setGravity(0,0,0);
			prName.show();
		}
	}
	public void onClickSendOff(View view) {
		//---send the message 'off' to the server---
		if (ipAddress != null) {
			sendToServer("Light_off" + " " + lightNum);
		}
		else {
			Toast prName = Toast.makeText(this,R.string.no_ip_error,Toast.LENGTH_LONG);
			prName.setGravity(0,0,0);
			prName.show();
		}
	}
	private void sendToServer(String message) {
		byte[] theByteArray =
		message.getBytes();
		new WriteToServerTask().execute(theByteArray);
	}
	@Override
	public void onResume() {
		super.onResume();
		new CreateCommThreadTask().execute();
	}
	@Override
	public void onPause() {
		super.onPause();
		new CloseSocketTask().execute();
	}
}