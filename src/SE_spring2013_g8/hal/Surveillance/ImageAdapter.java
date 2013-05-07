package SE_spring2013_g8.hal.Surveillance;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


// Images can be added or removed by their ID
// Adding an image who'se ID is already in use will simply update the bitmap of that image
// removing a non-existant ID will do nothing
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    List<BitmapWithID> bitmapList = new ArrayList<BitmapWithID>();

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
    	return bitmapList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(192, 128));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(bitmapList.get(position).getBitmap());
        return imageView;
    }
    
    // to add or update the image in the gridview
    // adding an image whose ID already exists will simply update the bitmap
    public void addImage(int imageID, Bitmap image) {
    	if (!imageExists(imageID)) { // if the image is not in the gridview yet
    		BitmapWithID mBitmapWithID = new BitmapWithID(imageID, image);
        	bitmapList.add(mBitmapWithID); // add the image
    	}
    	else { // if the image *is* in the gridview
    		for (int location=0; location<getCount(); location++) { // find it
        		if (bitmapList.get(location).getID() == imageID) {
        			bitmapList.get(location).setBitmap(image); // and update the bitmap
        		}
        	}
    	}   	
    }    

    public void removeImageByID(int imageID) {
    	for (int location=0; location<getCount(); location++) {
    		if (bitmapList.get(location).getID() == imageID) {
    			bitmapList.remove(location);
    			break;
    		}
    	}
    }

    public boolean imageExists(int imageID) {
    	boolean imageExists = false;
    	for (int location=0; location<getCount(); location++) {
    		if (bitmapList.get(location).getID() == imageID) {
    			imageExists = true;
    		}
    	}
    	return imageExists;
    }
    
    public int getIdByPosition (int position) {
    	int id = bitmapList.get(position).getID();
    	return id;
    }
}