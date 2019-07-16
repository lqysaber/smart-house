package h.uniview.smarthouse.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import h.uniview.smarthouse.data.ConfigMsg;

public class XstreamUtil<T> {
	
	/**
	 * 将xml转换为bean
	 * @param <T> 泛型
	 * @param xml 要转换为bean的xml
	 * @param cls bean对应的Class
	 * @return xml转换为bean
	 */
	public static <T> T xmlToObject(String xml, Class<T> cls){
		XStream xstream = new XStream(new DomDriver());
		//xstream使用注解转换
		xstream.processAnnotations(cls);
		return (T) xstream.fromXML(xml);
	}
	
	public static void main(String[] args) {
		ConfigMsg cm = xmlToObject("D:\\Users\\CN092227\\git\\smart-house\\src\\main\\resources\\uniview_cfg.xml", ConfigMsg.class);
		
		System.out.println(cm.getCameraManageCatalog());
		System.out.println(cm.getCameraOriginalCatalog());
		System.out.println(cm.getEnvironmentalFile());
		System.out.println(cm.getLocationFile());
		System.out.println(cm.getVideoScreenshotCatalog());
		System.out.println(cm.getVideoVideotapeCatalog());
		System.out.println(cm.getVideoVmdalarmCatalog());
	}

}
