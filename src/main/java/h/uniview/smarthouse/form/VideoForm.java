package h.uniview.smarthouse.form;

import java.io.Serializable;

public class VideoForm extends CameraForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String loginName;
	private String loginPwd;
	private String channelID;
	private Integer cloudControl;

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

	public Integer getCloudControl() {
		return cloudControl;
	}

	public void setCloudControl(Integer cloudControl) {
		this.cloudControl = cloudControl;
	}

	@Override
	public String toString() {
		return "VideoInfo{" +
				"loginName='" + loginName + '\'' +
				", loginPwd='" + loginPwd + '\'' +
				", channelID='" + channelID + '\'' +
				", cloudControl='" + cloudControl + '\'' +
				", camera='" + super.toString() + '\'' +
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
