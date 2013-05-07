package SE_spring2013_g8.hal.Surveillance;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

//import com.example.hellogridview.MainActivity;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ServerActivity Class
 * 
 * This class runs the activity which receives and displays the surveillance.
 * 
 * @author Steve
 *
 */

public class ServerActivity extends Activity {
	/**
	 * TextView describing the server's current status
	 */	
    private TextView serverStatus;
    
    /**
     * String IP address of the "server" tablet (the tablet which views the video streams)
     */
    public static String SERVERIP = "10.0.2.15"; // default ip
    
    /**
     * int Port of the "server" tablet (the tablet which views the video streams) 
     */
    public static final int SERVERPORT = 8080; // designate a port
    
    /**
     * handler which handles asynchronous threads communicating with the main UI thread
     */
    private Handler handler = new Handler();
    
    /**
     * ServerSocket of the server
     */
    private ServerSocket serverSocket;
    
    /**
     * MediaPlayer used to play the streamed video
     */
    MediaPlayer mMediaPlayer;
    
    /**
     * ImageView for displaying the streamed "video" (which is actually individual images)
     */
    ImageView incomingImages;
    
    /**
     * Bitmap of the individual video frames which are sent to the server
     */
    Bitmap incomingImage;
    
	final ImageAdapter mImageAdapter = new ImageAdapter(this);
	
	int preferredStreamId = 0;

	/**
	 * Creates the main activity of the surveillance module
	 * 
	 * @param savedInstanceState the saved instance state
	 */	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.surveillance_server_activity);
        serverStatus = (TextView) findViewById(R.id.server_status);
        mMediaPlayer = new MediaPlayer();    
        SERVERIP = getLocalIpAddress();

        //create view to hold images being sent from client
        incomingImages = (ImageView) findViewById(R.id.incoming_images);        

        //create the gridview
        GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(mImageAdapter);
	    
	    //set logic to select a video stream to display as the main video stream
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	int streamId = mImageAdapter.getIdByPosition(position);
	            Toast.makeText(getBaseContext(), "ID of stream: " + streamId, Toast.LENGTH_SHORT).show();
	            preferredStreamId = streamId;
	        }
	    });
	    
    	//start server thread
        Thread fst = new Thread(new ServerThread());
        fst.start();
    }

    
    /**
     * ServerThread Class
     * 
     * This class runs the thread for connecting to the client over sockets
     * 
     * @author Steve
     *
     */
    public class ServerThread implements Runnable {

        public void run() {
            try {

            	
        		MulticastSocket server = new MulticastSocket(1234);
        		InetAddress group = InetAddress.getByName("234.5.6.7");
        		server.joinGroup(group);
        		boolean infinite = true;
        		
        		/* Server continually receives data and prints them */
        		while(infinite) {
        			
        			handler.post(new Runnable() {
                        @Override
                        public void run() {
                            serverStatus.setText("Connected.");
                        }
                    });
        			
        			byte buf[] = new byte[6000];
        			DatagramPacket data = new DatagramPacket(buf, buf.length);
        			server.receive(data);
        			byte[] receivedArray = data.getData();
        			VideoFrame mVideoFrame = new VideoFrame(receivedArray);
                	//toastUsingHandler("length of received Array: " + receivedArray.length, handler, getBaseContext());
        			
        			//check data of videoFrame
                    //toastUsingHandler(mVideoFrame.getOne() + "  " + mVideoFrame.getTwo() + "  " + mVideoFrame.getThree() + "  " + mVideoFrame.getFour() + "  " + "id:" + mVideoFrame.getSourceId() + " " + "Length of image data: " + mVideoFrame.getImageData().length, handler, getBaseContext());
        			
                	byte[] imageArray = mVideoFrame.getImageData();
                	
                	// check if ID is sent properly
                	final byte encodedImageId =  mVideoFrame.getSourceId();

                	incomingImage = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
                	
                	// update the image!
                	handler.post(new Runnable() {
                        @Override
                        public void run() {
                        	// main image
                        	if (encodedImageId == preferredStreamId) {
                        		incomingImages.setImageBitmap(incomingImage);
                        	}
                            // grid images
                    		mImageAdapter.addImage(encodedImageId, incomingImage); // OMG BYTE OMG OMGOMG
                    		mImageAdapter.notifyDataSetChanged();
                        }
                    });
        		}
        		server.close();	
            } catch (Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        serverStatus.setText("Error");
                    }
                });
                e.printStackTrace();
            }
        }
    }
    
    /*
    private String arrayDataToString (byte[] byteArray) {
    	String arrayData = "";
    	arrayData = arrayData + "Array Length = " + byteArray.length;
    	arrayData = arrayData + "  **  " + "First Element = " + byteArray[0];
    	arrayData = arrayData + "  **  " + "Second Element = " + byteArray[1];
    	arrayData = arrayData + "  **  " + "Last Element = " + byteArray[byteArray.length-1];
    	return arrayData;
    }
    */
    
    /**
     * get's the local IP address of the current android device
     * 
     * @return String of the IP address of the current android device
     */
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

    /**
     * Closes the socket when the android application stops
     */    
    @Override
    protected void onStop() {
        super.onStop();
        /*try {
             // make sure you close the socket upon exiting
             serverSocket.close();
         } catch (IOException e) {
             e.printStackTrace();
         }*/
    }
    

    /**
     * Converts from the NV21 image format which is a byte array to a bitmap
     * 
     * @param callbackImageWidth width of the image to be converted
     * @param callbackImageHeight height of the image to be converted
     * @param imageArray byte array containing the image
     * @return Bitmap the generated bitmap
     */
    private Bitmap getBitmapFromNV21 (int callbackImageWidth, int callbackImageHeight, byte[] imageArray) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		YuvImage yuvImage = new YuvImage(imageArray, ImageFormat.NV21, callbackImageWidth, callbackImageHeight, null);
		yuvImage.compressToJpeg(new Rect(0, 0, callbackImageWidth, callbackImageHeight), 50, out);
		byte[] imageBytes = out.toByteArray();
		Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
		return image;
    }
    
    private void toastUsingHandler(String text, Handler handler, Context context) {
    	final String textFinal = text; 
    	final Context contextFinal = context;
    	handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(contextFinal, textFinal, Toast.LENGTH_SHORT).show();
            }
        });
    }
}