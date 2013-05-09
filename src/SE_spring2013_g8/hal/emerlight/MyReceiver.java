package SE_spring2013_g8.hal.emerlight;
import SE_spring2013_g8.hal.Main.MainActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	String action = intent.getAction();

        if(action.equals(Intent.ACTION_POWER_CONNECTED)) {
    		Toast.makeText(context, "Power Connected", Toast.LENGTH_LONG).show();
        	Intent i = new Intent(context, MainActivity.class);  
        	i.addFlags(Intent.FLAG_FROM_BACKGROUND);
        	i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   // Added
        	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	context.startActivity(i);
        }
        else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
    		Toast.makeText(context, "Power Disconnected", Toast.LENGTH_LONG).show();
        	Intent i = new Intent(context, EmergencyLighting.class);  
        	i.addFlags(Intent.FLAG_FROM_BACKGROUND);
        	i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   // Added
        	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        	context.startActivity(i);
        }
        
    }
}