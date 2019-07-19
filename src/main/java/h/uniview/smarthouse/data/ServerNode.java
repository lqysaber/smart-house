package h.uniview.smarthouse.data;

import java.io.Serializable;

public class ServerNode implements Serializable {
	
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

	public String getIP() {
		return iP;
	}

	public void setIP(String iP) {
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

	@Override
	public String toString() {
		return "ServerNode{" +
				"name='" + name + '\'' +
				", iP='" + iP + '\'' +
				", port=" + port +
				", mess='" + mess + '\'' +
				'}';
	}
}
