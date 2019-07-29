package h.uniview.smarthouse.form;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class CameraForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message = "设备名称不能为空")
	private String name;

	@Min(value = 1, message = "请选择正确的设备类型")
	@Max(value = 4, message = "请选择正确的设备类型")
	@Digits(integer = 1, fraction = 0)
	private String type;

	private String typeName;

	@NotEmpty(message = "在线状态不能为空")
	private String status;

	private String workModel;

	@NotEmpty(message = "IP不能为空")
	private String ip;

	@Min(value = 1, message = "端口不能小于1")
	@Max(value = 65535, message = "端口不能大于65535")
	private String port;

	private String mess;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeName() { return typeName; }

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWorkModel() {
		return workModel;
	}

	public void setWorkModel(String workModel) {
		this.workModel = workModel;
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

	@Override
	public String toString() {
		return "CameraForm{" +
				"type='" + type + '\'' +
				", typeName='" + typeName + '\'' +
				", name='" + name + '\'' +
				", status='" + status + '\'' +
				", workModel='" + workModel + '\'' +
				", ip='" + ip + '\'' +
				", port='" + port + '\'' +
				", mess='" + mess + '\'' +
				'}';
	}
	

	private Integer cursor;

	public Integer getCursor() {
		return null == cursor ? 0 : cursor.intValue();
	}

	public void setCursor(Integer cursor) {
		this.cursor = cursor;
	}
	
}
