package com.mq.pcrf.cache;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.mq.db.entity.common.BandWidthMaster;
import com.mq.db.entity.common.RadGroup;
import com.mq.db.entity.pcrf.RadServiceAttribute;
import com.mq.db.entity.policyengine.RuleGroupAssociation;
import com.mq.osslog.ServerLog;
import com.mq.pcrf.db.dao.PCRFDataHandler;

/**
 * @author naveen.beldari
 *
 */
public class RadCacheLoader {

	//private  static String strMQPcrfHome								= System.getProperty("mqpcrf.home");
	//private  static String strFileSeperaor								= System.getProperty("file.separator");
	
	private PCRFDataHandler pcrfDataHandler;

	public void setPcrfDataHandler(PCRFDataHandler pcrfDataHandler) {
	this.pcrfDataHandler = pcrfDataHandler;
}
	

	/**
	 * Caching all Radius Services .
	 * @param radSvcCache
	 */
	public void loadRadService(RadServiceCache radSvcCache) 
	{
		ServerLog.debug("[INFO] Entered into  method  loadRadService()  ");
		List<RadServiceAttribute> services = new ArrayList<RadServiceAttribute>();
		try
		{
			services = pcrfDataHandler.getAllRadServices();
			ServerLog.error("Service size = " +services.size(),null);
			
			int scount=0;
	        int attrSize = services.size();
//	        System.out.println(attrSize);
	        List<RadServiceAttribute> arrtrList = new ArrayList<RadServiceAttribute>();        
	        HashMap<String,List<RadServiceAttribute>> vendorMap = new HashMap<String,List<RadServiceAttribute>>();
	        Map<Long,Map<String,List<RadServiceAttribute>>>  serviceMap = new HashMap<Long,Map<String,List<RadServiceAttribute>>>();
	      /*  Map<String,List<RadServiceAttribute>> serviceMap1=new HashMap<String,List<RadServiceAttribute>>();
	        List<RadServiceAttribute> arrtrList1 = new ArrayList<RadServiceAttribute>();       */
	        
	        Long attrService = 0L;
	        String vendorName=null;
	        Long tmpService = 0L;
	        String tmpVendorName=null;
	        
	        for(RadServiceAttribute radAttribute:services){
	        	
				attrService = radAttribute.getServiceId();
				vendorName = radAttribute.getVendor(); 

				if( scount==0 ){
					tmpService = attrService;
					tmpVendorName = vendorName;
				}
				
				scount++;
				if(tmpService.equals(attrService)){
					if(tmpVendorName.equals(vendorName)){
					   arrtrList.add(radAttribute);
					}
					else{
						if(arrtrList.size()>0){
							vendorMap.put(tmpVendorName, arrtrList);
						}
						arrtrList = new ArrayList<RadServiceAttribute>();
						arrtrList.add(radAttribute);
						tmpVendorName = vendorName;
					}
				}else{				
					if(arrtrList.size()>0){
						vendorMap.put(tmpVendorName, arrtrList);
					}				
					serviceMap.put(tmpService, vendorMap);
					radSvcCache.addRadService(tmpService ,vendorMap);
					
					
					arrtrList = new ArrayList<RadServiceAttribute>();
					vendorMap = new HashMap<String,List<RadServiceAttribute>>();
					
					tmpService = attrService;
					tmpVendorName = vendorName;				
					arrtrList.add(radAttribute);
				}
			
				if(attrSize==scount){
					//System.out.println(" Attr Size : " + attrSize + " List Size : " + scount);
					vendorMap.put(tmpVendorName, arrtrList);
					serviceMap.put(tmpService, vendorMap);
					radSvcCache.addRadService(tmpService ,vendorMap);
					
					
				}
			}

	       /* Set keySet=serviceMap.keySet();
	        Iterator it1=keySet.iterator();
	        while (it1.hasNext()) {
	        	
	        	serviceMap1=serviceMap.get(it1.next());
	        	
	        	Set keySet1=serviceMap1.keySet();
	        	Iterator it2=keySet1.iterator();
	        	HashMap mapAVP = new HashMap();
	        	 
	        	 while (it2.hasNext()) {
	        		String vendor = (String) it2.next();
	        		 arrtrList1=(List<RadServiceAttribute>) serviceMap1.get(vendor);
	        		 mapAVP.put(vendor, arrtrList1);
	        		 for (RadServiceAttribute radServiceAttribute : arrtrList1) {
		        		 //radSvcCache.addRadService(radServiceAttribute.getServiceId(), mapAVP);
		        		 
	        			 ServerLog.debug("******************************************************************************************** ");
	        			 ServerLog.debug("radServiceAttribute.getServiceAttributeId " +radServiceAttribute.getServiceAttributeId());
	        			 ServerLog.debug("radServiceAttribute.getServiceId " +radServiceAttribute.getServiceId());
	        			 ServerLog.debug("radServiceAttribute.getAttributeCode " +radServiceAttribute.getAttributeCode());
	        			 ServerLog.debug("radServiceAttribute.getAttributeValue " +radServiceAttribute.getAttributeValue());
	        			 ServerLog.debug("radServiceAttribute.getVendor " +radServiceAttribute.getVendor());
	        			 ServerLog.debug("******************************************************************************************** ");
		        		 System.out.println("radSvcCache.radServiceMap. values " +radSvcCache.radServiceMap.get(radServiceAttribute.getServiceId()));
		        		
		        			
							
						}
	        		 
	        	
					
				}
				
			}
*/	        
	   /*	Set<Long> keySet2=radSvcCache.radServiceMap.keySet();
 		Iterator<Long> it3=keySet2.iterator();
	    Map<String,List<RadServiceAttribute>> serviceMap5=new HashMap<String,List<RadServiceAttribute>>();
 		while (it3.hasNext()) {
 			Long key=it3.next();
 			serviceMap5 = radSvcCache.radServiceMap.get(key);
 			Set<String> key4=serviceMap5.keySet();
 			Iterator<String> it4=key4.iterator();
 			List<RadServiceAttribute> arrtrList3 = new ArrayList<RadServiceAttribute>();
 			while (it4.hasNext()) {
 				
 				String vend =  it4.next();
// 				 System.out.println(" vend : "+vend);
 				 //System.out.println(" value: "+temp.get(vend));
 				 
 				 arrtrList3 = serviceMap5.get(vend);
// 				 System.out.println("arrtrList3 " +arrtrList3.size());
 				 for (RadServiceAttribute radServiceAttribute2 : arrtrList3) {
 					 
 					 	 ServerLog.debug("******************************************************************************************** ");
	        			 ServerLog.debug("radServiceAttribute.getServiceAttributeId " +radServiceAttribute2.getServiceAttributeId());
	        			 ServerLog.debug("radServiceAttribute.getServiceId " +radServiceAttribute2.getServiceId());
	        			 ServerLog.debug("radServiceAttribute.getAttributeCode " +radServiceAttribute2.getAttributeCode());
	        			 ServerLog.debug("radServiceAttribute.getAttributeValue " +radServiceAttribute2.getAttributeValue());
	        			 ServerLog.debug("radServiceAttribute.getVendor " +radServiceAttribute2.getVendor());
	        			 ServerLog.debug("******************************************************************************************** ");
						
					}
					
				}
				
		 }
	       */
			
			/*if(services != null)
			{
				List<RadServiceAttribute> attributeValueList=null;
				Iterator it = services.iterator();
				while(it.hasNext())
				{
					Long serviceid= ((RadService)it.next()).getServiceId();
					HashMap mapAVP = new HashMap();
					try
					{
						ArrayList lstVendor = pcrfDataHandler.getAllVendorListBySvcId(serviceid);
						if(lstVendor != null)
						{
							Iterator itr = lstVendor.iterator();
							while(itr.hasNext())
							{
								String strVendor = (String)itr.next();
								attributeValueList = pcrfDataHandler.getAttributesListBySvcIdByVendor(serviceid,strVendor);
								if(attributeValueList != null)
									mapAVP.put(strVendor,attributeValueList);
							}
						}
					}
					catch (SQLException e)
					{
						ServerLog.error("Exception in loadRadService method .",e);
					}
					catch ( Exception e)
					{
						ServerLog.error("Exception in loadRadService method .",e);
					}
					radSvcCache.addRadService(serviceid, mapAVP);
				}
			}*/
		}
		
		catch ( Exception e)
		{
			ServerLog.error("Exception in loadRadService method .",e);
		}
		finally
		{
			services.clear();
			ServerLog.debug("[INFO] Exited from  method  loadRadService()  ");
		}
	}


