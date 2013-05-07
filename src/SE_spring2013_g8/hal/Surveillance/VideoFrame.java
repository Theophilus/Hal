package SE_spring2013_g8.hal.Surveillance;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;

public class VideoFrame {

	byte firstMSBOfLength;
	byte secondMSBOfLength;
	byte thirdMSBOfLength;
	byte fourthMSBOfLength;
	byte sourceId;
	byte[] imageData;
	
	int firstMSBOfLengthPosition=0;
	int secondMSBOfLengthPosition=1;
	int thirdMSBOfLengthPosition=2;
	int fourthMSBOfLengthPosition=3;
	int sourceIdPosition=4;
	
	int width = 320; // width of the image contained in the imageData
	int height = 240;// height of the image contained in the imageData
	int headerSize=5; // this is the additional number of places in the arrayOfBytes reserved for data (such as width and height)
	
	// create a video frame object with the intention of creating an array of bytes from the created object
	public VideoFrame() {
	}
	
	// create a video frame object out of an array of bytes
	public VideoFrame(byte[] videoFrameWithAddtnlInfo) {
		firstMSBOfLength=videoFrameWithAddtnlInfo[firstMSBOfLengthPosition];
		secondMSBOfLength=videoFrameWithAddtnlInfo[secondMSBOfLengthPosition];
		thirdMSBOfLength=videoFrameWithAddtnlInfo[thirdMSBOfLengthPosition];
		fourthMSBOfLength=videoFrameWithAddtnlInfo[fourthMSBOfLengthPosition];
		sourceId = videoFrameWithAddtnlInfo[sourceIdPosition];
		imageData = Arrays.copyOfRange(videoFrameWithAddtnlInfo, headerSize, videoFrameWithAddtnlInfo.length);
	}
	
	// accepts NV21 format, stores data as compressed jpeg
	public void setImageData(byte[] id) {
		imageData = compressNV21toJPEG(id);
	}
	
	public void setOne(byte x) {
		firstMSBOfLength = x;
	}
	
	public void setTwo(byte x) {
		secondMSBOfLength = x;
	}
	
	public void setThree(byte x) {
		thirdMSBOfLength = x;
	}
	
	public void setFour(byte x) {
		fourthMSBOfLength = x;
	}
	
	public void setSourceId(byte id) {
		sourceId = id;
	}
	
	// returns jpeg format
	public byte[] getImageData() {
		return imageData;
	}

	public byte getOne() {
		return firstMSBOfLength;
	}
	
	public byte getTwo() {
		return secondMSBOfLength;	
	}
	
	public byte getThree() {
		return thirdMSBOfLength;
	}
	
	public byte getFour() {
		return fourthMSBOfLength;
	}
	
	public byte getSourceId() {
		return sourceId;
	}
	
	public byte[] getVideoFrameWithAddtnlInfo() {
		byte[] arrayOfBytes = new byte[imageData.length+headerSize];
		System.arraycopy(imageData, 0, arrayOfBytes, headerSize, imageData.length);
		arrayOfBytes[firstMSBOfLengthPosition] = firstMSBOfLength;
		arrayOfBytes[secondMSBOfLengthPosition] = secondMSBOfLength;
		arrayOfBytes[thirdMSBOfLengthPosition] = thirdMSBOfLength;
		arrayOfBytes[fourthMSBOfLengthPosition] = fourthMSBOfLength;
		arrayOfBytes[sourceIdPosition] = sourceId;
		return arrayOfBytes;
	}
	
	private byte[] compressNV21toJPEG(byte[] data) {
		ByteArrayOutputStream outstr = new ByteArrayOutputStream();
        Rect rect = new Rect(0, 0, width, height);
		YuvImage mYuvImage = new YuvImage(data, ImageFormat.NV21, width, height, null);
		mYuvImage.compressToJpeg(rect, 20, outstr);
		return outstr.toByteArray();
	}
}
