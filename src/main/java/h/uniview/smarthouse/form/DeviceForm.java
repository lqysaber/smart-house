package h.uniview.smarthouse.form;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class DeviceForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Min(value = 0, message = "系统异常")
	@Max(value = 100, message = "系统异常")
	@Digits(integer = 3, fraction = 0)
	private String cursor;
	
	@Min(value = 1, message = "请选择正确的设备类型")
	@Max(value = 5, message = "请选择正确的设备类型")
	@Digits(integer = 1, fraction = 0)
	private String type;

	public String getCursor() {
		return cursor;
	}

	public void setCursor(String cursor) {
		this.cursor = cursor;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