	/**
	 * Caching all Radius Groups .
	 * @param radGrpCache
	 */

	public void loadRadGroup(RadGroupCache radGrpCache) 
	{
		ServerLog.debug("[INFO] Entered into  method  loadRadGroup()  ");
		List<RadGroup> groups =new ArrayList<RadGroup>();
		try
		{
			
			groups = pcrfDataHandler.getAllRadGroups();
			
			ServerLog.debug("Group size = " +groups.size());
			if(groups != null)
			{
				Iterator<RadGroup> it = groups.iterator();
				while(it.hasNext())
				{
					radGrpCache.addRadGroup(it.next());

					
				}
			}
		}
		catch (Exception e)
		{
			ServerLog.error("Exception in loadRadGroup method .",e);
		}
		finally
		{
			groups.clear();
			ServerLog.debug("[INFO] Exited from  method  loadRadGroup()  ");
		}
	}
	/**
	 * Caching all Radius Rule-group Associations .
	 * @param radRuleGrpAssoCache
	 */
	public void loadRadRuleGrpAsso(RadRuleGrpAssoCache radRuleGrpAssoCache) 
	{
		ServerLog.debug("[INFO] Entered into  method  loadRadRuleGrpAsso()  ");
		List<Integer> listGrpListOfRuleGrpAsso = new ArrayList<Integer>();
		try
		{
			listGrpListOfRuleGrpAsso = pcrfDataHandler.getGrpListFromRuleGrpAssociation();
			ServerLog.debug("RadRuleGrpAsso size = " +listGrpListOfRuleGrpAsso.size());
			if(listGrpListOfRuleGrpAsso != null)
			{
				Iterator<Integer> grpList = listGrpListOfRuleGrpAsso.iterator();
				List<RuleGroupAssociation> ruleGrpAssociationsList = null;
				while(grpList.hasNext())
				{
				Object objGrpId = grpList.next();
				long grpId=0;
				if(objGrpId instanceof BigDecimal)
				{
					grpId=((BigDecimal)objGrpId).longValue();
				}
				else if(objGrpId instanceof Integer)
				{
					grpId=((Integer)objGrpId).longValue();
				}
				ruleGrpAssociationsList = pcrfDataHandler.getRuleListByGroupId(grpId);
				if(ruleGrpAssociationsList != null)
					radRuleGrpAssoCache.addRuleGrpAssociation(grpId, ruleGrpAssociationsList);
				}
			}
		}
		catch ( Exception e)
		{
			ServerLog.error("Exception in loadRadRuleGrpAsso().",e);
		}
		finally
		{
			listGrpListOfRuleGrpAsso.clear();
			ServerLog.debug("[INFO] Exited from  method  loadRadRuleGrpAsso()  ");
		}
	}
	/**
	 * Caching all Bandwidth master .
	 * @param bandWidthMaster
	 */
	public void loadBandWidthMaster(BandWidthMasterCache bandWidthMaster)
	{
		ServerLog.debug("[INFO] Entered into  method  loadBandWidthMaster()  ");
		List<BandWidthMaster> objBandWidthMaster = new ArrayList<BandWidthMaster>();
		
		try
		{
			Timestamp ts4 = new Timestamp(new Date().getTime());
			objBandWidthMaster = pcrfDataHandler.getAllValuesBandWidthMaster();
			ServerLog.debug("inside RadCacheLoader Time taken to get all BandWidthMaster::: : " +(new Timestamp(new Date().getTime()).getTime()-ts4.getTime())+" millisecs");
			ServerLog.debug("BandWidthMaster size = " +objBandWidthMaster.size());
			if(objBandWidthMaster != null)
			{
				Iterator<BandWidthMaster> it = objBandWidthMaster.iterator();
				while( it.hasNext())
				{
					bandWidthMaster.addBandWidthQOS(it.next());
		
				}
			}
		}
		catch( Exception e)
		{
			ServerLog.error("Exception in loadBandWidthMaster method .",e);
		}
		finally
		{
			objBandWidthMaster.clear();
			ServerLog.debug("[INFO] Exited from  method  loadBandWidthMaster()  ");
		}
	}


}