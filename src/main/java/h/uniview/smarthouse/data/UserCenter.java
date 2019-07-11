package h.uniview.smarthouse.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "uc")
@PropertySource("classpath:/uc.properties")
public class UserCenter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> loginMap = new HashMap<String, String>();
	
	private List<String> loginlist = new ArrayList<String>();
	
	public void setLoginlist(List<String> loginlist) {
		this.loginlist = loginlist;
		
		if(CollectionUtils.isEmpty(loginlist)) {
			return;
		}
		this.loginlist.forEach(i->{
			String[] up = i.split(",");
            getLoginMap().put(up[0], up[1]);
        });
	}
	
	public Map<String, String> getLoginMap() {
		return this.loginMap;
	}

}
