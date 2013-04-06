package SE_spring2013_g8.hal.Lights;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.util.Log;
/**
 * CommsThread class
 * 
 * Class used to create a socket between the application and a given server IP
 * 
 * @author Mike
 *
 */
public class CommsThread extends Thread {
	/**
	 * used to hold the socket information
	 */
	private final Socket socket;
	/**
	 * used to hold the input stream data
	 */
	private final InputStream inputStream;
	/**
	 * used to hold the output stream data
	 */
	private final OutputStream outputStream;
	/**
	 * constructor that initializes a socket when the class is created
	 * @param sock
	 */
	public CommsThread(Socket sock) {
		socket = sock;
		InputStream tmpIn = null;
		OutputStream tmpOut = null;
		try {

			tmpIn = socket.getInputStream();
			tmpOut = socket.getOutputStream();
		} catch (IOException e) {
			Log.d("SocketChat", e.getLocalizedMessage());
		}
	inputStream = tmpIn;
	outputStream = tmpOut;
	}
	
	/**
	 * method used to read information from the server
	 */
	public void run() {
		byte[] buffer = new byte[1024];
		int bytes;
		while (true) {
			try {
				bytes = inputStream.read(buffer);
				LightControl.UIupdater.obtainMessage(
				0,bytes, -1, buffer).sendToTarget();
			} catch (IOException e) {
				break;
			}
		}
	}
	
	/**
	 * method used to write to the server
	 * @param bytes
	 */
	public void write(byte[] bytes) {
		try {
			outputStream.write(bytes);
		} catch (IOException e) { }
	}
	/**
	 * method that shuts down the connection
	 */
	public void cancel() {
		try {
			socket.close();
		} catch (IOException e) { }
	}
}