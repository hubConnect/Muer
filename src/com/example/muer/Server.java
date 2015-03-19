package com.example.muer;

public class Server {
	Server(int PortNumber, String HostURL, String GameDescription,String GameName)
	{
		this.PortNumber = PortNumber;
		this.HostURL = HostURL;
		this.GameDescription = GameDescription;
		this.GameName = GameName;
	}
	
	int getPort() {
		
		return PortNumber;
	}
	
	String getURL() {
		
		return HostURL;
	}
	
	String getDescription() {
		
		return GameDescription;
	}
	
	String getName() {
		
		return GameName;
	}
	
	private int PortNumber;
	private String HostURL;
	private String GameDescription;
	private String GameName;
}
