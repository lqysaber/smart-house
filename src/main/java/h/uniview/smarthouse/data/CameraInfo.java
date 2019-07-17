package h.uniview.smarthouse.data;

import java.io.Serializable;

public class CameraInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String Type;
	private String TypeName;
	private String Name;
	private String Status;
	private String WorkModel;
	private String IP;
	private String Port;
	private String Mess;

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getTypeName() {
		return TypeName;
	}

	public void setTypeName(String typeName) {
		TypeName = typeName;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getWorkModel() {
		return WorkModel;
	}

	public void setWorkModel(String workModel) {
		WorkModel = workModel;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String IP) {
		this.IP = IP;
	}

	public String getPort() {
		return Port;
	}

	public void setPort(String port) {
		Port = port;
	}

	public String getMess() {
		return Mess;
	}

	public void setMess(String mess) {
		Mess = mess;
	}

	@Override
	public String toString() {
		return "CameraInfo{" +
				"Type='" + Type + '\'' +
				", TypeName='" + TypeName + '\'' +
				", Name='" + Name + '\'' +
				", Status='" + Status + '\'' +
				", WorkModel='" + WorkModel + '\'' +
				", IP='" + IP + '\'' +
				", Port='" + Port + '\'' +
				", Mess='" + Mess + '\'' +
				'}';
	}
}
