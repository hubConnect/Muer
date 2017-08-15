package com.skript.muer;
import java.io.Serializable;



public class Server implements Serializable {
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

	@Override
	public boolean equals(Object o) {
		Server testServer = (Server) o;

		if( testServer.getURL().equals(getURL()) && testServer.getPort() == getPort() && testServer.getName().equals(getName()) )
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		return HostURL;
	}

	private int PortNumber;
	private String HostURL;
	private String GameDescription;
	private String GameName;
}
