package SE_spring2013_g8.hal.Main;

import SE_spring2013_g8.hal.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * ImageAdapter class
 * 
 * ImageAdapter is an adapter class that displays the list is the gridview of the
 * main activity
 * 
 * @author Theophilus Mesnah
 *
 */
public class ImageAdapter extends BaseAdapter {
	
	/**
	 * The context of the ImageAdapter class
	 */
	private Context mContext;

	/**
	 * The image adapter 
	 * @param c the context of the ImageAdpater
	 */
    public ImageAdapter(Context c) {
        mContext = c;
    }

    /**
     * gets the count of images in the view
     * 
     * @return count the count of images in the view
     */
    public int getCount() {
        return mThumbIds.length;
    }

    /**
     * gets the position of the image object
     * 
     * @return null
     */
    public Object getItem(int position) {
        return null;
    }

    /**
     * gets the Id of the image
     * 
     * @return zero not used
     */
    public long getItemId(int position) {
        return 0;
    }

    /**
     * getView gets and displays the position of the image in the list
     * 
     * @param position the position of the image in the view
     * @param convertView the view to be displayed
     * @param parent the container for the image view group
     * @return imageView an imageView filled with images to be displayed
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    /**
     * Array of images to displayed in the view
     */
    private Integer[] mThumbIds = {
            R.drawable.climate_control_icon, R.drawable.sun,R.drawable.gps_icon,
            R.drawable.intercom_spkr,R.drawable.emerlight_icon,R.drawable.intercom_icon,
            R.drawable.surveillanceicon,R.drawable.lightcontrolicon
    };

	

}
