package h.uniview.smarthouse.form;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class VideoForm extends CameraForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "设备登录用户名不能为空")
	private String loginName;

	@NotEmpty(message = "设备登录密码不能为空")
	private String loginPwd;

	@NotEmpty(message = "通道ID不能为空")
	@Min(value = 1, message = "请填写数字")
	private String channelID;

	@NotEmpty(message = "请选择云台标记")
	@Digits(integer = 1, fraction = 0)
	@Min(value = 0, message = "请选择云台标记")
	@Max(value = 1, message = "请选择云台标记")
	private String cloudControl;

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

	public String getCloudControl() {
		return cloudControl;
	}

	public void setCloudControl(String cloudControl) {
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
