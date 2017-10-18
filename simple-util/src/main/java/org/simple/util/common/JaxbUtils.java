package org.simple.util.common;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.simple.util.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author dailiwei
 *
 */
public class JaxbUtils {

	private static final Logger logger = LoggerFactory.getLogger(JaxbUtils.class);
	private static Map<String, JAXBContext> jaxbMap = new HashMap<>();
	private static final boolean ENABLE_FORMATTED_XML = false;
	public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

	/**
	 * 转换成xml文本
	 * 
	 * @param obj
	 * @return
	 */
	public static String toXml(Object obj) {
		return toXml(obj, Constants.UTF_8);
	}

	/**
	 * 转换成xml文本
	 * 
	 * @param obj
	 *            javabean对象
	 * @param encoding
	 *            编码
	 * @return xml文本
	 */
	public static String toXml(Object obj, String encoding) {
		String result = "";
		try {
			JAXBContext jaxbContext = jaxbMap.get(obj.getClass().getName());
			if (jaxbContext == null) {
				jaxbContext = JAXBContext.newInstance(obj.getClass());
				jaxbMap.put(obj.getClass().getName(), jaxbContext);
			}
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, ENABLE_FORMATTED_XML);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

			try (StringWriter sw = new StringWriter()) {
				marshaller.marshal(obj, sw);
				result = sw.toString();
			}
		} catch (JAXBException ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public static String toXmlNoHeader(Object obj) {
		return toXmlNoHeader(obj, Constants.UTF_8);
	}

	public static String toXmlNoHeader(Object obj, String encoding) {
		String result = "";
		try {
			JAXBContext jaxbContext = jaxbMap.get(obj.getClass().getName());
			if (jaxbContext == null) {
				jaxbContext = JAXBContext.newInstance(obj.getClass());
				jaxbMap.put(obj.getClass().getName(), jaxbContext);
			}
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, ENABLE_FORMATTED_XML);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

			try (StringWriter sw = new StringWriter()) {
				marshaller.marshal(obj, sw);
				result = StringUtils.replaceEach(sw.toString(),
						new String[] { "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "ns2:", ":ns2" }, new String[] { "", "", "" });
			}
		} catch (JAXBException ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
		} catch (IOException ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> T toBean(String xml, Class<T> type) {
		T instance = null;
		try {
			JAXBContext jaxbContext = jaxbMap.get(type.getName());
			if (jaxbContext == null) {
				jaxbContext = JAXBContext.newInstance(type);
				jaxbMap.put(type.getName(), jaxbContext);

			}

			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			try (StringReader sr = new StringReader(xml)) {
				instance = (T) unmarshaller.unmarshal(sr);
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
		}
		return instance;
	}
}