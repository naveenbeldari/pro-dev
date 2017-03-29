package com.mq.pcrf.cache;

import java.io.Serializable;
import java.util.HashMap;

import com.mq.db.entity.common.RadGroup;
import com.mq.osslog.ServerLog;


/**
 * @author naveen.beldari
 *
 */
public class RadGroupCache implements Serializable { 
	

	private static final long serialVersionUID = 265723482504642547L;
	private static boolean isRadGrpCacheRefreshed=true;

	/**
	 * 
	 * @param Name
	 */
	public RadGroupCache(){
	}

    HashMap <Long,Object> radGroupMap = new HashMap<Long, Object>();
	HashMap <String,Object> radGroupMapByCode=new HashMap<String, Object>();
    HashMap<Long, Object> tempRadGroupMap=new HashMap<Long, Object>() ;
    HashMap<String, Object> tempRadGrpMapByCode=new HashMap<String, Object>() ;
    
    /** 
	 * @param groupId
	 * @return: Group object from original map if caching is completed else 
	 * from temporary map if caching is under processing.
	 */
	
	public RadGroup getGroupById(long groupId){
		if (isRadGrpCacheRefreshed) 
			return (RadGroup) radGroupMap.get((new Long(groupId)));
		return (RadGroup) tempRadGroupMap.get((new Long(groupId)));
	}
	
	/**
	 * @param groupCode
	 * @return:Original Group cache  if caching is completed else 
	 *  temporary cache if caching is under processing.
	 */
	public RadGroup getGroupByCode(String groupCode) {
		if (isRadGrpCacheRefreshed)
			return (RadGroup) radGroupMapByCode.get(groupCode);
		return (RadGroup) tempRadGrpMapByCode.get(groupCode);
	}

	/**
	 * Stores a Group into Cache . 
	 * @param radGroup
	 */
	public void addRadGroup(RadGroup radGroup) {
			try 
			{
				radGroupMap.put(new Long(radGroup.getGroupId()),radGroup);
				radGroupMapByCode.put(radGroup.getGroupCode(), radGroup);
				
			}
		catch (Exception e) 
		{				
			ServerLog.error(" Exception at addRadGroup() ", e);
		}
	}
	/**
	 * This method first keeps the back-up of existing group cache,
	 * disable the flag and then clear the existing cache.
	 */
	public void clearCache() {
		tempRadGroupMap.putAll(radGroupMap);
		tempRadGrpMapByCode.putAll(radGroupMapByCode);
		isRadGrpCacheRefreshed=false;
		radGroupMap.clear();
		radGroupMapByCode.clear();
		
	}
	/**
	 * This method enable the flag and then clear the temporary  group cache
	 */
	public void clearTempCache() {
		isRadGrpCacheRefreshed=true;
		tempRadGroupMap.clear();
		tempRadGrpMapByCode.clear();
		
	}

	}
