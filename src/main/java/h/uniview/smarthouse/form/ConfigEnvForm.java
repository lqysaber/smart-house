package h.uniview.smarthouse.form;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class ConfigEnvForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	private String locationFile;
	
	@NotNull
	private String environmentalFile;

	public String getLocationFile() {
		return locationFile;
	}

	public void setLocationFile(String locationFile) {
		this.locationFile = locationFile;
	}

	public String getEnvironmentalFile() {
		return environmentalFile;
	}

	public void setEnvironmentalFile(String environmentalFile) {
		this.environmentalFile = environmentalFile;
	}

}
