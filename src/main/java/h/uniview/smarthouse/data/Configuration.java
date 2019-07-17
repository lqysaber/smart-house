package h.uniview.smarthouse.data;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Configuration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static WorkstationMsg workstationMsg = new WorkstationMsg();
	
	private static ServerMsg serverMsg = new ServerMsg();
	
	private static ConfigMsg configMsg = new ConfigMsg();
	
	 
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
		
		Object value = null;
		Element elem = null;
		Element wsElement = (Element) doc.selectSingleNode("/Configuration/WorkstationMsg");
		Iterator wsIT = wsElement.elementIterator();
		while(wsIT.hasNext()) {
			elem = (Element) wsIT.next();
			System.out.println(elem.getName()+","+elem.getTextTrim());
			
//			String setMethodName = "set" + elem.getName();
//			Field field = getClassField(WorkstationMsg.class, elem.getName());
//			Class<?> fieldTypeClass = field.getType();
//			value = convertValType(elem.getTextTrim(), fieldTypeClass);
//			WorkstationMsg.class.getMethod(setMethodName, field.getType()).invoke(workstationMsg, value);
			mapObject(elem.getName(), elem.getTextTrim(), WorkstationMsg.class, workstationMsg);
		}
		System.out.println(workstationMsg.getName());
		System.out.println(workstationMsg.getCode());
		
		
	
	}
	
	public static void mapObject(String key, String value, Class<?> clazz, Object obj) throws Exception {
		String setMethodName = "set" + key;
		Field field = getClassField(WorkstationMsg.class, key);
		Class<?> fieldTypeClass = field.getType();
		Object v = convertValType(value, fieldTypeClass);
		clazz.getMethod(setMethodName, field.getType()).invoke(obj, v);
	}

	/**
	 * 将Map对象通过反射机制转换成Bean对象
	 * 
	 * @param map   存放数据的map对象
	 * @param clazz 待转换的class
	 * @return 转换后的Bean对象
	 * @throws Exception 异常
	 */
	public static Object mapToBean(Map<String, Object> map, Class<?> clazz) throws Exception {
		Object obj = clazz.newInstance();
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

	/**
	 * 将Object类型的值，转换成bean对象属性里对应的类型值
	 * 
	 * @param value          Object对象值
	 * @param fieldTypeClass 属性的类型
	 * @return 转换后的值
	 */
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

	/**
	 * 获取指定字段名称查找在class中的对应的Field对象(包括查找父类)
	 * 
	 * @param clazz     指定的class
	 * @param fieldName 字段名称
	 * @return Field对象
	 */
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
		
		initialConfigData("D:\\Users\\CN092227\\git\\smart-house\\src\\main\\resources\\uniview.xml");
		
//		File uri = new File("D:\\Users\\CN092227\\git\\smart-house\\src\\main\\resources\\uniview.xml");
//		System.out.println(cm.getCameraManageCatalog());
//		System.out.println(cm.getCameraOriginalCatalog());
//		System.out.println(cm.getEnvironmentalFile());
//		System.out.println(cm.getLocationFile());
//		System.out.println(cm.getVideoScreenshotCatalog());
//		System.out.println(cm.getVideoVideotapeCatalog());
//		System.out.println(cm.getVideoVmdalarmCatalog());
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

	public ServerMsg getServerMsg() {
		return serverMsg;
	}

	public void setServerMsg(ServerMsg serverMsg) {
		this.serverMsg = serverMsg;
	}
	
}
