package com.mq.pcrf.cache;

import java.io.Serializable;
import java.util.HashMap;
import com.mq.db.entity.common.BandWidthMaster;
import com.mq.osslog.ServerLog;


/**
 * @author naveen.beldari
 *
 */
public class BandWidthMasterCache  implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static boolean isBandWidthCacheRefreshed=true;
	HashMap<String,BandWidthMaster> bandWidthMasterMap=new HashMap<String,BandWidthMaster>();
	HashMap<String,BandWidthMaster> tempBandWidthMasterMap=new HashMap<String,BandWidthMaster>();
	
	public BandWidthMasterCache()
	{}
		
	
	/**
	 * This method used to add the Bandwidth master object into the map.
	 * @param objBWM
	 */
	public void addBandWidthQOS(BandWidthMaster objBWM)
	{
		try {
			bandWidthMasterMap.put(objBWM.getQoS(), objBWM);
		} catch (Exception e) {
			ServerLog.error("Exception at addBandWidthQOS() method ", e);
		}
	}

	/**
	 * @param strQOSCode
	 * @return :Bandwidth master cache if isBandWidthCacheRefreshed is enabled
	 *         i.e refreshed cache else return temporary bandwidth master cache
	 */
	public BandWidthMaster getBandWidthMasterByQOSCode(String strQOSCode) {
		if (isBandWidthCacheRefreshed)
			return bandWidthMasterMap.get(strQOSCode);
		return tempBandWidthMasterMap.get(strQOSCode);
	}
	/**
	 * This method first keeps the back-up of existing bandwidth master cache,
	 * disable the flag and then clear the existing  bandwidth master cache.
	 */
	public void clearCache() {
		tempBandWidthMasterMap.putAll(bandWidthMasterMap);
		isBandWidthCacheRefreshed = false;
		bandWidthMasterMap.clear();

	}
	/**
	 * This method enabled the flag and then clear the temporary bandwidth master cache
	 */
	public void clearTempCache() {
		isBandWidthCacheRefreshed = true;
		tempBandWidthMasterMap.clear();

	}
}
