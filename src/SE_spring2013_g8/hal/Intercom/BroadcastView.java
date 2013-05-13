package SE_spring2013_g8.hal.Intercom;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Set;

import SE_spring2013_g8.hal.R;
import SE_spring2013_g8.hal.Main.MainActivity;
import SE_spring2013_g8.hal.Surveillance.VideoFrame;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * BroadcastView class
 * 
 * BroadcastView is an activity class that displays the view that is shown when broadcast is clicked from the 
 * intercom home view 
 * 
 * @author Theophilus Mensah
 *
 */
public class BroadcastView extends Activity {
	
	
	Chronometer timer;
	View startbutton;
	View stopbutton;
	
    public byte[] buffer;
    public static DatagramSocket socket;
    private int port=5500;         //which port??
    AudioRecord recorder;
    String destinationIP="";
    TextView desIP;
    EditText IPinput;
    //Audio Configuration. 
    private int sampleRate = 8000;      //How much will be ideal?
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;    
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;       

    private boolean recordStatus = true;


	/**
	 * onCreate prepares and displays the Broadcast view 
	 * @param savedInstanceState an instance of the BroadcastView activity class
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intercom_broadcast);
		
		desIP= (TextView) findViewById(R.id.broadcast_IPAddr);
		if(destinationIP.equalsIgnoreCase("")){
			desIP.setText(" Not set");
		}
		else{
			desIP.setText(destinationIP);
		}
		
		IPinput= (EditText) findViewById(R.id.broadcast_IP_editText);
		timer=(Chronometer) this.findViewById(R.id.broadcastTimer);
		startbutton=this.findViewById(R.id.intercom_broadcast_startbutton);
		stopbutton=this.findViewById(R.id.intercom_broadcast_stopbutton);
		stopbutton.setClickable(false);
		
	}
	
	   
	public void stopRecording() {
		recordStatus = false;
	    recorder.release();
	    Log.d("VS","Recorder released");
	}


	public void startRecording() {
		
		recordStatus = true;
			startStreaming();  
		
	}

	public void setDesIP(View v){
		String ip=IPinput.getText().toString();
		System.out.println(ip);
		if(ip == null){
			if(desIP.getText().equals(" Not Set")){
				Toast.makeText(this.getApplicationContext(), "Destination IP not set", Toast.LENGTH_SHORT).show();
			}
		}
		else{
				destinationIP=ip;
				desIP.setText(destinationIP);
			
		}
	}
	
	/**
	 * gotoAnnouncement redirects the user to the announcement activity when the announcement
	 * button is clicked in the intercom home view
	 * 
	 * @param view the view to be displayed
	 */
	public void gotoAnnouncement(View view) {
		System.out.println(destinationIP);
		if(destinationIP.equalsIgnoreCase("")|| destinationIP.equalsIgnoreCase(" not set")){
			Toast.makeText(this.getApplicationContext(), "Destination IP not set", Toast.LENGTH_SHORT).show();
		}else{
			//MainActivity.stopVoiceReceiver();
			startRecording();
			timer.setBase(SystemClock.elapsedRealtime());
			timer.start();
			startbutton.setClickable(false);
			stopbutton.setClickable(true);
		}
	}
	
	public void errorToast(){
		Toast.makeText(this.getApplicationContext(), "Incorrect IP address", Toast.LENGTH_SHORT).show();
	}
	
	public void startStreaming() {

	    Thread streamThread = new Thread(new Runnable() {

	        @Override
	        public void run() {
	            try {

	                int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
	                DatagramSocket socket = new DatagramSocket();
	                Log.d("VS", "Socket Created");

	                byte[] buffer = new byte[minBufSize];

	                Log.d("VS","Buffer created of size " + minBufSize);
	                DatagramPacket packet;

	               
	                final InetAddress destination = InetAddress.getByName(destinationIP);
	                Log.d("VS", "Address retrieved");


	                recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRate,channelConfig,audioFormat,minBufSize);
	                Log.d("VS", "Recorder initialized");

	                recorder.startRecording();


	                while(recordStatus == true) {

	                    //reading data from MIC into buffer
	                    minBufSize = recorder.read(buffer, 0, buffer.length);

	                    //putting buffer in the packet
	                    packet = new DatagramPacket (buffer,buffer.length,destination,port);
	                    socket.send(packet);
	                    Log.e("VS", "Recording");
	                }

	            } catch(UnknownHostException e) {
	                Log.e("VS", "UnknownHostException");
	                //errorToast();
	               
	            } catch (IOException e) {
	                Log.e("VS", "IOException");
	            } 
	        }

	    });
	    streamThread.start();
	 }
	
	public void stopBroadcast(View view){
		//MainActivity.startVoiceReceiver();
		stopRecording();
		timer.stop();
		stopbutton.setClickable(false);
		startbutton.setClickable(true);
	}
	
	@Override
	/**
	 * Method to go back to photo list if back button is pressed
	 */
	public boolean onKeyDown(int keycode, KeyEvent event){
		if (keycode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent(this, HomeView.class);
			startActivity(intent);
	        return true;
	    }
		
		return false;
		
	}
	
}
