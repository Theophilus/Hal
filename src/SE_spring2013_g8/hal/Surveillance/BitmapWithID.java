package SE_spring2013_g8.hal.Surveillance;

import android.graphics.Bitmap;

public class BitmapWithID {
	int ID;
	Bitmap mBitmap;
	
	public BitmapWithID(int ID, Bitmap mBitmap) {
		this.ID = ID;
		this.mBitmap = mBitmap;		
	}
	
	public void setBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}
	
	public Bitmap getBitmap() {
		return this.mBitmap;
	}
	
	public int getID() {
		return this.ID;
	}
}
