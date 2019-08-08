package h.uniview.smarthouse.data;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@Order(1)
public class UserXMLCenter implements CommandLineRunner, Serializable {

	@Autowired
	private PropCenter propCenter;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userDir;
	private Map<String, String> loginMap = new HashMap<String, String>();

	public void initialUserData(String userPath) throws Exception {
		File url = new File(userPath);
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(url);

		List<?> list = doc.selectNodes("/UserList/User");
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Element uElement = (Element) it.next();
			loginMap.put(uElement.element("Username").getTextTrim(), uElement.element("Password").getTextTrim());
		}

//		loginMap.forEach((k, v) -> {
//			System.out.println("username:" + k + ",pwd:" + v);
//		});
	}

	public void updatePassword(String username, String password) throws Exception {
		File url = new File(userDir);
		// 获取一个Document对象
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(url);

		// 获取id为d**的节点下的Password元素
		Node pwdNode = doc.selectSingleNode("/UserList/User[@id = '" + username + "']/Password[1]");
		pwdNode.setText(password);

		XMLWriter writer = new XMLWriter(new FileWriter(url));// 将内存数据关联给一个字符输出流
		writer.write(doc);
		writer.close();
	}

	@Override
	public void run(String... args) throws Exception {
		this.userDir = propCenter.getUserDir();
		this.initialUserData(userDir);
	}

	public Map<String, String> getLoginMap() {
		return this.loginMap;
	}

}
