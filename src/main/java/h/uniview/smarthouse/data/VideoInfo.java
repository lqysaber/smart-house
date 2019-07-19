package h.uniview.smarthouse.data;

import java.io.Serializable;

public class VideoInfo extends CameraInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String loginName;
	private String loginPwd;
	private String channelID;
	private String hardDiskUsage;

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

	@Override
	public String toString() {
		return "VideoInfo{" +
				"loginName='" + loginName + '\'' +
				", loginPwd='" + loginPwd + '\'' +
				", channelID='" + channelID + '\'' +
				", hardDiskUsage='" + hardDiskUsage + '\'' +
				'}';
	}
}
