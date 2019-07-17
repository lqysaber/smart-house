package h.uniview.smarthouse.data;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public class Configuration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static WorkstationMsg workstationMsg = new WorkstationMsg();
	
	private static ConfigMsg configMsg = new ConfigMsg();

    private static List<ServerMsg> serverMsgList = new ArrayList<ServerMsg>();
    private static List<CameraInfo> cameraList = new ArrayList<CameraInfo>();
    private static List<NVRInfo> NVRList = new ArrayList<NVRInfo>();
    private static List<BVideoInfo> bVideoList = new ArrayList<BVideoInfo>();
    private static List<GVideoInfo> gVideoList = new ArrayList<GVideoInfo>();


    public static void upadteConfigData(File url) throws Exception {
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
 
	public static void dom4jAddElement(File url) throws Exception {
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
	
	public static void dom4jDeleteElement(File url) throws Exception {
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

	public static void initialConfigData(String configPath) throws Exception {
		
		File url = new File(configPath);
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(url);
		
        workstationMsg = convert2Object((Element) doc.selectSingleNode("/Configuration/WorkstationMsg"), WorkstationMsg.class);
        System.out.println(workstationMsg);

        configMsg = convert2Object((Element) doc.selectSingleNode("/Configuration/ConfigMsg"), ConfigMsg.class);
        System.out.println(configMsg);

        List<Element> camList = (List<Element>) doc.selectNodes("/Configuration/DevMsg/CameraInfo");
        for(Element element : camList) {
            cameraList.add(convert2Object(element, CameraInfo.class));
        }
        System.out.println(cameraList);

        List<Element> nvrList = (List<Element>) doc.selectNodes("/Configuration/DevMsg/NVRInfo");
        for(Element element : nvrList) {
            NVRList.add(convert2Object(element, NVRInfo.class));
        }
        System.out.println(NVRList);

        List<Element> bvideoList = (List<Element>) doc.selectNodes("/Configuration/DevMsg/BVideoInfo");
        for(Element element : bvideoList) {
            bVideoList.add(convert2Object(element, BVideoInfo.class));
        }
        System.out.println(bVideoList);

        List<Element> gvideoList = (List<Element>) doc.selectNodes("/Configuration/DevMsg/GVideoInfo");
        for(Element element : gvideoList) {
            gVideoList.add(convert2Object(element, GVideoInfo.class));
        }
        System.out.println(gVideoList);
	
	}

	public static <T> T convert2Object(Element element, Class<T> clazz) throws Exception {
        Map<String, Object> map = new HashMap<>();

        Iterator it = element.elementIterator();
        while(it.hasNext()) {
            Element tmp = (Element) it.next();
            map.put(tmp.getName(), tmp.getTextTrim());
        }

        Iterator attrIT = element.attributeIterator();
        while(attrIT.hasNext()) {
            Attribute tmp = (Attribute) attrIT.next();
            map.put(tmp.getName(), tmp.getValue());
        }

        return mapToBean(map, clazz);
    }

	public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) throws Exception {
		T obj = clazz.newInstance();
		if(null == map || map.isEmpty()) {
			return obj;
		}
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String propertyName = entry.getKey();
			Object value = entry.getValue();
			String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
			Field field = getClassField(clazz, propertyName);
			Class<?> fieldTypeClass = field.getType();
			value = convertValType(value, fieldTypeClass);
			clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);
		}
		return obj;
	}

	private static Object convertValType(Object value, Class<?> fieldTypeClass) {
		Object retVal = null;
		if (Long.class.getName().equals(fieldTypeClass.getName())
				|| long.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Long.parseLong(value.toString());
		} else if (Integer.class.getName().equals(fieldTypeClass.getName())
				|| int.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Integer.parseInt(value.toString());
		} else if (Float.class.getName().equals(fieldTypeClass.getName())
				|| float.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Float.parseFloat(value.toString());
		} else if (Double.class.getName().equals(fieldTypeClass.getName())
				|| double.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Double.parseDouble(value.toString());
		} else {
			retVal = value;
		}
		return retVal;
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
		
		initialConfigData("D:\\Application\\hugo-git-ws\\smart-house\\src\\main\\resources\\uniview.xml");
		
//		File uri = new File("D:\\Users\\CN092227\\git\\smart-house\\src\\main\\resources\\uniview.xml");
	}

	
	

	public ConfigMsg getConfigMsg() {
		return configMsg;
	}

	public void setConfigMsg(ConfigMsg configMsg) {
		this.configMsg = configMsg;
	}

	public WorkstationMsg getWorkstationMsg() {
		return workstationMsg;
	}

	public void setWorkstationMsg(WorkstationMsg workstationMsg) {
		this.workstationMsg = workstationMsg;
	}

}
