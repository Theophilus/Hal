package SE_spring2013_g8.hal.Surveillance;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;


public class TimeoutTracker {
	int timeout = 2000;
	// while images remain on this list, they cannot be timed out
    List<Integer> bitmapList = new ArrayList<Integer>();
    
	public TimeoutTracker() {
	}
	
	public void resetTimer(ImageAdapter mImageAdapter, int id) {
		Thread tThread = new Thread(new timeoutThread(mImageAdapter, id));
        tThread.start();
	}
	
    public class timeoutThread implements Runnable {
    	ImageAdapter mImageAdapter;
    	int id;    	
    	public timeoutThread(ImageAdapter mImageAdapter, int id) {
    		this.mImageAdapter= mImageAdapter; 
    		this.id = id;
    	}
    	public void run() {
    		// add this image to the list 
    		bitmapList.add((Integer) id);
    		try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		// remove this image from the list
    		bitmapList.remove((Integer) id);
    		
    		// if the image has not been refreshed by another thread since this thread added it to the list, it must be timed out
    		if (!imageExists(id)) {
    			mImageAdapter.imageTimedOut(id);
    			Log.e("TimeoutTracker: ", "image should have timed out");
    		}
        }
    }

    // check if the image with the given ID exists in the list
    public boolean imageExists(int imageID) {
    	boolean imageExists = false;
    	for (int location=0; location<bitmapList.size(); location++) {
    		if (bitmapList.get(location) != null && bitmapList.get(location) == imageID) {
    			imageExists = true;
    		}
    	}
    	return imageExists;
    }
}
