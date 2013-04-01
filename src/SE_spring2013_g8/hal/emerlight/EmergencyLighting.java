package SE_spring2013_g8.hal.emerlight;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class EmergencyLighting extends Activity {
	
    /** Called when the activity is first created. */
    float BackLightValue = 0.5f; //dummy default value


	//flag to detect flash is on or off
	private boolean isLighOn = false;
 
	private Camera camera;
 
	private Button button;
 
	@Override
	protected void onStop() {
		super.onStop();
 
		if (camera != null) {
			camera.release();
		}
	}
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergency_lighting);
		
		// Everything from here to "button =" is for screen brightness
		SeekBar BackLightControl = (SeekBar)findViewById(R.id.backlightcontrol);
        final TextView BackLightSetting = (TextView)findViewById(R.id.backlightsetting);
        
        /*//Button for updating screen brightness on system
        Button UpdateSystemSetting = (Button)findViewById(R.id.updatesystemsetting);
        */
        
		button = (Button) findViewById(R.id.buttonFlashlight);
 
		Context context = this;
		PackageManager pm = context.getPackageManager();
 
		// if device support camera?
		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			Log.e("err", "Device has no camera!");
			return;
		}
 
		camera = Camera.open();
		final Parameters p = camera.getParameters();
 
		button.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
				if (isLighOn) {
 
					Log.i("info", "torch is turn off!");
 
					p.setFlashMode(Parameters.FLASH_MODE_OFF);
					camera.setParameters(p);
					camera.stopPreview();
					isLighOn = false;
 
				} else {
 
					Log.i("info", "torch is turn on!");
					
					p.setFlashMode(Parameters.FLASH_MODE_TORCH);
 
					camera.setParameters(p);
					camera.startPreview();
					isLighOn = true;
 
				}
 
			}
		});
		
		/* Everything in OnCreate past this point is for brightness control */
		
		/* Looks like part 1/2 is not needed
		//get the actual brightness 25-255 and slidervalue 0-100
		float curBrightnessValue;
		try { // This try/catch is part 1/2 so brightness doesn't hit zero
			curBrightnessValue=(float) android.provider.Settings.System.getInt(
			getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS)/255;

			curBrightnessValue=(float)Math.round(curBrightnessValue * 100) / 100;
			BackLightSetting.setText(String.valueOf(curBrightnessValue));
			float progressval = (float) curBrightnessValue * 100;
			int pi =(int)progressval;
			BackLightControl.setProgress(pi);
		}
		catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*//Button functionality for updating screen brightness on system
		UpdateSystemSetting.setOnClickListener(new Button.OnClickListener(){
			 
			@Override
			public void onClick(View arg0) {
		    	  int SysBackLightValue = (int)(BackLightValue * 255);
		    	  android.provider.Settings.System.putInt(getContentResolver(),
		          android.provider.Settings.System.SCREEN_BRIGHTNESS,
		          SysBackLightValue);
		      }    
		});
		*/
		
		BackLightControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
		 
			@Override
		    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		    // TODO Auto-generated method stub
		    BackLightValue = (float)arg1/100;
		    
		    // This if is part 2/2 so brightness doesn't hit zero
		    if(BackLightValue<0.1)
		    {
		    	BackLightValue=(float)0.1;
		    }
		    
		    BackLightSetting.setText(String.valueOf(BackLightValue));
		       
		    WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
		    layoutParams.screenBrightness = BackLightValue;
		    getWindow().setAttributes(layoutParams);
			}
			
		    @Override
		    public void onStartTrackingTouch(SeekBar arg0) {
		    }
		    @Override
		    public void onStopTrackingTouch(SeekBar arg0) {
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.emergency_lighting, menu);
		return true;
	}

}
