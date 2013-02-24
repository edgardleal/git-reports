package com.github.gitreport.data;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import com.github.gitreport.util.Str;

/**
 * This is a singletom classto load configuration
 *
 * @author Edgard
 *
 */
public class Config {
	public static final String MANAGER_FOLDER;
	private String language;
	private static Config instance = null;
	private Log log = LogFactory.getLog(Config.class);
	private Document document = null;
	static {
		MANAGER_FOLDER = new File(".").getAbsolutePath();
	}

	private Config() {
		load();
	}

	private Document getXMLDocument() {
		if (document == null) {
			try {
				File file = new File("config.xml");
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				if (!file.exists())
					document = builder.newDocument();
				else
					document = builder.parse(file);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(this, e);
			}
			document.normalize();
		}
		return document;
	}

	private void load() {
		getXMLDocument();
	}

	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}

	/**
	 * Just to be used as constant
	 *
	 * @return
	 */
	public static String LANGUAGE() {
		return getInstance().getLanguage();
	}

	public String getLanguage() {
		if (Str.isNullOrEmpty(language))
			language = getProperty("language", "DUMMY_DEFAULT");
		return language;
	}

	public String getProperty(String name, String defaultValue) {
		String result = null;
		result = getXMLDocument().getElementsByTagName("language").item(0)
				.getTextContent();
		if (Str.isNullOrEmpty(result)) {
			result = defaultValue;
			getXMLDocument().createElement(name).setTextContent(defaultValue);
			saveXmlDocument(getXMLDocument());
		}
		return result;
	}

	private void saveXmlDocument(Document document) {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File("config.xml"));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(this, e);
		}
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
