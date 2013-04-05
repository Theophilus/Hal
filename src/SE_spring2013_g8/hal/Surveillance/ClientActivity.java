package SE_spring2013_g8.hal.Surveillance;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * ClientActivity Class
 * 
 * This class runs the activity which creates and streams the surveillance video.
 * 
 * @author Steve
 *
 */

public class ClientActivity extends Activity {

	/**
	 * EditText containing the IP address of the server (the receiving end of this video)
	 */
    private EditText serverIpEditText;
    
    /**
     * Button to connect this tablet with the tablet for receiving the streamed video
     */
    private Button connectPhones;
    
    /**
     * String containing the IP address of the server.
     */
    private String serverIpAddress = "";
    
    /**
     * boolean used to determine if the client is already connected to the server (to avoid trying to connect more than once)
     */
    private boolean connected = false;
    
    //private Handler handler = new Handler();
    
    /**
     * Socket used for creating a socket connection to the server
     */
    Socket socket;
    
    /**
     * Camera for accessing the camera hardware on an android device 
     */
	private Camera mCamera;
	
	/**
	 * CameraPreview for accessing and displaying the preview video produced by the camera 
	 */
	private CameraPreview mPreview;
	
	/**
	 * MediaRecorder for 
	 */
	private MediaRecorder mMediaRecorder;
	
	/**
	 * int of the index of the camera to use
	 */
	private int mCameraIndex = 0;
	
	/**
	 * boolean representing the current recording state of the camera
	 */
	private boolean isRecording = false;
	
	/**
	 * Button used for toggling saving video on/off
	 */
	private Button captureButton;
	
	/**
	 * byte[] used to store a video frame from the camera preview
	 */
	byte[] callbackImage;
	
	/**
	 * int width of the video frame from the camera preview
	 */
	int callbackImageWidth = 0;
	
	/**
	 * int height of the video frame from the camera preview
	 */
	int callbackImageHeight = 0;
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	String TAG = "MainActivity";
	
	/**
	 * PreviewCallback of the video preview which returns a video frame
	 */
	
