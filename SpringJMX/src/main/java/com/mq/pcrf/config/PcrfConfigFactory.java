package com.mq.pcrf.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mq.osslog.ServerLog;

public class PcrfConfigFactory {
	

	private static boolean m_loaded = false;
	public static PcrfConfigFactory m_singleton = null;
	public HashMap<String, HashMap<String, String>> hmapConfigFile		                        = new HashMap<String, HashMap<String, String>>();
	public HashMap<String, HashMap<String, String>> hmapDataSoureFile	                        = new HashMap<String, HashMap<String, String>>();
	public HashMap<String, HashMap<String, String>> hmapHssFile			                = new HashMap<String, HashMap<String, String>>();
	public HashMap<String, HashMap<String, String>> hmapWebServiceConfiguration			= new HashMap<String, HashMap<String, String>>();
	public HashMap<String, HashMap<String,HashMap<String,HashMap<String,String>>>> hmapCommandAttribute			    = new HashMap<String, HashMap<String,HashMap<String,HashMap<String,String>>>>();
	public HashMap<String, HashMap<String, String>> hmapWebServiceMsg		            = new HashMap<String, HashMap<String, String>>();
	private static String PCRF_CONFIG_FILE_NAME		                                = "MQPcrf-Configuration.xml";
	private static String PCRF_DATASOURCE_FILE_NAME	                                = "MQPcrfDataSource.xml";
	private static String PCRF_WEBSERVICE_CONFIGURATION                               = "webserviceconfiguration.xml";
	private static String PCRF_Command_Attribute_Configuration	                    = "Command_Attribute_Configuration.xml";
	private static String PCRF_WebServiceMsg                                          = "WebServiceMsg.xml";
	public String ipAddress ;
	public String dataBaseType;
	
	public PcrfConfigFactory()
	{}
	
	public PcrfConfigFactory(File configFile,File dataSourceFile,File commandAttributeFile,File webserviceconfiguration,File WebServiceMsg ){
		
		readXMLFile(configFile);
		readXMLFile(dataSourceFile);
		readXMLFile(commandAttributeFile);
		readXMLFile(webserviceconfiguration);
		readXMLFile(WebServiceMsg);
	}
	
	public static synchronized void init(){
		if(m_loaded){
			return;
		}
		try {
			
			File configFile                         = getFile(PCRF_CONFIG_FILE_NAME);
			File dataSourceFile                     = getFile(PCRF_DATASOURCE_FILE_NAME);
			File commandAttributeFile               = getFile(PCRF_Command_Attribute_Configuration);
			File webserviceconfiguration            = getFile(PCRF_WEBSERVICE_CONFIGURATION);
			File WebServiceMsg                      = getFile(PCRF_WebServiceMsg);
			m_singleton = new PcrfConfigFactory(configFile,dataSourceFile,commandAttributeFile,webserviceconfiguration,WebServiceMsg);
			m_loaded = true;
		} catch (IOException e) {
			ServerLog.error("Exception : ", e);
		}
		
	}

	public static final File getFile(String fileName) throws IOException {

		String home = getHome();
		File homeDir = new File(home);
		if(!homeDir.exists()){
			ServerLog.error("Home directory doesnot exist : "+homeDir, null);
			throw new FileNotFoundException("MQPcrf home directory \""+home+"\" does not exist");
		}
		File  file = new File(home+File.separator+"conf"+File.separator+fileName);
		if(!file.exists()){
			throw new FileNotFoundException("The requested file '"+fileName+"' could not be found at "+file.getAbsolutePath());
		}
		return file;
	 }
	 
	/**
	 * @return:Home directory of PCRF Server
	 */
	public static final String getHome(){
		String home = System.getProperty("MQPcrf.home");
		if (home.endsWith("/") || home.endsWith(File.separator))
			home = home.substring(0, home.length() - 1);
		return home;
	}

	public static synchronized PcrfConfigFactory getInstance() {
		if (!m_loaded)
			throw new IllegalStateException("The factory has not been initialized");
		return m_singleton;
	}
	
