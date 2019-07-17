package h.uniview.smarthouse.data;

import java.io.Serializable;

public class ServerMsg implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String Name;
	
	private String IP;
	
	private int Port;
	
	private String Mess;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String IP) {
		this.IP = IP;
	}

	public int getPort() {
		return Port;
	}

	public void setPort(int port) {
		Port = port;
	}

	public String getMess() {
		return Mess;
	}

	public void setMess(String mess) {
		Mess = mess;
	}
}
