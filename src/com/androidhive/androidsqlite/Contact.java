package com.androidhive.androidsqlite;

public class Contact {
	
	//private variables
	int _id;
	String _name;
	String IP_Address;
	
	// Empty constructor
	public Contact(){
		
	}
	// constructor
	public Contact(int id, String name, String IP_Address){
		this._id = id;
		this._name = name;
		this.IP_Address = IP_Address;
	}
	
	// constructor
	public Contact(String name, String IP_Address){
		this._name = name;
		this.IP_Address = IP_Address;
	}
	// getting ID
	public int getID(){
		return this._id;
	}
	
	// setting id
	public void setID(int id){
		this._id = id;
	}
	
	// getting name
	public String getName(){
		return this._name;
	}
	
	// setting name
	public void setName(String name){
		this._name = name;
	}
	
	// getting phone number
	public String getIPAddress(){
		return this.IP_Address;
	}
	
	// setting phone number
	public void setIPAddress(String IP_Address){
		this.IP_Address = IP_Address;
	}
}
