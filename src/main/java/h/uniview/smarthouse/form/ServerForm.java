package h.uniview.smarthouse.form;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class ServerForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	private String Name;
	
	@NotNull
	private String IP;
	
	@NotNull
	private int Port;
	
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
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

	private String Mess;

}