    PreviewCallback previewCallback = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
        	callbackImage = data;
        }
    };    
	
    
    /**
     * Creates the activity for the client
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.surveillance_client_activity);
        
        serverIpEditText = (EditText) findViewById(R.id.server_ip);
        connectPhones = (Button) findViewById(R.id.connect_phones);
        connectPhones.setOnClickListener(connectListener);
        
        // Create our Preview view and set it as the content of our activity.
        mCamera = openFrontFacingCameraGingerbread();	    
        mPreview = new CameraPreview(this, mCamera, previewCallback);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
	    
		/*
        // Add a listener to the Capture button
		final Button captureButton = (Button) findViewById(R.id.button_capture);
		captureButton.setOnClickListener(
		    new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		            if (isRecording) {
		            	// stop recording and release camera
		                mMediaRecorder.stop();  // stop the recording
		                releaseMediaRecorder(); // release the MediaRecorder object
		                mCamera.lock();         // take camera access back from MediaRecorder
		                
		                // inform the user that recording has stopped
		                captureButton.setText("Start video capture storage");
		                isRecording = false;
		            } else {
		                // initialize video camera
		                if (prepareVideoRecorder()) {
		                    // Camera is available and unlocked, MediaRecorder is prepared,
		                    // now you can start recording
		                    mMediaRecorder.start();

		                    // inform the user that recording has started
		                    captureButton.setText("Stop video capture storage");
		                    isRecording = true;
		                } else {
		                    // prepare didn't work, release the camera
		                    releaseMediaRecorder();
		                    // inform user
		                }
		            }
		        }
		    }
		); */
        
        // Add a listener to the Capture button
		final Button captureButton = (Button) findViewById(R.id.button_capture);
		captureButton.setOnClickListener(
		    new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		            if (isRecording) {
		            	// stop recording and release camera
		                mMediaRecorder.stop();  // stop the recording
		                releaseMediaRecorder(); // release the MediaRecorder object
		                mCamera.lock();         // take camera access back from MediaRecorder
		                
		                // inform the user that recording has stopped
		                captureButton.setText("Start video capture storage");
		                isRecording = false;
		            } else {
		                // initialize video camera
		                if (prepareVideoRecorder()) {
		                    // Camera is available and unlocked, MediaRecorder is prepared,
		                    // now you can start recording
		                    mMediaRecorder.start();

		                    // inform the user that recording has started
		                    captureButton.setText("Stop video capture storage");
		                    isRecording = true;
		                } else {
		                    // prepare didn't work, release the camera
		                    releaseMediaRecorder();
		                    // inform user
		                }
		            }
		        }
		    }
		);
    }
    
    /**
     * OnClickListener used to listen for the connect button
     */
    private OnClickListener connectListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!connected) {
            	serverIpAddress = serverIpEditText.getText().toString();
                if (!serverIpAddress.equals("")) {
                    Thread cThread = new Thread(new ClientThread());
                    cThread.start();
                }
            }
        }
    };

    /**
     * 
     * ClientThread which connects to the server asymmetrically
     * 
     * @author Steve
     *
     */
    public class ClientThread implements Runnable {

        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                Log.d("ClientActivity", "C: Connecting...");
                socket = new Socket(serverAddr, ServerActivity.SERVERPORT);
                connected = true;
                
                while (connected) {
                    try {
                    	Thread.sleep(200);
                    	Log.d("ClientActivity", "C: Sending command.");
                    	DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                        final byte[] imageToSend = callbackImage;

                        // Toast to see if the byte array containing the image is created properly
                        /*handler.post(new Runnable() {
                            @Override
                            public void run() {
                            	Toast.makeText(getBaseContext(), arrayDataToString(imageToSend), Toast.LENGTH_LONG).show();
                            }
                        });*/

                        int callbackImageWidth =  mCamera.getParameters().getPreviewSize().width;
                    	int callbackImageHeight =  mCamera.getParameters().getPreviewSize().height;
                        
                    	out.writeInt(callbackImageWidth);
                    	out.writeInt(callbackImageHeight);
                        out.writeInt(imageToSend.length);
                        out.write(imageToSend);
                        
                        Log.i("ClientActivity", "C: Sent.");
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
                }
                
                socket.close();
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
                connected = false;
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
    }*/
    
    /**
     * Sets the text of the capture button to indicate the current status of capturing
     * 
     * @param s the text to set for the capture button
     */
	public void setCaptureButtonText(String s) {
	    captureButton.setText(s);
	}
	
	/**
	 * 
	 * Prepares a video recorder 
	 * 
	 * @return boolean representing creation without exceptions
	 */
	private boolean prepareVideoRecorder(){

	    //mCamera = openFrontFacingCameraGingerbread();
	    mMediaRecorder = new MediaRecorder();

	    // Step 1: Unlock and set camera to MediaRecorder
	    mCamera.unlock();
	    mMediaRecorder.setCamera(mCamera);

	    // Step 2: Set sources
	    mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
	    mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

	    // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
	    //mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
	    mMediaRecorder.setProfile(CamcorderProfile.get(mCameraIndex, CamcorderProfile.QUALITY_LOW));

	    // Step 4: Set output file stored as file
	    //mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
	    
	    // Step 4: Set output file for broadcast
	    mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
	    Toast.makeText(getBaseContext(), "File Output location:" + getOutputMediaFile(MEDIA_TYPE_VIDEO).toString(), Toast.LENGTH_LONG).show();
	    
	    //perhaps change this to a way that stores a different video format?
	    //mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
					        
	    // Step 5: Set the preview output
	    mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

	    // Step 6: Prepare configured MediaRecorder
	    try {
	        mMediaRecorder.prepare();
	    } catch (IllegalStateException e) {
	        Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
	        releaseMediaRecorder();
	        return false;
	    } catch (IOException e) {
	        Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
	        releaseMediaRecorder();
	        return false;
	    }
	    return true;
	}
	
	
	/**
	 * inflate the options menu
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * 
	 * Find and open the frontfacing camera 
	 * 
	 * @return Camera which is the frontfacing camera found and opened
	 */
	private Camera openFrontFacingCameraGingerbread() {
		String TAG = "openFrontFacingCameraGingerbread";
		int cameraCount = 0;
		Camera cam = null;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();
		for ( int camIdx = 0; camIdx < cameraCount; camIdx++ ) {
			Camera.getCameraInfo( camIdx, cameraInfo );
			if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT  ) {
				mCameraIndex = camIdx;
				try {
					cam = Camera.open( camIdx );
				} catch (RuntimeException e) {
					Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
				}
			}
		}
		
		return cam;
	}
	
	/**
	 * Create a File for saving an image or video
	 * 
	 * @param type for creating the name of the media file
	 * @return null
	 */
	private static File getOutputMediaFile(int type) {
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }

	    
	    if (mediaFile.exists()) {
	        // Set to Readable and MODE_WORLD_READABLE
	        mediaFile.setReadable(true, false);
	    }
	    
	    return mediaFile;
	}
	
	/**
	 * release the mediarecorder and camera onPause
	 */
    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    /**
     * release the mediarecorder
     */
    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    /**
     * release the camera
     */
    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
}
