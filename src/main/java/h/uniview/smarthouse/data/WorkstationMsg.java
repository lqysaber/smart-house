package h.uniview.smarthouse.data;

import java.io.Serializable;

public class WorkstationMsg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String Name;
	
	private String Code;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		this.Code = code;
	}
	

}
