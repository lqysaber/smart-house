package h.uniview.smarthouse.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Configuration
@ConfigurationProperties(prefix = "smart-house")
public class PropCenter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ShiroProperties shiro = new ShiroProperties();

	private String timeFormat = "yyyy-MM-dd HH:mm:ss";
	
	private String configDir;
	private String userDir;

	public ShiroProperties getShiro() {
		return shiro;
	}

	public void setShiro(ShiroProperties shiro) {
		this.shiro = shiro;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public String getConfigDir() {
		return configDir;
	}

	public void setConfigDir(String configDir) throws Exception {
		this.configDir = configDir;
	}

	public String getUserDir() {
		return userDir;
	}

	public void setUserDir(String userDir) {
		this.userDir = userDir;
	}
}
