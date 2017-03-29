package com.mq.pcrf.db.connection;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import com.mq.common.Common;
import com.mq.osslog.ServerLog;

/**
 * @author devendra.miyyapuram
 */
/**
 * DSConnectionFactory class is connnection pool factory class For giving the
 * DataSource implementation class object called ComboPooledDataSource it is
 * from c3p0 connnection pool library
 */
public class DSConnectionFactory {
	private static boolean dataSourceStatus = false;
	
	private static ComboPooledDataSource ds;

	private DSConnectionFactory() {
	}

	/*static {

		//loadDataSourceByReadingXml();
		loadDataSourceByReadingProp();
	}*/

	/**
	 * This method will load the DataSource by reading database configuration
	 * details from xml file. Tis metod will called by startup class in the same
	 * class
	 */
	public static void loadDataSource(Map<String, String> mapPcrfDataSourceConfig) {
		try {

			ServerLog
					.debug("Entered Into DSConnectionFactory.loadDataSourceByReadingXml");
			
			if (dataSourceStatus == false) {

				ds = new ComboPooledDataSource();
				ds.setUser(mapPcrfDataSourceConfig.get("user"));
				ds.setPassword(mapPcrfDataSourceConfig.get("password"));
				ds.setJdbcUrl(mapPcrfDataSourceConfig.get("jdbcUrl"));
				ds.setMinPoolSize(Integer.parseInt(mapPcrfDataSourceConfig
						.get("minConnections")));
				ds.setMaxPoolSize(Integer.parseInt(mapPcrfDataSourceConfig
						.get("maxConnections")));
				ds.setDriverClass(mapPcrfDataSourceConfig.get("driverClass"));
				ds.setAcquireIncrement(Integer.parseInt(mapPcrfDataSourceConfig
						.get("acquireIncrement")));
				ds.setIdleConnectionTestPeriod(Integer
						.parseInt(mapPcrfDataSourceConfig
								.get("idleConnectionTestPeriod")));
				ds.setTestConnectionOnCheckin(Boolean
						.parseBoolean(mapPcrfDataSourceConfig
								.get("testConnectionOnCheckin")));
				ds.setAutomaticTestTable(mapPcrfDataSourceConfig
						.get("automaticTestTable"));

				dataSourceStatus = true;
				ServerLog.debug("MQPcrf DataSource is Loaded Sucessfully ");
				ServerLog
						.debug("Exited From DSConnectionFactory.loadDataSourceByReadingXml");

			}
		} catch (Exception ex) {
			ServerLog.error(
					"Failed to execute the DSConnectionFactory.loadDataSource",
					ex);
		}
	}

	/**
	 * This method will give DataSource object for first time calling from the
	 * next time it gives only reference.
	 */
	public static ComboPooledDataSource getDataSource() {
		ServerLog.debug("Entered Into DSConnectionFactory.getDataSource");
		if (ds == null) {
			ServerLog.debug("Failed To Returning DataSource");
		}
		ServerLog.debug("Exiting From DSConnectionFactory.getDataSource");
		return ds;
	}

	/**
	 * This method will close all DataBase Connections.
	 */
	public static void closeAllDataBaseConnections() {

		try {
			if (dataSourceStatus == true) {
				ds.close();
				DataSources.destroy(ds);
				ds = null;
				dataSourceStatus = false;
				ServerLog
						.debug("All MQPcrf DataBase Connections are Closed Sucessfully");
			}
		} catch (Exception e) {
			ServerLog
					.error("Failed To execute the DSConnectionFactory.closeAllDataBaseConnections",
							e);
		}

	}
	
	public static boolean getIsDbConnected(){
		return dataSourceStatus;
	}
}