package h.uniview.smarthouse.data;

import java.io.Serializable;

public class NVRInfo extends CameraInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String LoginName;
	private String LoginPwd;
	private String ChannelSum;

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

	public String getChannelSum() {
		return ChannelSum;
	}

	public void setChannelSum(String channelSum) {
		ChannelSum = channelSum;
	}

	@Override
	public String toString() {
		return "NVRInfo{" +
				"LoginName='" + LoginName + '\'' +
				", LoginPwd='" + LoginPwd + '\'' +
				", ChannelSum='" + ChannelSum + '\'' +
				", CameraInfo='" + super.toString() + '\'' +
				'}';
	}
}