package com.mq.pcrf.core;

import java.lang.reflect.UndeclaredThrowableException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import com.mq.licencing.Licence;
import com.mq.licencing.LicenceReader;
import com.mq.osslog.ServerLog;
import com.mq.pcrf.cache.BandWidthMasterCache;
import com.mq.pcrf.cache.RadCacheLoader;
import com.mq.pcrf.cache.RadDatastoreCache;
import com.mq.pcrf.cache.RadGroupCache;
import com.mq.pcrf.cache.RadRuleGrpAssoCache;
import com.mq.pcrf.cache.RadServiceCache;
import com.mq.pcrf.config.PcrfConfigFactory;
import com.mq.pcrf.jmx.AbstractServiceDaemon;



/**
 * @author naveen.beldari
 *
 */
public class Pcrfd extends AbstractServiceDaemon{
	
	private Pcrfd() {
		super("MQPcrf.Pcrfd");
	}
	
	
	
	
	@Override
	protected void onInit() {

	}
		@SuppressWarnings("unchecked")
		@Override
		protected void onStart() {
			ServerLog.debug("[INFO]   : Entered into method : onStart()  ");
			ServerLog.debug("[START]  : Checking for MQNsure license ....");
			
			
			/**
			 * ********************************
			 *  CHECKING FOR MQPCRF LICENSE
			 * ********************************
			 */
			try {
				checkLicenseStatus();
			}catch(Exception e) {
				ServerLog.error("[START]  : License information not found. ", null);
				throw new UndeclaredThrowableException(e);

			}
			try {
				String ipAddress="";
				Timestamp t= new Timestamp(new Date().getTime());
				PcrfConfigFactory.init();
				dFactory = PcrfConfigFactory.getInstance(); 
				HashMap<String, HashMap<String, String>> hmapConfigFile=dFactory.hmapConfigFile;
				if(hmapConfigFile.size()==0)
				{
					ServerLog.error("[Error]  : Pcrf  Configuration File not loaded Properly  :",null);
				}
				else
				{
					ipAddress=dFactory.getIpAddress();
					ServerLog.debug("[START]  : Pcrf service configuration loaded sucessfully.");
					ServerLog.debug("[STATS]  : Timetaken to load Pcrf service configuration :  "+(new Timestamp(new Date().getTime()).getTime()-t.getTime())+" milli seconds .");
				}
				
				
				
				/**
				 * ***********************************************
				 * LOAD DATABASE CONFIGURATION AND CONNECT SERVICE
				 * ***********************************************
				 */			
				t= new Timestamp(new Date().getTime());
				
				/*if(!DSConnectionFactory.getIsDbConnected()){
					
					//String filePath = Common.strMqPcrfHome+Common.fileSeprator+"conf"+Common.fileSeprator+"dbconfig.properties";
					String filePath = Common.strMqPcrfHome+Common.fileSeprator+"conf"+Common.fileSeprator+"MQPcrfDataSource.xml";
					Map<String, String> mapDBConfig=Common.loadXmlAttrbutesByTagNameToMap(filePath, new HashMap<String, String>(), Common.getDatabaseTypeName());
					//Map<String, String> mapDBConfig=Common.loadPropDataToMap(filePath, new HashMap<String, String>(), Common.getDatabaseTypeName());
					//DSConnectionFactory.loadDataSource(mapDBConfig);
				}*/
				
				//DSConnectionFactory1.loadDataSourceByReadingXml();
				ServerLog.debug("[STATS]  : Time taken for Datastore connection .. :  "+(new Timestamp(new Date().getTime()).getTime()-t.getTime())+" milli seconds .");
				 
				/**
				 * *****************************************
				 * START CACHING SERVICE
				 * *****************************************
				 */

				RadDatastoreCache cache = new RadDatastoreCache();   
				ServerLog.debug("[START]  : Caching service intialized sucessfully.");
				/**
				 * *****************************************
				 * LOAD DATA INTO SERVICE CACHING SYSTEM
				 * *****************************************
				 */
				
				cache.cacheData();
				ServerLog.debug("[START]  : Caching service cached data sucessfully.");
				
				
				
				System.out.println(" \n [ start ] PCRF server started successfully....		[ 0K ] ");
			}
			catch(Exception exp){
				ServerLog.error("[START]  : pcrf server not properly started .....", exp);
				throw new UndeclaredThrowableException(exp);
			}
			
		}
	
