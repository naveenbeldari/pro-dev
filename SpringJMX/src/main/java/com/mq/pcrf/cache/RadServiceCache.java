package com.mq.pcrf.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.mq.db.entity.pcrf.RadServiceAttribute;
import com.mq.osslog.ServerLog;




/**
 * @author naveen.beldari
 *
 */
public class RadServiceCache   implements Serializable{ 

	private static final long serialVersionUID = 1L;
	private static boolean isRadSrvcCacheRefreshed=true;
	HashMap<Long, HashMap> radServiceMap=new HashMap<Long, HashMap>();
	HashMap<Long, HashMap> tempRadServiceMap=new HashMap<Long, HashMap>();

	public RadServiceCache(){
	}


	/**
	 * Stores a Service with its Map in to Cache . 
	 * @param serviceId
	 * @param attributeValue
	 */
	public void addRadService(Long serviceId, HashMap<String,List<RadServiceAttribute>> attributeValueMap) {
		try {
			radServiceMap.put((new Long(serviceId)), attributeValueMap);
		} catch (Exception e) {
			ServerLog.error("Exception at addRadService() method  ", e);
		}
	}

	/**
	 * Retrieves a Service Map from Cache through svcId.
	 * @param svcId
	 * @return
	 */
	public HashMap getMapBySvcId(long svcId) {
		if (isRadSrvcCacheRefreshed)
			return radServiceMap.get(new Long(svcId));
		return tempRadServiceMap.get(new Long(svcId));

	}
	/**
	 * keeps the back-up of existing service cache,
	 * disable the flag and clear the existing service cache
	 */
	public void clearCache() {
		tempRadServiceMap.putAll(radServiceMap);
		isRadSrvcCacheRefreshed=false;
		radServiceMap.clear();
	}
	/**
	 * This method enable the flag and then clear the temporary  service cache
	 */
	public void clearTempCache() {
		isRadSrvcCacheRefreshed=true;
		tempRadServiceMap.clear();
	}

}
