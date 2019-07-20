package h.uniview.smarthouse.data;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

@Service
@Order(1)
public class EnvCfgCenter implements CommandLineRunner, Serializable {

	@Autowired
	private PropCenter propCenter;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String configEnvDir = null;
	private String userCenterDir = null;

	private WorkstationMsg workstationMsg = new WorkstationMsg();

	private ConfigMsg configMsg = new ConfigMsg();

	private List<ServerNode> serverNodeList = new ArrayList<ServerNode>();
	private List<CameraInfo> cameraInfoList = new ArrayList<CameraInfo>();
	private List<NVRInfo> nvrInfoList = new ArrayList<NVRInfo>();
	private List<VideoNodeInfo> videoInfoList = new ArrayList<VideoNodeInfo>();

	public void upadteNode(String pattern, Object object) throws Exception {
		File url = new File(configEnvDir);
		// 获取一个Document对象
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(url);

		// 获取id为1的节点下的name元素
		Node name = doc.selectSingleNode("/persons/person[@id = '1']/name[1]");
		name.setText("这里是修改的id为1的节点下的name元素值");

		// 注意：XML文件是被加载到内存中 修改也是在内存中 ==》因此需要将内存中的数据同步到磁盘中
		XMLWriter writer = new XMLWriter(new FileWriter(url));// 将内存数据关联给一个字符输出流
		writer.write(doc);
		writer.close();
	}

	public void crateNode(Object object, String pattern) throws Exception {
		File url = new File(configEnvDir);
		// 获取一个Document对象
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(url);

		// 获取根节点
		Element root = doc.getRootElement().selectSingleNode(pattern).getParent();
		
		Class<?> clazz = object.getClass();
		System.out.println(clazz);
		// 添加一个标签 即一个节点
		Element nelement = root.addElement(clazz.getSimpleName());
		
		List<Field> fieldList = listField(clazz);
		fieldList.forEach(f -> {
			// 添加值到节点中
			try {
				Object o = getFieldValue(f, object);
				nelement.addElement(upperFirst(f.getName())).setText(null == o ? "" : o.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(new FileWriter(url), format);// 构造一个具有良好输出格式的XML输出对象
		writer.write(doc);
		writer.close();
	}
	
	public static Object getFieldValue(Field field, Object o) throws Exception {
		String getter = "get" + upperFirst(field.getName());
		Method method = o.getClass().getMethod(getter, new Class[] {});
		return method.invoke(o, new Object[] {});
	}

	public void deleteNode(String pattern, int cursor) throws Exception {
		File url = new File(configEnvDir);
		// 获取一个Document对象
		SAXReader reader = new SAXReader();
		Document doc = reader.read(url);
		Element root = doc.getRootElement().selectSingleNode(pattern).getParent();
		// 获取第2个节点
		Node person = root.selectSingleNode(pattern + "[" + cursor + "]");
		root.remove(person);

		XMLWriter writer = new XMLWriter(new FileWriter(url));
		OutputFormat format = OutputFormat.createPrettyPrint();
		writer = new XMLWriter(new FileWriter(url), format);
		writer.write(doc);
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
			videoInfoList.add(convert2Object((Element) it.next(), VideoNodeInfo.class));
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
		while (it.hasNext()) {
			Element tmp = (Element) it.next();
			map.put(lowerFirst(convertNodeName(tmp.getName())), emptyStr(tmp.getTextTrim()));
		}

		Iterator<?> attrIT = element.attributeIterator();
		while (attrIT.hasNext()) {
			Attribute tmp = (Attribute) attrIT.next();
			map.put(lowerFirst(convertNodeName(tmp.getName())), emptyStr(tmp.getValue()));
		}

		return mapToBean(map, clazz);
	}

	public static String lowerFirst(String oldStr) {
//		char[] chars = oldStr.toCharArray();
//		chars[0] += 32;
//		return String.valueOf(chars);
		if(StringUtils.isEmpty(oldStr)) {
			return "";
		}
		return oldStr.substring(0,1).toLowerCase() + oldStr.substring(1);
	}
	
	public static String upperFirst(String oldStr) {
		if(StringUtils.isEmpty(oldStr)) {
			return "";
		}
		return oldStr.substring(0,1).toUpperCase() + oldStr.substring(1);
	}

	public static String emptyStr(String value) {
		if (null == value) {
			return "";
		}
		return value;
	}

	public static <T> T mapToBean(Map<String, String> map, Class<T> clazz) throws Exception {
		if (null == map || map.isEmpty()) {
			return null;
		}

		ObjectMapper objectMapper = new ObjectMapper();
		T obj = objectMapper.convertValue(map, clazz);

		return obj;
	}

	public static List<Field> listField(Class<?> clazz) {
		if(null == clazz) {
			return null;
		}
		List<Field> fieldList = new ArrayList<Field>();
		Field[] fields = clazz.getDeclaredFields();
	    for(Field field : fields){
	        /** 过滤静态属性**/
	        if(Modifier.isStatic(field.getModifiers())){
	            continue;
	        }
	        /** 过滤transient 关键字修饰的属性**/
	        if(Modifier.isTransient(field.getModifiers())){
	            continue;
	        }
	        fieldList.add(field);
	    }
	    /** 处理父类字段**/
	    Class<?> superClass = clazz.getSuperclass();
	    if(superClass.equals(Object.class)) {
	        return fieldList;
	    }
	    fieldList.addAll(listField(superClass));
	    return fieldList;
	}

	public static void main(String[] args) throws Exception {
		
		EnvCfgCenter env = new EnvCfgCenter();
		env.setConfigEnvDir("D:\\Users\\CN092227\\git\\smart-house\\src\\main\\resources\\uniview.xml");

		CameraInfo o = new CameraInfo();
		o.setIp("192.169.2.2");
		o.setMess("mark");
		o.setName("test.li");
		o.setPort("6379");
		o.setStatus("working");
		o.setType("photo 222222");
		
//		env.crateNode(o, ConfigEnvType.CAMERA.getValue());
//		env.deleteNode(ConfigEnvType.CAMERA.getValue(), 2);

		new EnvCfgCenter().initialConfigData("D:\\Application\\hugo-git-ws\\smart-house\\src\\main\\resources\\uniview.xml");

//		new EnvCfgCenter().initialConfigData("D:\\Users\\CN092227\\git\\smart-house\\src\\main\\resources\\uniview.xml");
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

	public List<VideoNodeInfo> getVideoInfoList() {
		return videoInfoList;
	}
	
	public void setConfigEnvDir(String configEnvDir) {
		this.configEnvDir = configEnvDir;
	}

	@Override
	public void run(String... args) throws Exception {
		this.configEnvDir = propCenter.getConfigDir();
		this.initialConfigData(configEnvDir);
	}

	public static String convertNodeName(String name) {
		if("IP".equals(name)) {
			return "Ip";
		}
		return name;
	}

}