		/**
		 * This method checks the  PCRF license status 
		 */
		private void checkLicenseStatus() {
			ServerLog.debug("[INFO]   : Entered into getLicenseStatus() method ");
			int intLicExpStatus;
			LicenceReader licReader = new LicenceReader();
			Licence license = licReader.readLicense();
			if (license != null )
			{
				strClientName = license.getLicenceTo();
				intLicExpStatus = licReader.getExpiryDateStatus(license);
				if(intLicExpStatus == 0) 		// 0 --> License OK
				{
					//need to handle if intimations are to be sent at the time successful license
				}
				else if(intLicExpStatus == 1) 	// 1 --> License has expired.
				{
					//need to handle if any warnings/intimations are to be sent at the time of grace period
					ServerLog.error( "[ WARNING ]  : Your license has been expired !! ",null);
					//call alarm mail
				}
				else if(intLicExpStatus == 2)  	// 2-- License has expired and grace period also completed.
				{
					//need to handle if any warnings/intimations are to be sent at the time expired state
					ServerLog.error( "[ ERROR ]  : Licence has been expired , please renew license to countinue MQNsure service.  ",null);
					throw new IllegalStateException();
				}
			}
			else {
				//when license info not found.
				ServerLog.error("[ERROR]  : MQNSure License not found. ", null);
				throw new IllegalStateException();				
			}
			ServerLog.debug("[INFO]   : Exited from getLicenseStatus() method ");
		}
		
		
		/* 
		 * This methods is used for stopping all running services
		 */
		@Override
		protected void onStop() {
			ServerLog.debug("[INFO]   : Entered into onStop() method ");
			Timestamp stopTime= new Timestamp(new Date().getTime());
			try{
				ServerLog.debug("[STOP]   : Stopping all services...");
				/*
				 * clearing cache
				 */
				PcrfConfigFactory objPcrfConfigFactory=PcrfConfigFactory.getInstance();
				objPcrfConfigFactory.close();
				RadDatastoreCache.clearAllCache();
			}
			catch(Exception exp)
			{
				ServerLog.debug("[STOP]   : Service failed to stop datastore service.");

			}
			ServerLog.debug("[STOP]   : Services stopped sucessfully...\n");
			ServerLog.debug("[INFO]   : Exited from onStop() method ");
			ServerLog.debug("[STATS]  : Timetaken for onStop() method  :  "+(new Timestamp(new Date().getTime()).getTime()-stopTime.getTime())+"milli seconds .");
			System.out.println(" [ stop ] PCRF server stopped successfully....		[ 0K ] ");
			
		}
			
	
		
		
		
		/* 
		 * This method is used for refreshing Service cache
		 */
		protected void onRefreshService()
		{
			ServerLog.debug("[INFO]   : Entered into onRefreshService() method ");
			try
			{
				RadServiceCache radSvcCache = RadDatastoreCache.getRadSvcCache();
				radSvcCache.clearCache();
				RadCacheLoader dataLoader = new RadCacheLoader();
				dataLoader.loadRadService(radSvcCache);
				radSvcCache.clearTempCache();
			}
			catch(Exception ex)
			{
				ServerLog.error("Service details caching Failed. Error is: "+ex.getMessage(),null);
			}
			finally
			{
				ServerLog.debug("[INFO]   : Exited from onRefreshService() method ");
			}
		}
		
		
		
		/* 
		 * This method is used for refreshing the Bandwidth master cache
		 */
		protected void onRefreshBandWidth()
		{
			ServerLog.debug("[INFO]   : Entered into onRefreshBandWidth() method ");
			try
			{
				BandWidthMasterCache bandWidthMasterCache = RadDatastoreCache.getBandWidthMasterCache();
				bandWidthMasterCache.clearCache();
				RadCacheLoader dataLoader = new RadCacheLoader();
				dataLoader.loadBandWidthMaster(bandWidthMasterCache);
				bandWidthMasterCache.clearTempCache();
			}
			catch(Exception ex)
			{
				ServerLog.error("Band Width details caching Failed. Error is: "+ex.getMessage(),null);
			}
			finally
			{
				ServerLog.debug("[INFO]   : Exited from onRefreshBandWidth() method ");
			}
		}
		
		
		
		/* 
		 * this method is used for refreshing Rule cache
		 */
		protected void onRefreshRule()
		{
			ServerLog.debug("[INFO]   : Entered into onRefreshRule() method ");
			try
			{
				RadRuleGrpAssoCache radRuleGrpAssoCache = RadDatastoreCache.getRadRuleGrpAsso();
				radRuleGrpAssoCache.clearCache();
				RadCacheLoader dataLoader = new RadCacheLoader();
				dataLoader.loadRadRuleGrpAsso(radRuleGrpAssoCache);
				radRuleGrpAssoCache.clearTempCache();
			}
			catch(Exception ex)
			{
				ServerLog.error("Rule details caching Failed. Error is: "+ex.getMessage(),null);
			}
			finally
			{
				ServerLog.debug("[INFO]   : Exited from onRefreshRule() method ");
			}
		}
		/* 
		 * this method is used for refreshing Group cache
		 */
		protected  void onRefreshGroup()
		{
			ServerLog.debug("[INFO]   : Entered into onRefreshGroup() method ");
			try
			{   
				RadGroupCache radGrpCache = RadDatastoreCache.getRadGrpCache();
				radGrpCache.clearCache();
				RadCacheLoader dataLoader = new RadCacheLoader();
				dataLoader.loadRadGroup(radGrpCache);
				radGrpCache.clearTempCache();
			}
			catch(Exception ex)
			{
				ServerLog.error("Group details caching Failed. Error is: "+ex.getMessage(),null);
			}
			finally
			{
				ServerLog.debug("[INFO]   : Exited from onRefreshGroup() method ");
			}
		}
	
		@Override
		protected void onReStart() {
			ServerLog.debug("[RESTART]: Restarting all services...");
			onStop();
			onStart();
			ServerLog.debug("[RESTART]: Services restarted sucessfully...\n");	
		}


		/**
		 * Returns the single instance of the Pcrf server.
		 * 
		 */
		public static Pcrfd getInstance()
		{
			return m_singleton;
		}

		private final static Pcrfd m_singleton = new Pcrfd();
		
		public static String strClientName = null;
		private PcrfConfigFactory dFactory;
		public Object objProxyStarter;
		
		
	}

	

