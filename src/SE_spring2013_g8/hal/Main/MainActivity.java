package SE_spring2013_g8.hal.Main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import lash.halapp.ViewDevActivity;
import SE_spring2013_g8.hal.R;
import SE_spring2013_g8.hal.Climate.ThermostatActivity;
import SE_spring2013_g8.hal.Intercom.HomeView;
import SE_spring2013_g8.hal.Lights.LightControl;
import SE_spring2013_g8.hal.Surveillance.SurveillanceMainActivity;
import SE_spring2013_g8.hal.audio.audio_home;
import SE_spring2013_g8.hal.emerlight.EmergencyLighting;
import SE_spring2013_g8.hal.Weather.Weather;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

/**
 * MainActivity class
 * 
 *  MainActivity is an activity class that creates and displays the home view of the 
 *  home automation system.
 * 
 * @author Theophilus Mensah
 *
 */
public class MainActivity extends Activity {
	
	public static DatagramSocket socket;
	private static AudioTrack speaker;

	//Audio Configuration. 
	private static int sampleRate = 44100;      //How much will be ideal?
	private static int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;    
	private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;       

	private static boolean status = true;

	
	/**
	 * onCreate prepares and displays the list of modules in the MainActivity view.
	 * It listens and redirects the user to the view module clicked.
	 *   
	 * @param savedInstanceState an instance of the MainActivity class
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter(this));
	    
	    startVoiceReceiver();
	    
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            //Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	            if (position == 7) {
	            	Intent intent = new Intent(MainActivity.this, LightControl.class);
	            	startActivity(intent);
	            }
	            if(position ==5){
	            	Intent intent = new Intent(MainActivity.this, HomeView.class);
	            	startActivity(intent);
	            }
	            if (position == 6) {
	            	Intent intent = new Intent(MainActivity.this, SurveillanceMainActivity.class);
	            	startActivity(intent);
	            }
	            if (position == 4) {
	            	Intent intent = new Intent(MainActivity.this, EmergencyLighting.class);
	            	startActivity(intent);
	            }
	            if (position == 0) {
	            	Intent intent = new Intent(MainActivity.this, ThermostatActivity.class);
	            	startActivity(intent);
	            }
	            if (position == 3) {
	            	Intent intent = new Intent(MainActivity.this, audio_home.class);
	            	startActivity(intent);
	            }
	            if (position == 2) {
	            	Intent intent = new Intent(MainActivity.this, ViewDevActivity.class);
	            	startActivity(intent);
	            }
	            if (position == 1) {
	            	Intent intent = new Intent(MainActivity.this, Weather.class);
	            	startActivity(intent);
	            }
	        }			
	    });
	    
	}

	public static void stopVoiceReceiver() {
	        status = false;
	        speaker.release();
	        Log.d("VR","Speaker released");

	}

	public static void startVoiceReceiver() {
	        status = true;
	        startReceiving();

	}



	public static void startReceiving() {

	    Thread receiveThread = new Thread (new Runnable() {

	        @Override
	        public void run() {

	            try {

	                DatagramSocket socket = new DatagramSocket(5500);
	                Log.d("VR", "Socket Created");

	                int buff= AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);
	                byte[] buffer = new byte[buff];


	                //minimum buffer size. need to be careful. might cause problems. try setting manually if any problems faced
	                int minBufSize =+ AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
	                //int minBufSize=2400;
	                speaker = new AudioTrack(AudioManager.STREAM_MUSIC,sampleRate,channelConfig,audioFormat,minBufSize,AudioTrack.MODE_STREAM);

	                speaker.play();

	                while(status == true) {
	                    try {

	                        DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
	                        socket.receive(packet);
	                        Log.d("VR", "Packet Received");

	                        //reading content from packet
	                        buffer=packet.getData();
	                        Log.d("VR", "Packet data read into buffer");

	                        //sending data to the Audiotrack obj i.e. speaker
	                        speaker.write(buffer, 0, minBufSize);
	                        Log.d("VR", "Writing buffer content to speaker");

	                    } catch(IOException e) {
	                        Log.e("VR","IOException");
	                    }
	                }

	            } catch (SocketException e) {
	                Log.e("VR", "SocketException");
	            }

	        }

	    });
	    receiveThread.start();
	}

}
