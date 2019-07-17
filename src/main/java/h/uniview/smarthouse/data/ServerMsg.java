package h.uniview.smarthouse.data;

import java.io.Serializable;

public class ServerMsg implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String iP;
	
	private int port;
	
	private String mess;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getiP() {
		return iP;
	}

	public void setiP(String iP) {
		this.iP = iP;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getMess() {
		return mess;
	}

	public void setMess(String mess) {
		this.mess = mess;
	}

}
