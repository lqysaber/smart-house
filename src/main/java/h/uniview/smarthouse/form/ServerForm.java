package h.uniview.smarthouse.form;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class ServerForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	private String name;
	
	@NotNull
	private String ip;
	
	@NotNull
	private int port;
	
	private String mess;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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

	private Integer cursor;

	public Integer getCursor() {
		return null == cursor ? 0 : cursor.intValue();
	}

	public void setCursor(Integer cursor) {
		this.cursor = cursor;
	}
	
}
