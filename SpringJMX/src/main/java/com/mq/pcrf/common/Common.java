package com.mq.pcrf.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mq.osslog.ServerLog;
import com.mq.pcrf.cache.RadDatastoreCache;
import com.mq.pcrf.cache.RadGroupCache;
import com.mq.pcrf.config.PcrfConfigFactory;

public class Common {

	public static Map<String, String> loadPropDataToMapCache = null;
	public static Map<String, String> loadXmlAttrbutesByTagNameToMapCache = null;
	public static String getDatabaseTypeNameCache = null;
	public static String fileSeprator = System.getProperty("file.separator");
	public static String strMqPcrfHome = System.getProperty("MQPcrf.home");

	private static Set<Object> queriesDetail = new TreeSet<Object>();

	public static Map<String, String> loadXmlAttrbutesByTagNameToMap(
			String filePath, Map<String, String> mapObj, String tagName) {

		

			ServerLog
					.debug("Entered Into Common.loadXmlAttrbutesByTagNameToMap");
			if (Common.loadXmlAttrbutesByTagNameToMapCache==null) {
				Common.loadXmlAttrbutesByTagNameToMapCache = new HashMap<String, String>();
				try {
				File filepath = new File(filePath);
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(filepath);
				doc.getDocumentElement().normalize();

				if (tagName != null) {
					NodeList nodeList = doc.getElementsByTagName(tagName);
					if (nodeList.getLength() == 1) {
						Node node = nodeList.item(0);
						NamedNodeMap rootMap = node.getAttributes();
						if (rootMap != null) {
							for (int i = 0; i < rootMap.getLength(); ++i) {
								mapObj.put(rootMap.item(i).getNodeName(), rootMap
										.item(i).getNodeValue());
							}
						}
					} else {
						ServerLog.error(tagName
								+ "  related details not available in" + filePath,
								null);
					}
				}

				

			} catch (Exception ex) {
				ServerLog
						.error("Failed to execute the Common.loadXmlAttrbutesByTagNameToMap",
								ex);
			}
				ServerLog.debug("Loaded Map Type Successfully From The "+filePath+" path File Common.loadXmlAttrbutesByTagNameToMap()");
				ServerLog
				.debug("Exesting from Common.loadXmlAttrbutesByTagNameToMap");
			Common.loadXmlAttrbutesByTagNameToMapCache=mapObj;
			return mapObj;
			}
			ServerLog.debug("Returning Map From The Cache Memory in Common.loadXmlAttrbutesByTagNameToMap()");
			ServerLog
			.debug("Exesting from Common.loadXmlAttrbutesByTagNameToMap");
			return Common.loadXmlAttrbutesByTagNameToMapCache;
				
			}
			
		

	

	public static String getDatabaseTypeName() {
		
		HashMap<String, String> pcrfConfig = new HashMap<String, String>();

		ServerLog.debug("Entered Into Common.getDatabaseTypeName()");
		if (getDatabaseTypeNameCache==null) {
			try {
				String fileName = "MQPcrf-Configuration.xml";
				File filepath = new File(Common.strMqPcrfHome + fileSeprator + "conf"
						+ fileSeprator + fileName);
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(filepath);
				doc.getDocumentElement().normalize();
				Element root = doc.getDocumentElement();
				NamedNodeMap rootMap = root.getAttributes();
				pcrfConfig = new HashMap<String, String>();
				if (rootMap != null) {
					for (int i = 0; i < rootMap.getLength(); ++i) {
						pcrfConfig.put(rootMap.item(i).getNodeName(),
								rootMap.item(i).getNodeValue());
					}
				}

			} catch (Exception e) {
				ServerLog
						.debug("Failed to execute the Common.getDatabaseTypeName()");
			} 			
			Common.getDatabaseTypeNameCache = pcrfConfig.get("dataBaseType").toLowerCase();
			ServerLog.debug("Loaded Database Type Successfully From The MQPcrf-Configuration.xml Configuration File Common.getDatabaseTypeName()");
			ServerLog.debug("Exiting from Common.getDatabaseTypeName()");
			return Common.getDatabaseTypeNameCache;
			
		}
		ServerLog.debug("Returning Database Type From The Cache Memory in Common.getDatabaseTypeName()");
		ServerLog.debug("Exiting from Common.getDatabaseTypeName()");
		return Common.getDatabaseTypeNameCache;
	
	}

	public static Map<String, String> loadPropDataToMap(String filePath,
			Map<String, String> mapObj, String appendName) {
		ServerLog.debug("Entered Into Common.loadPropDataToMap");
		String key;
		String value;
		if (Common.loadPropDataToMapCache == null) {
			Common.loadPropDataToMapCache= new HashMap<String, String>();
			File file = new File(filePath);
			if (file.exists()) {
				Properties pro = new Properties();
				FileInputStream inputStream;
				try {
					inputStream = new FileInputStream(file);
					pro.load(inputStream);
					queriesDetail = pro.keySet();
					Iterator<Object> itr = queriesDetail.iterator();
					while (itr.hasNext()) {
						key = (String) itr.next();

						value = pro.getProperty(key);
						if (key.startsWith(appendName)) {
							key = key.substring(appendName.length() + 1);
							mapObj.put(key, value);
						}
					}
				

				} catch (IOException e) {
					e.printStackTrace();
					ServerLog
							.error("Failed to execute the Common.loadPropDataToMap",
									e);
				}

			}
			ServerLog.debug("Loaded Map Type Successfully From The "+filePath+"path File Common.loadPropDataToMap()");
			ServerLog.debug("Existing From Common.loadPropDataToMap");
			Common.loadPropDataToMapCache = mapObj;
			return Common.loadPropDataToMapCache;

		}
		ServerLog.debug("Returning Map From The Cache Memory in Common.getDatabaseTypeName()");
		ServerLog.debug("Existing From Common.loadPropDataToMap");
		return Common.loadPropDataToMapCache;
	}
}