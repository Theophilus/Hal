package SE_spring2013_g8.hal.Lights;

/**
 * LightsList Class
 * 
 * Object to hold various information about lights
 * @author Mike
 *
 */

public class LightsList {
	
	/**
	 * Used to hold the ID of the light 1-8 in this case
	 */
	private final Integer ID;
	
	/**
	 * Used to hold the name of the light
	 */
	private final String Name;
	
	/**
	 * constructor of the class object
	 * @param Lid
	 * @param Lame
	 */
	public LightsList(int Lid, String Lame ){
		ID = Lid;
		Name = Lame;
	}
	
	public int getID(){
		return ID;
	}
	
	public String getName(){
		return Name;
	}
	
	
}
