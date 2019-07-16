package h.uniview.smarthouse.data;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "uniview")
@PropertySource("classpath:/uniview.properties")
public class UniviewCenter {
	
	private String wsname;
	
	private String wsnumber;
	
	private Map<String, String> filedir = new HashMap<String, String>();
	
	private Map<String, String> datadir = new HashMap<String, String>();

	public String getWsname() {
		return wsname;
	}

	public void setWsname(String wsname) {
		this.wsname = wsname;
	}

	public String getWsnumber() {
		return wsnumber;
	}

	public void setWsnumber(String wsnumber) {
		this.wsnumber = wsnumber;
	}

	public Map<String, String> getFiledir() {
		return filedir;
	}

	public void setFiledir(Map<String, String> filedir) {
		this.filedir = filedir;
	}

	public Map<String, String> getDatadir() {
		return datadir;
	}

	public void setDatadir(Map<String, String> datadir) {
		this.datadir = datadir;
	}
	
}
