package h.uniview.smarthouse.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class ServerForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message = "服务名称不能为空")
	private String name;
	
	@NotEmpty(message = "ip地址不为能为空")
	private String ip;
	
	@Min(value = 1, message = "端口不能小于1")
	@Max(value = 65535, message = "端口不能大于65535")
	private String port;
	
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

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
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
