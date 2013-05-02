package SE_spring2013_g8.hal.Climate;

import SE_spring2013_g8.hal.R;
import android.app.Activity;
//import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import java.net.URL;
import java.io.InputStream;
import android.os.Bundle;

/**
 * Climate Control Class
 * 
 * This class contains functionality for controlling climate
 * 
 * @author Mohak Tamhane
 *@version 1.0
 */

public class ClimateControl extends Activity implements OnClickListener{
	
	Button btn1, btn2, plus, minus;
	TextView mode, temp;
	int counter = 70;
	String strcounter;
	
/**
 * Connects the CLimate control module to the main.	
 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.climate_control);
		btn1=(Button)findViewById(R.id.button1);
		btn2=(Button)findViewById(R.id.button2);
		plus=(Button)findViewById(R.id.button4);
		minus=(Button)findViewById(R.id.button3);
		mode=(TextView)findViewById(R.id.textView2);
		temp=(TextView)findViewById(R.id.textView4);
		plus.setOnClickListener(this);
		minus.setOnClickListener(this);
		final String my_server_ip_address_and_port_number = "192.168.1.100:3344";
		final ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
				
		toggleButton.setOnClickListener(new View.OnClickListener(){
				
			public void onClick(View v) {
				if (toggleButton.isChecked()) { 
					try {
						final InputStream is = new URL("http://"+ my_server_ip_address_and_port_number +"/command/on").openStream();
						}
					catch (Exception e) { 
						}
				} else { 
					try {
						final InputStream is = new URL("http://"+ my_server_ip_address_and_port_number +"/command/off").openStream();
				}
				catch (Exception e) { 
					}
				} 
			}
		});
				
		btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mode.setText("Cool");
				
			}
		});
		
		btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mode.setText("Heat");
			}
		});
		
		
	}
	
	public void onClick(View v){
		if(v==plus){
			counter++;
			strcounter = Integer.toString(counter);
			temp.setText(strcounter);
		}
		if(v==minus){
			counter--;
			strcounter = Integer.toString(counter);
			temp.setText(strcounter);
			
		}
		
		
	}

}
