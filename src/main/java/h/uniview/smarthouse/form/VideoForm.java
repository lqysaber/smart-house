package h.uniview.smarthouse.form;

import java.io.Serializable;

public class VideoForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String loginName;
	private String loginPwd;
	private String channelID;
	private String hardDiskUsage;
	
	private String type = "";
	private String typeName = "";
	private String name;
	private String status;
	private String workModel;
	private String ip;
	private String port;
	private String mess;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

	public String getHardDiskUsage() {
		return hardDiskUsage;
	}

	public void setHardDiskUsage(String hardDiskUsage) {
		this.hardDiskUsage = hardDiskUsage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

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
		return "VideoInfo{" +
				"loginName='" + loginName + '\'' +
				", loginPwd='" + loginPwd + '\'' +
				", channelID='" + channelID + '\'' +
				", hardDiskUsage='" + hardDiskUsage + '\'' +
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
