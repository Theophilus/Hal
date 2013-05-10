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
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
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

	
	/**
	 * gotoAnnouncement redirects the user to the announcement activity when the announcement
	 * button is clicked in the intercom home view
	 * 
	 * @param view the view to be displayed
	 */
	public void gotoAnnouncement(View view) {
		MainActivity.stopVoiceReceiver();
		startRecording();
		//timer.
		timer.start();
		startbutton.setClickable(false);
		stopbutton.setClickable(true);
		
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

	                final InetAddress destination = InetAddress.getByName("192.168.0.105"); //home IP
	                //final InetAddress destination = InetAddress.getByName("192.168.1.3"); // rutgers IP
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
	
	
}
