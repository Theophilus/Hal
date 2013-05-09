package SE_spring2013_g8.hal.audio;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class audio_home extends Activity implements OnClickListener {
	
	Button play, pause;
	MediaPlayer mpAudio;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_layout);
		play=(Button)findViewById(R.id.button2);
		pause=(Button)findViewById(R.id.button3);
		play.setOnClickListener(this);
		pause.setOnClickListener(this);
		
		mpAudio = MediaPlayer.create(this, R.raw.levels);
		mpAudio.setLooping(false);
		
	}
	public void onClick(){
		
	}
	public void sync(){
		
	}
	
	@Override
	public void onClick(View v) {
		if (v==play){
			mpAudio.start();
			
		}else if (v==pause){
			mpAudio.pause();
			
		}
		
	}
}
