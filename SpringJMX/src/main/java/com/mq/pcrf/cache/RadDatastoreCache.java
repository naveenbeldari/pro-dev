package com.mq.pcrf.cache;

import java.sql.Timestamp;
import java.util.Date;

import com.mq.osslog.ServerLog;
import com.mq.pcrf.config.PcrfConfigFactory;
import com.mq.pcrf.db.dao.PCRFDataHandler;


/**
 * @author naveen.beldari
 *
 */

public class RadDatastoreCache {
	
	private static RadCacheLoader radCacheLoader;


	public void setRadCacheLoader(RadCacheLoader radCacheLoader) {
		this.radCacheLoader = radCacheLoader;
	}

	public RadDatastoreCache(){
		initAllCaches();
	}

	/**
	 * initializes all cache
	 */
	private void initAllCaches() {		
		radGrpCache = new RadGroupCache();
		radSvcCache = new RadServiceCache();	
		radRuleGrpAssoCache = new RadRuleGrpAssoCache();
		bandWidthMasterCache = new BandWidthMasterCache();
	}	

	/**
	 * Caching the Group, Service, Rule and Bandwidth master
	 */
	public void cacheData(){
		try
		{
			ServerLog.debug("[INFO] Entered into  method  cacheData()  ");
			String dataBaseType=PcrfConfigFactory.getInstance().getDataBaseType().toLowerCase();
			PCRFDataHandler.loadQueries(dataBaseType);
			//RadCacheLoader dataLoader = new RadCacheLoader();
			Timestamp ts1 = new Timestamp(new Date().getTime());
			radCacheLoader.loadRadGroup(radGrpCache);
			ServerLog.debug("Time taken to get all radgroups : " +(new Timestamp(new Date().getTime()).getTime()-ts1.getTime())+" millisecs");
			Timestamp ts = new Timestamp(new Date().getTime());
			radCacheLoader.loadRadService(radSvcCache);	
			ServerLog.error("Time taken to get all radservice : " +(new Timestamp(new Date().getTime()).getTime()-ts.getTime())+" millisecs",null);
			radCacheLoader.loadRadRuleGrpAsso(radRuleGrpAssoCache);
			Timestamp ts3 = new Timestamp(new Date().getTime());
			radCacheLoader.loadBandWidthMaster(bandWidthMasterCache);
			ServerLog.debug("Time taken to get all BandWidthMaster : " +(new Timestamp(new Date().getTime()).getTime()-ts3.getTime())+" millisecs");
		}
		catch(Exception exp)
		{
			ServerLog.error("Exception in cacheData method .",exp);
		}
		ServerLog.debug("[INFO] Exited from  method  cacheData()  ");
	}

	
	/**
	 * @return 
	 */
	public static RadGroupCache getRadGrpCache() {
			return radGrpCache;
		}

	/**
	 * 
	 * @return
	 */
	public static RadServiceCache getRadSvcCache() {
		return radSvcCache;
	}
	/**
	 * 
	 * @return
	 */
	public static RadRuleGrpAssoCache getRadRuleGrpAsso() {
		return radRuleGrpAssoCache;
	}

	/**
	 * 
	 * @return
	 */
	public static BandWidthMasterCache getBandWidthMasterCache() {
		return bandWidthMasterCache;
	}

	
	
	
	

	/**
	 * Clearing all cache while stopping the PCRF Server.
	 */
	public static  void clearAllCache() {
		radGrpCache.clearCache();
		radSvcCache.clearCache();
		radRuleGrpAssoCache.clearCache();
		bandWidthMasterCache.clearCache();
		ServerLog.debug("[INFO] Exited from  method  clearAllCache()  ");
	}
	
	private static RadGroupCache radGrpCache;
	private static RadServiceCache radSvcCache;
	private static RadRuleGrpAssoCache radRuleGrpAssoCache;
	private static BandWidthMasterCache bandWidthMasterCache;

}
