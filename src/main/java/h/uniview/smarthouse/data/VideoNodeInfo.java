package h.uniview.smarthouse.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class VideoNodeInfo extends CameraInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String loginName;
	private String loginPwd;
	private String channelID;
	private int cloudControl;

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

	public int getCloudControl() {
		return cloudControl;
	}

	public void setCloudControl(int cloudControl) {
		this.cloudControl = cloudControl;
	}

	@Override
	public String toString() {
		return "VideoInfo{" +
				"loginName='" + loginName + '\'' +
				", loginPwd='" + loginPwd + '\'' +
				", channelID='" + channelID + '\'' +
				", cloudControl='" + cloudControl + '\'' +
				", " + super.toString() +
				'}';
	}
}
