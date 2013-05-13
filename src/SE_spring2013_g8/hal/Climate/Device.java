package SE_spring2013_g8.hal.Climate;

public class Device {
	String name;
	boolean state;
	int num;
	
	private Device(){name = null;}
	
	Device(String name, boolean state, int num){
		this.name = name;
		this.state = state;
		this.num = num;
	}
	
	//bad coding iknow, whatevs
	static boolean bool;
	static int n;
	public static String parseInfo(String s){
		int i = s.indexOf('|'), j = s.lastIndexOf('|');
		String str = "Default";
		
		try{
			str = s.substring(0, i);
			bool = Boolean.parseBoolean(s.substring(i+1,j));
			n = Integer.parseInt(s.substring(j+1));
		} catch(NumberFormatException e){ bool = false; n = 1; } 
		  catch(IndexOutOfBoundsException e){ bool = false; n = 1; }
		
		return str;
	}
	
}
