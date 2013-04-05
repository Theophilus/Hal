package SE_spring2013_g8.hal.Surveillance;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

/**
 * CameraPreview class
 * 
 * This class creates the on screen video preview of the video being captured.  Frames of this video preview are are what actually get transmitted by the ClientActivity class.
 * 
 * @author Steve
 *
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	/**
	 * SurfaceHolder required for creating the camera
	 */
    private SurfaceHolder mHolder;
    
    /**
     * Camera the camera being used to capture video
     */
    private Camera mCamera;
    
    /**
     * PreviewCallback for accessing the preview
     */
    private PreviewCallback mPreviewCallback;
    
    String TAG = "CameraPreview";

    /**
     * 
     * Set up camera preview
     * 
     * @param context the context
     * @param camera the camera
     * @param previewCallback the previewCallback defined earlier
     */
    public CameraPreview(Context context, Camera camera, PreviewCallback previewCallback) {
        super(context);
        mCamera = camera;
        
        mPreviewCallback = previewCallback;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
    }

    
    /**
     * The Surface has been created, now tell the camera where to draw the preview.
     */
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
        
        Toast.makeText(getContext(), "surface created", Toast.LENGTH_SHORT).show();
    }

    /**
     * The surface has been destroyed
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    /**
     * for handling the surface rotating and changing
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            										mCamera.setPreviewCallback(mPreviewCallback);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}