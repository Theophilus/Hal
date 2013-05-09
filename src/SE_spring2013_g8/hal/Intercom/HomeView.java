package SE_spring2013_g8.hal.Intercom;

import java.io.IOException;
import java.net.DatagramPacket;
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

	private MediaPlayer   mPlayer = null;
	
	private int port=12343;
	
	private ServerSocket server;
	/**
	 * onCreate prepares and displays the home view
	 * 
	 * @param savedInstanceState an instance of the home view activity class
	 */
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.intercom_homeview);
	
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
	
	
	public class BroadcastThread implements Runnable {

        public void run() {
        	
        	try {
    			server = new ServerSocket(port);
    			System.out.println("Running echo server at port " + port);
    		} catch (IOException e) {
    			System.out.println("Could not start server at port " + port);
    			System.exit(1);
    		}
    		
    		// accept clients
    		while (true) {
    			try {
    				Socket client = server.accept();
    				startPlaying(client.toString());
    				//new Thread(es).start();
    			} catch (IOException e) {
    				
    			}
    		}
          /*  try {

            	
        		MulticastSocket server = new MulticastSocket(12340);
        		InetAddress group = InetAddress.getByName("234.5.6.7");
        		server.joinGroup(group);
        		boolean infinite = true;
        		
        		 Server continually receives data and prints them 
        		while(infinite) {
        			
        			byte buf[] = new byte[6000];
        			DatagramPacket data = new DatagramPacket(buf, buf.length);
        			server.receive(data);
        			//server.
        			byte[] receivedArray = data.getData();
        			for(int i=0;i<receivedArray.length;i++){
        				//mPlayer.
	        			//String mfile=receivedArray;
	        			//startPlaying(mfile);
        			}
        		}	
            }
            catch(Exception e) {
        }*/
    }
	
        private void startPlaying(String mFileName) {
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(mFileName);
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
               // Log.e(LOG_TAG, "prepare() failed");
            }
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
}
