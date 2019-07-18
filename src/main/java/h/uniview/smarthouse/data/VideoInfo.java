package h.uniview.smarthouse.data;

import java.io.Serializable;

public class VideoInfo extends CameraInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String LoginName;
	private String LoginPwd;
	private String ChannelID;
	private String HardDiskUsage;


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

	public String getHardDiskUsage() {
		return HardDiskUsage;
	}

	public void setHardDiskUsage(String hardDiskUsage) {
		HardDiskUsage = hardDiskUsage;
	}

	@Override
	public String toString() {
		return "BVideoInfo{" +
				"LoginName='" + LoginName + '\'' +
				", LoginPwd='" + LoginPwd + '\'' +
				", ChannelID='" + ChannelID + '\'' +
				", HardDiskUsage='" + HardDiskUsage + '\'' +
				", CameraInfo='" + super.toString() + '\'' +
				'}';
	}
}
