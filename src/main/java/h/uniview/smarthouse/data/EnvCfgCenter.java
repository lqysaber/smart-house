package h.uniview.smarthouse.data;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(1)
public class EnvCfgCenter implements CommandLineRunner,Serializable {
	
	@Autowired
	private PropCenter propCenter;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private WorkstationMsg workstationMsg = new WorkstationMsg();
	
	private ConfigMsg configMsg = new ConfigMsg();

    private List<ServerNode> serverNodeList = new ArrayList<ServerNode>();
    private List<CameraInfo> cameraInfoList = new ArrayList<CameraInfo>();
    private List<NVRInfo> nvrInfoList = new ArrayList<NVRInfo>();
    private List<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();
    
    public void upadteNode(String pattern, Object object) throws Exception {
    	File url = new File(propCenter.getConfigDir());
		// 获取一个Document对象
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(url);
		
		// 获取id为1的节点下的name元素
		Node name = doc.selectSingleNode("/persons/person[@id = '1']/name[1]");
		name.setText("这里是修改的id为1的节点下的name元素值");
 
		// 注意：XML文件是被加载到内存中 修改也是在内存中 ==》因此需要将内存中的数据同步到磁盘中
		XMLWriter writer = new XMLWriter(new FileWriter(url));//将内存数据关联给一个字符输出流
		writer.write(doc);
		writer.close();
	}
 
	public void crateNode() throws Exception {
		File url = new File(propCenter.getConfigDir());
		// 获取一个Document对象
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(url);
		
		//获取根节点
		Element root = doc.getRootElement();
		//添加一个标签 即一个节点
		Element person = root.addElement("person");
 
		//添加值到节点中
		person.addElement("name").setText("小星");
		person.addElement("age").setText("16");
		person.addElement("sex").setText("女");
 
		// 注意：XML文件是被加载到内存中 修改也是在内存中 ==》因此需要将内存中的数据同步到磁盘中
		//XMLWriter writer = new XMLWriter(new FileWriter(url));
		
		// 按照指定格式将dom打印到xml文件中
		//方式一：
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(new FileWriter(url), format);//构造一个具有良好输出格式的XML输出对象
		writer.write(doc);
		writer.close();
		
		//方式二：
//		OutputFormat format = OutputFormat.createCompactFormat();
//	    writer = new XMLWriter(new FileWriter(url), format);
//	    writer.write( doc );
//	    writer.close();
	}
	
	public void deleteNode() throws Exception {
		File url = new File(propCenter.getConfigDir());
		// 获取一个Document对象
		SAXReader reader = new SAXReader();
		Document doc = reader.read(url);		
		
		// 通过文档对象doc获取文档中的根节点
		Element root = doc.getRootElement();
		//获取第2个节点
		Node person = root.selectSingleNode("/persons/person[2]");
		//移出节点
		root.remove(person);
		
		// 注意：XML文件是被加载到内存中 修改也是在内存中 ==》因此需要将内存中的数据同步到磁盘中
		XMLWriter writer = new XMLWriter(new FileWriter(url));
		
		// 按照指定格式将dom打印到xml文件中
		OutputFormat format = OutputFormat.createPrettyPrint();
        writer = new XMLWriter(new FileWriter(url), format );
        writer.write( doc );
        writer.close();
	}

	public void initialConfigData(String configPath) throws Exception {
		
		File url = new File(configPath);
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(url);
		
        workstationMsg = convert2Object((Element) doc.selectSingleNode("/Configuration/WorkstationMsg"), WorkstationMsg.class);
        System.out.println(workstationMsg);

        configMsg = convert2Object((Element) doc.selectSingleNode("/Configuration/ConfigMsg"), ConfigMsg.class);
        System.out.println(configMsg);

        List<?> list = doc.selectNodes("/Configuration/DevMsg/CameraMsg/CameraInfo");
        Iterator<?> it = list.iterator();
        while (it.hasNext()) {
        	cameraInfoList.add(convert2Object((Element) it.next(), CameraInfo.class));
		}
        System.out.println(cameraInfoList);

        list = doc.selectNodes("/Configuration/DevMsg/NVRMSG/NVRInfo");
        it = list.iterator();
        while (it.hasNext()) {
        	nvrInfoList.add(convert2Object((Element) it.next(), NVRInfo.class));
		}
        System.out.println(nvrInfoList);

        list = doc.selectNodes("/Configuration/DevMsg/VideoMsg/VideoNodeInfo");
        it = list.iterator();
        while (it.hasNext()) {
        	videoInfoList.add(convert2Object((Element) it.next(), VideoInfo.class));
		}
        System.out.println(videoInfoList);
        
        list = doc.selectNodes("/Configuration/ServerMsg/ServerNodeInfo");
        it = list.iterator();
        while (it.hasNext()) {
        	serverNodeList.add(convert2Object((Element) it.next(), ServerNode.class));
		}
        System.out.println(serverNodeList);
	}

	public static <T> T convert2Object(Element element, Class<T> clazz) throws Exception {
        Map<String, String> map = new HashMap<>();

        Iterator<?> it = element.elementIterator();
        while(it.hasNext()) {
            Element tmp = (Element) it.next();
            map.put(tmp.getName(), tmp.getTextTrim());
        }

        Iterator<?> attrIT = element.attributeIterator();
        while(attrIT.hasNext()) {
            Attribute tmp = (Attribute) attrIT.next();
            map.put(tmp.getName(), tmp.getValue());
        }

        return mapToBean(map, clazz);
    }

	public static <T> T mapToBean(Map<String, String> map, Class<T> clazz) throws Exception {
		T obj = clazz.newInstance();
		if(null == map || map.isEmpty()) {
			return obj;
		}
		map.forEach((propertyName, value) -> {
			String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
			Field field = getClassField(clazz, propertyName);
			Class<?> fieldTypeClass = field.getType();
//			clazz.getMethod(setMethodName, field.getType()).invoke(obj, convertValType(value, fieldTypeClass));
		});
		return obj;
	}

	private static <T> T convertValType(String value, Class<T> fieldTypeClass) {
		if(fieldTypeClass instanceof String)
		return null;
	}

	private static Field getClassField(Class<?> clazz, String fieldName) {
		if (Object.class.getName().equals(clazz.getName())) {
			return null;
		}
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}

		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null) {// 简单的递归一下
			return getClassField(superClass, fieldName);
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		
		System.out.println(Long.class);
		System.out.println(long.class);
		System.out.println(Long.class.getName());
		
		Field f = getClassField(VideoInfo.class, "Name");
		System.out.println(f);
		System.out.println(f.getType());
		
//		initialConfigData("D:\\Application\\hugo-git-ws\\smart-house\\src\\main\\resources\\uniview.xml");
		
//		initialConfigData("D:\\Users\\CN092227\\git\\smart-house\\src\\main\\resources\\uniview.xml");
	}

	public ConfigMsg getConfigMsg() {
		return configMsg;
	}

	public WorkstationMsg getWorkstationMsg() {
		return workstationMsg;
	}

	public List<CameraInfo> getCameraInfoList() {
		return cameraInfoList;
	}

	public List<ServerNode> getServerNodeList() {
		return serverNodeList;
	}

	public List<NVRInfo> getNvrInfoList() {
		return nvrInfoList;
	}

	public List<VideoInfo> getVideoInfoList() {
		return videoInfoList;
	}

	@Override
	public void run(String... args) throws Exception {
		this.initialConfigData(propCenter.getConfigDir());
	}

}
