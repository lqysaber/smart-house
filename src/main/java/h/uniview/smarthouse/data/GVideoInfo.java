package h.uniview.smarthouse.data;

import java.io.Serializable;

public class GVideoInfo extends CameraInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String LoginName;
	private String LoginPwd;
	private String ChannelID;

	public String getLoginName() {
		return LoginName;
	}

	public void setLoginName(String loginName) {
		LoginName = loginName;
	}

	public String getLoginPwd() {
		return LoginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		LoginPwd = loginPwd;
	}

	public String getChannelID() {
		return ChannelID;
	}

	public void setChannelID(String channelID) {
		ChannelID = channelID;
	}

	@Override
	public String toString() {
		return "GVideoInfo{" +
				"LoginName='" + LoginName + '\'' +
				", LoginPwd='" + LoginPwd + '\'' +
				", ChannelID='" + ChannelID + '\'' +
				", CameraInfo='" + super.toString() + '\'' +
				'}';
	}
}