	private void readXMLFile(File file) 
	{
		ServerLog.debug("[INFO] Entered into method : readXMLFile()");
		try
		{
			String tagName = "";
			String[] strArr = file.getAbsolutePath().split("\\\\");
			String strName = strArr[strArr.length-1];
			HashMap<String, HashMap<String, String>> mapXMLFile = new HashMap<String, HashMap<String, String>>();
			if(strName.contains("MQPcrf-Configuration"))
			tagName = "PcrfdConfiguration";
			else if(strName.contains("datasource"))
			tagName = "DataSource-Details";
			else if(strName.contains("Command_Attribute"))
			tagName = "Command_Attribute_Configuration";
			else if(strName.contains("webserviceconfiguration"))
			tagName = "Configuration";
			else if(strName.contains("WebServiceMsg"))
			tagName = "WebServiceMsg";
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			if(tagName.equalsIgnoreCase("Command_Attribute_Configuration"))
			{
				HashMap<String,String> 					mapAttributes 			 = null;
				HashMap<String,HashMap<String,String>>	mapRequestType 			 = null;
				HashMap<String, HashMap<String,HashMap<String,String>>> mapNAS	 = null;
				if(file.exists())
				{
					doc.getDocumentElement().normalize();		
					/**
					 * Get the Root Element i.e <Attribute_Configuration>
					 **/
					Element root =doc.getDocumentElement();
					/**
					 * Get the Child Element i.e <Radius> or <Diameter>
					 **/
					NodeList nodeLst = root.getChildNodes();
					for(int i=0;i<nodeLst.getLength();i++)
					{	
						Node serverNode=nodeLst.item(i);
						if (serverNode.getNodeType() == Node.ELEMENT_NODE && serverNode.getNodeType() != Node.TEXT_NODE) 
						{ 
							mapNAS = new HashMap<String, HashMap<String,HashMap<String,String>>>();

							NodeList nasNodeLst=serverNode.getChildNodes();
							for(int j=0;j<nasNodeLst.getLength();j++)
							{
								Node nasNode = nasNodeLst.item(j);
								if (nasNode.getNodeType() == Node.ELEMENT_NODE && nasNode.getNodeType() != Node.TEXT_NODE) 
								{ 
									/**
									 * Getting value of the <NAS> element like for <NAS Name="Mikrotik"> value is "Mikrotik"
									 **/
									String strNas = nasNode.getAttributes().item(0).toString();
									String[] strArrNas = strNas.split("=");
									String[] strNasName = strArrNas[1].split("\"");
									Element element =(Element)nasNode;
									/**
									 * Getting child of the <NAS> i.e  <POD>, <COA> or <ASR>, <RAR>
									 **/						
									NodeList nas_child = element.getChildNodes();
									mapRequestType = new HashMap<String,HashMap<String,String>>();
									for(int k=0;k<nas_child.getLength();k++)
									{
										Node requestType = nas_child.item(k);
										if (requestType.getNodeType() == Node.ELEMENT_NODE && requestType.getNodeType() != Node.TEXT_NODE) 
										{
											/**
											 * Getting child of the <POD>, <COA> or <ASR>, <RAR>
											 **/								
											NodeList podORcod_child = requestType.getChildNodes();
											mapAttributes = new HashMap<String,String>();
											for(int l=0;l<podORcod_child.getLength();l++)
											{
												Node attribute = podORcod_child.item(l);
												if(attribute.getNodeType() != Node.TEXT_NODE)
												{ 
													/**
													 * Getting value of the <POD>, <COA> or <ASR>, <RAR> attributes
													 **/	
													String strAttributesName = attribute.getAttributes().item(0).toString();
													String[] strArrAttr = strAttributesName.split("=");
													String[] strAttrName = strArrAttr[1].split("\"");
													String strAttributesRequired = attribute.getAttributes().item(1).toString();
													String[] strArrAttrReq = strAttributesRequired.split("=");
													String[] strAttrReq = strArrAttrReq[1].split("\"");
													mapAttributes.put(strAttrName[1], strAttrReq[1]);
												}
											}
											mapRequestType.put(requestType.getNodeName().toString(), mapAttributes);						 
										}
									}
									mapNAS.put(strNasName[1], mapRequestType);
								}
								hmapCommandAttribute.put(serverNode.getNodeName(),mapNAS);
							}
						}
					}

				}
				else
				ServerLog.error("Command_Attribute_Configuration. Xml does not exist at given path = "+file.getAbsolutePath(), null);			
			}
			else if(tagName.equalsIgnoreCase("WebServiceMsg"))
			{
				  doc.getDocumentElement().normalize();
				  NodeList nodeLstMsges = doc.getElementsByTagName("error-messages");
				  for(int i=0;i<nodeLstMsges.getLength();i++)
				  {
					 HashMap<String,String> hashmapMessages = new HashMap();	
					 Element element = (Element) nodeLstMsges.item(i);
					 String partyName = element.getAttribute("name");								 
					 NodeList nodeLstMsg = doc.getElementsByTagName("message");			
					 for (int s = 0; s < nodeLstMsg.getLength(); s++)
					 {
						 Node fstNode = nodeLstMsg.item(s);   
						 if (fstNode.getNodeType() == Node.ELEMENT_NODE) 
						 {		  
							 Element fstElmnt = (Element) fstNode;
							 NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("message-id");
							 Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
							 NodeList fstNm = fstNmElmnt.getChildNodes(); 
							 String strMsgId = (fstNm.item(0)).getNodeValue();

							 NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("message-text");
							 Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
							 NodeList lstNm = lstNmElmnt.getChildNodes();     
							 String strMsgText = (lstNm.item(0)).getNodeValue();	     
							 hashmapMessages.put(strMsgId, strMsgText);
						 }
					 }
					 hmapWebServiceMsg.put(partyName,hashmapMessages);
				  }
			}
			else
			{
				NodeList nodeLst = doc.getElementsByTagName(tagName);
				for(int i=0;i<nodeLst.getLength();i++)
				{
					Node fstNode = nodeLst.item(i);
					if (fstNode.getNodeType() == Node.ELEMENT_NODE) 
					{
						Element fstElmnt = (Element) fstNode;

						if(fstElmnt.getAttribute("ipAddress") != null && !fstElmnt.getAttribute("ipAddress").equals("")){
							/**
							 * this value taking from pcrf configuration file
							 */
							this.ipAddress = fstElmnt.getAttribute("ipAddress");
						}
						if(fstElmnt.getAttribute("tgpp-network") != null && !fstElmnt.getAttribute("tgpp-network").equals("")){
							/**
							 * this value taking from radius configuration file
							 */
							//this.isTGPPNetworkName = fstElmnt.getAttribute("tgpp-network");
						}
						if(fstElmnt.getAttribute("dataBaseType") != null && !fstElmnt.getAttribute("dataBaseType").equals("")){
							/**
							 * this value taking from pcrf configuration file
							 */
							this.dataBaseType = fstElmnt.getAttribute("dataBaseType");
						}
						NodeList nodeList = fstElmnt.getElementsByTagNameNS("*", "*");
						for (int x = 0; x < nodeList.getLength(); x++)
						{
							Node childnode = nodeList.item(x);
							HashMap hashmapConfDetails = new HashMap();
							mapXMLFile.put(childnode.getNodeName(), hashmapConfDetails);
							for (int j = 0; j < childnode.getAttributes().getLength(); j++)
							{
								Node ndAttr = childnode.getAttributes().item(j);
								String strAttrName = ndAttr.getNodeName();
								String strAttrVal = ndAttr.getNodeValue();
								hashmapConfDetails.put(strAttrName, strAttrVal);
							}
						}
						
					}
				}
			}
			if(tagName.equalsIgnoreCase("PcrfdConfiguration"))
			{
				hmapConfigFile = mapXMLFile;
			}
			else if(tagName.equalsIgnoreCase("datasource-configuration"))
			{
				hmapDataSoureFile = mapXMLFile;
			}
			else if(tagName.equalsIgnoreCase("HSSConfiguration"))
			{
				hmapHssFile = mapXMLFile;
			}
			else if(tagName.equalsIgnoreCase("Configuration"))
			{
				hmapWebServiceConfiguration = mapXMLFile;
			}
		}catch (Exception e) {
			ServerLog.error("Exception in method : readXML() ", e);
		}finally{
			ServerLog.debug("[INFO] Exited  from method : readXML() ");
		}
	}
	public  String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipaddress)
	{
		ipAddress = ipaddress ;
	}
	/*public  String getTGPPNetwork()
	{
		return isTGPPNetworkName;
	}*/
	/*public void setTGPPNetwork(String network)
	{
		 isTGPPNetworkName = network ;
	}*/
	public  String getDataBaseType(){
		return dataBaseType;
	}
	public void setDataBaseType(String dataBaseType)
	{
		this.dataBaseType = dataBaseType;
	}
	public void close()
	{
		m_loaded = false;
		m_singleton = null;
		hmapConfigFile = null;
		hmapDataSoureFile = null;
		hmapHssFile = null;
		hmapCommandAttribute=null;
		hmapWebServiceConfiguration=null;
		hmapWebServiceMsg=null;
	}




}
