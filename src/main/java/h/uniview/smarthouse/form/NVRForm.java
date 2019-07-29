package h.uniview.smarthouse.form;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class NVRForm extends CameraForm implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "设备登录用户名不能为空")
	private String loginName;

	@NotEmpty(message = "设备登录密码不能为空")
	private String loginPwd;

	@NotEmpty(message = "通道数量不能为空")
	@Digits(integer = 3, fraction = 0)
	@Min(value = 1, message = "通道数量最小为1")
	@Max(value = 100, message = "通道数量最大为100")
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

