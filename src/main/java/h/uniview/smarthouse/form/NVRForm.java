package h.uniview.smarthouse.form;

import java.io.Serializable;

public class NVRForm extends CameraForm implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	private String loginName;
	private String loginPwd;
	private String channelSum;
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

	public String getChannelSum() {
		return channelSum;
	}

	public void setChannelSum(String channelSum) {
		this.channelSum = channelSum;
	}

	public String getHardDiskUsage() {
		return hardDiskUsage;
	}

	public void setHardDiskUsage(String hardDiskUsage) {
		this.hardDiskUsage = hardDiskUsage;
	}

	@Override
	public String toString() {
		return "NVRInfo{" +
				"loginName='" + loginName + '\'' +
				", loginPwd='" + loginPwd + '\'' +
				", channelSum='" + channelSum + '\'' +
				", hardDiskUsage='" + hardDiskUsage + '\'' +
				", " + super.toString() +
				'}';
	}
}

