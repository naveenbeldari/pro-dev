package com.mq.pcrf.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import com.mq.db.entity.policyengine.RuleGroupAssociation;
import com.mq.osslog.ServerLog;


/**
 * @author naveen.beldari
 *
 */
public class RadRuleGrpAssoCache  implements Serializable{

	private static final long serialVersionUID = -2776171262940847278L;
	private static boolean isRuleGrpCacheRefreshed=true;
	HashMap<Long,List> ruleGroupMap=new HashMap<Long, List>(); 
	HashMap<Long,List> tempRuleGroupMap=new HashMap<Long, List>(); 

	public RadRuleGrpAssoCache()
	{
	}

		/**
	 * Retrieves a Rule-Group association List of specific rule from Cache through groupId on rule priority based.
	 * @param groupId
	 * @return :Original Rule-Group association cache if caching is completed else 
	 * temporary Rule-Group association cache if caching is under processing.
	 */
	@SuppressWarnings("unchecked")
	public List<RuleGroupAssociation> getRuleByGrpId(long grpId) {
		if (isRuleGrpCacheRefreshed)
			return ruleGroupMap.get(new Long(grpId));
		return tempRuleGroupMap.get(new Long(grpId));

	}
	
	
	
	/**
	 * Stores a Rule-Group association into Cache . 
	 * @param lngGroupId
	 * @param lstRuleGrpAsso
	 */
	public void addRuleGrpAssociation(long lngGroupId,List<RuleGroupAssociation> lstRuleGrpAsso)
	{
		try
		{
			ruleGroupMap.put(new Long(lngGroupId), lstRuleGrpAsso);
					
		} catch (Exception e) {				
				ServerLog.error(" Exception at addRuleGrpAssociation() method ", e);
		}
	}
	
	/**
	 * keeps back-up of existing Rule-Group association cache,disable the flag 
	 * and then clear the existing Rule-Group association cache
	 */
	public void clearCache() {
		tempRuleGroupMap.putAll(ruleGroupMap);
		isRuleGrpCacheRefreshed = false;

		ruleGroupMap.clear();

	}
	
	/**
	 * Enable the flag and clear the temporary Rule-Group association cache,
	 * once caching is completed
	 */
	public void clearTempCache() {
		isRuleGrpCacheRefreshed = true;

		tempRuleGroupMap.clear();

	}
}
