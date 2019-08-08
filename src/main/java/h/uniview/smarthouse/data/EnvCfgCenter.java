package h.uniview.smarthouse.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import h.uniview.smarthouse.utils.Constant.ConfigEnvType;

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
@Order(2)
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

	private List<ServerNodeInfo> serverNodeList = new ArrayList<ServerNodeInfo>();
	private List<CameraInfo> cameraInfoList = new ArrayList<CameraInfo>();
	private List<NVRInfo> nvrInfoList = new ArrayList<NVRInfo>();
	private List<VideoNodeInfo> videoInfoList = new ArrayList<VideoNodeInfo>();

	public synchronized void upadteNode(String pattern, int cursor, Object object) throws Exception {
		File url = new File(configEnvDir);
		// 获取一个Document对象
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(url);

		Element element = (Element) doc.selectSingleNode(pattern+"["+cursor+"]");
		
		Class clazz = object.getClass();
		List<Field> fieldList = listField(clazz);
		fieldList.forEach(f -> {
			// 添加值到节点中
			try {
				Object o = getFieldValue(f, object);
				String nodeName = "ip".equals(f.getName())? "IP" : upperFirst(f.getName());
//				System.out.println("f.getName():"+f.getName());
				element.element(nodeName).setText(null == o ? "" : o.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// 注意：XML文件是被加载到内存中 修改也是在内存中 ==》因此需要将内存中的数据同步到磁盘中
		XMLWriter writer = new XMLWriter(new FileWriter(url));// 将内存数据关联给一个字符输出流
		writer.write(doc);
		writer.close();
		
		initialConfigData(this.configEnvDir);
	}

	public synchronized void crateNode(Object object, String pattern) throws Exception {
		File url = new File(configEnvDir);
		// 获取一个Document对象
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(url);

		// 获取根节点
		Element root = doc.getRootElement().selectSingleNode(pattern).getParent();
		
		Class<?> clazz = object.getClass();
//		System.out.println(clazz);
		// 添加一个标签 即一个节点
		Element nelement = root.addElement(clazz.getSimpleName());
		
		List<Field> fieldList = listField(clazz);
		fieldList.forEach(f -> {
			// 添加值到节点中
			try {
				Object o = getFieldValue(f, object);
				nelement.addElement("ip".equals(f.getName())? "IP":upperFirst(f.getName())).setText(null == o ? "" : o.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(new FileWriter(url), format);// 构造一个具有良好输出格式的XML输出对象
		writer.write(doc);
		writer.close();
		
//		initialConfigData(this.configEnvDir);
		if(clazz.equals(ServerNodeInfo.class)) {
			serverNodeList.add((ServerNodeInfo) object);
		} else if(clazz.equals(CameraInfo.class)) {
			cameraInfoList.add((CameraInfo) object);
		} else if(clazz.equals(VideoNodeInfo.class)) {
			videoInfoList.add((VideoNodeInfo) object);
		} else if(clazz.equals(NVRInfo.class)) {
			nvrInfoList.add((NVRInfo) object);
		} else {
			// do nothing..
		}
		
	}
	
	public static Object getFieldValue(Field field, Object o) throws Exception {
		String getter = "get" + upperFirst(field.getName());
		Method method = o.getClass().getMethod(getter, new Class[] {});
		return method.invoke(o, new Object[] {});
	}

	public synchronized void deleteNode(String pattern, int cursor) throws Exception {
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
		
		initialConfigData(this.configEnvDir);
	}

	public void initialConfigData(String configPath) throws Exception {

		File url = new File(configPath);
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(url);

		workstationMsg = convert2Object((Element) doc.selectSingleNode(ConfigEnvType.WROKSTATION.getValue()), WorkstationMsg.class);
//		System.out.println(workstationMsg);

		configMsg = convert2Object((Element) doc.selectSingleNode(ConfigEnvType.CONFIGDATA.getValue()), ConfigMsg.class);
//		System.out.println(configMsg);

		List<?> list = doc.selectNodes(ConfigEnvType.CAMERA.getValue());
		Iterator<?> it = list.iterator();
		List<CameraInfo> tmalist = new ArrayList<CameraInfo>();
		while (it.hasNext()) {
			tmalist.add(convert2Object((Element) it.next(), CameraInfo.class));
		}
		cameraInfoList = tmalist;
//		System.out.println(cameraInfoList);

		list = doc.selectNodes(ConfigEnvType.NVR.getValue());
		it = list.iterator();
		List<NVRInfo> nvrlist = new ArrayList<NVRInfo>();
		while (it.hasNext()) {
			nvrlist.add(convert2Object((Element) it.next(), NVRInfo.class));
		}
		nvrInfoList = nvrlist;
//		System.out.println(nvrInfoList);

		list = doc.selectNodes(ConfigEnvType.VIDEO.getValue());
		it = list.iterator();
		List<VideoNodeInfo> tvnilist = new ArrayList<VideoNodeInfo>();
		while (it.hasNext()) {
			tvnilist.add(convert2Object((Element) it.next(), VideoNodeInfo.class));
		}
		videoInfoList = tvnilist;
//		System.out.println(videoInfoList);

		list = doc.selectNodes(ConfigEnvType.SERVERNODE.getValue());
		it = list.iterator();
		List<ServerNodeInfo> tsnilist = new ArrayList<ServerNodeInfo>();
		while (it.hasNext()) {
			tsnilist.add(convert2Object((Element) it.next(), ServerNodeInfo.class));
		}
		serverNodeList = tsnilist;
//		System.out.println(serverNodeList);
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

	public ConfigMsg getConfigMsg() {
		return configMsg;
	}

	public WorkstationMsg getWorkstationMsg() {
		return workstationMsg;
	}

	public List<CameraInfo> getCameraInfoList() {
		return cameraInfoList;
	}

	public List<ServerNodeInfo> getServerNodeList() {
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
