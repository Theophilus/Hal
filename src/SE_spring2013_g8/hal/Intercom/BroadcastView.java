package SE_spring2013_g8.hal.Intercom;

import java.util.ArrayList;
import java.util.Set;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore.Audio;
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
	
	private static final int audioSource=  MediaRecorder.AudioSource.DEFAULT;// default source, device specific
	private static final int sampleRateInHz= 44100 ; //works for all devices
	private static final int channel= AudioFormat.CHANNEL_IN_MONO; // mono channel works on all devices
	private static final int audioFormat= AudioFormat.ENCODING_PCM_16BIT; //ENCODING_PCM_16BIT works for all devices
	private static final int buffersize=AudioRecord.getMinBufferSize(sampleRateInHz, channel, audioFormat);
	private static final byte[] buffer= new byte[buffersize]; 
	
	AudioRecord recorder;
	
	AudioTrack player;
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
	
	
	
	/**
	 * gotoAnnouncement redirects the user to the announcement activity when the announcement
	 * button is clicked in the intercom home view
	 * 
	 * @param view the view to be displayed
	 */
	public void gotoAnnouncement(View view) {
		   
		/*Intent intent = new Intent(this, AnnouncementView.class);
	    startActivity(intent);*/
		
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
			CharSequence error = "Broadcast requires bluetooth";
			int duration = Toast.LENGTH_SHORT;

			Toast bluetoothOff= Toast.makeText(getApplicationContext(),error, duration);
			bluetoothOff.show();
		}
		else{
			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
			recorder= new AudioRecord(audioSource, sampleRateInHz, channel, audioFormat,buffersize);
			timer.setBase(SystemClock.elapsedRealtime()); 
			timer.start();
			recorder.startRecording();
			// If there are paired devices
			if (pairedDevices.size() > 0) {
			    // Loop through paired devices
				
			    for (BluetoothDevice device : pairedDevices) {
			    		System.out.println(device.getName()+" , "+ device.getAddress());
			    }
			}
		}
		startbutton.setClickable(false);
		stopbutton.setClickable(true);
		
	}
	
	public void stopBroadcast(View view){
		timer.stop();
		recorder.stop();
		recorder.release();
		/*player=new AudioTrack(AudioManager.STREAM_MUSIC,sampleRateInHz,AudioFormat.CHANNEL_OUT_MONO,
	            AudioFormat.ENCODING_PCM_16BIT,buffersize,AudioTrack.MODE_STREAM);
		player.play();
		player.release();*/
		System.out.println(timer.getBase());
		stopbutton.setClickable(false);
		startbutton.setClickable(true);
	}
}
