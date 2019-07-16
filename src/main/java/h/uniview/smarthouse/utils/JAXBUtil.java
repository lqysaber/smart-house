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

public class JAXBUtil<T> {

	@SuppressWarnings("unchecked")
	public T unmarshal(Class<T> clazz, InputStream is) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller un = context.createUnmarshaller();
		return (T) un.unmarshal(is);
	}

	public T unmarshal(Class<T> clazz, File file) throws JAXBException, FileNotFoundException {
		return unmarshal(clazz, new FileInputStream(file));
	}

	public void marshal(T element, OutputStream os) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(element.getClass());
		Marshaller m = jc.createMarshaller();
		m.marshal(element, os);
	}

	public void marshal(T element, File output) throws FileNotFoundException, JAXBException {
		marshal(element, new FileOutputStream(output));
	}

	/**
	 * Java Object->Xml.
	 */
	public String toXml(T element) {
		try {
			StringWriter writer = new StringWriter();
			JAXBContext jc = JAXBContext.newInstance(element.getClass());
			Marshaller m = jc.createMarshaller();
			m.marshal(element, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Java Object->Xml.(with encoding)
	 */
	public String toXml(T element, String encoding) {
		try {
			StringWriter writer = new StringWriter();
			JAXBContext jc = JAXBContext.newInstance(element.getClass());
			Marshaller m = jc.createMarshaller();
			if (encoding != null) {
				if (encoding.length() > 0) {
					m.setProperty(Marshaller.JAXB_ENCODING, encoding);
				}
			}
			m.marshal(element, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Java Xml->Object.
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromXml(T element, String fromXml) {
		try {
			JAXBContext jc = JAXBContext.newInstance(element.getClass());
			Unmarshaller um = jc.createUnmarshaller();
			StringReader reader = new StringReader(fromXml);
			return (T) um.unmarshal(reader);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Java Xml->Object.(with caseSensitive)
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromXml(T element, String fromXml, boolean caseSensitive) {
		try {
			JAXBContext jc = JAXBContext.newInstance(element.getClass());
			Unmarshaller um = jc.createUnmarshaller();
			StringReader reader = new StringReader(fromXml);
			return (T) um.unmarshal(reader);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

}
