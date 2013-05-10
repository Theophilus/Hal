package SE_spring2013_g8.hal.Intercom;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import SE_spring2013_g8.hal.R;
import SE_spring2013_g8.hal.Main.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * HomeView class
 * 
 * HomeView is an activity class displays the home view of the intercom subsystem.
 * 
 * @author Theophilus Mensah
 *
 */
public class HomeView extends Activity {

	public static DatagramSocket socket;
	private static AudioTrack speaker;

	//Audio Configuration. 
	private static int sampleRate = 8000;      //How much will be ideal?
	private static int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;    
	private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;       

	private static boolean status = true;

	/**
	 * onCreate prepares and displays the home view
	 * 
	 * @param savedInstanceState an instance of the home view activity class
	 */
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.intercom_homeview);
	startRecordReciever();
	}
	
	/**
	 * gotoBroadcast redirects the user to the broadcast view when broadcast is clicked in the
	 * intercom home view.
	 * 
	 * @param view the view to be displayed
	 */
	public void gotoBroadcastView(View view) {
	   
		Intent intent = new Intent(this, BroadcastView.class);
	    startActivity(intent);
	}
	
	/**
	 * gotoP2PView redirects the user to the P2P view when P2P is clicked in the
	 * intercom home view
	 * 
	 * @param view the view to be displayed
	 */
	
	public void gotoP2PView(View view) {
		Intent intent = new Intent(this, P2PView.class);
	    startActivity(intent);
	}
	
	/**
	 * gotoConferenceView redirects the user to the conference view when conference 
	 * is clicked in the intercom home view.
	 * 
	 * @param view
	 */
	public void gotoConferenceView(View view) {
		
		Intent intent = new Intent(this, ConferenceView.class);
	    startActivity(intent);
	}
	
	/**
	 * exit_intercom redirects the user to the home view of the application
	 * 
	 * @param view the view to be displayed
	 */
	public void exit_intercom(View view) {
		
		Intent intent = new Intent(this, MainActivity.class);
	    startActivity(intent);
	}
	
	public static void startRecordReciever() {
        status = true;
        startReceiving();

    }

	public static void stopRecordReceiver() {
        status = false;
        speaker.release();
        Log.d("VR","Speaker released");

    }
	
	public static void startReceiving() {

	    Thread receiveThread = new Thread (new Runnable() {

	        @Override
	        public void run() {

	            try {

	                DatagramSocket socket = new DatagramSocket(5500);
	                Log.d("VR", "Socket Created");

	                int buff= AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
	                byte[] buffer = new byte[buff];


	                //minimum buffer size. need to be careful. might cause problems. try setting manually if any problems faced
	                int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);

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

	
       
        
	private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    //IPv4:
                    if (!inetAddress.isLoopbackAddress()    && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())   ) {
                    	String IPv4Address = inetAddress.getHostAddress().toString();
                    	
                    	String[] tokens = IPv4Address.split("\\.");
                    	String lastElement = tokens[tokens.length-1];
                    	Log.e("Server IP Address: ", lastElement);
                    	//return inetAddress.getHostAddress().toString();
                    	return IPv4Address;
                    }
                    
                    //IPv6:
                    //if (!inetAddress.isLoopbackAddress()    && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())   ) { return inetAddress.getHostAddress().toString(); }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }
        return null;
    }
	
}
