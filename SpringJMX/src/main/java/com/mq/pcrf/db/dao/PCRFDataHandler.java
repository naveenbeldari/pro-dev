package com.mq.pcrf.db.dao;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mq.db.entity.common.BandWidthMaster;
import com.mq.db.entity.common.RadGroup;
import com.mq.db.entity.pcrf.RadService;
import com.mq.db.entity.pcrf.RadServiceAttribute;
import com.mq.db.entity.policyengine.Rule;
import com.mq.db.entity.policyengine.RuleDetail;
import com.mq.db.entity.policyengine.RuleGroupAssociation;
import com.mq.osslog.ServerLog;



public class PCRFDataHandler 
{
	public PCRFDataHandler()
	{
		
	}
	
	private JdbcTemplate jdbcTemplate;
	
	private Connection conn;
	

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {  
		    this.jdbcTemplate = jdbcTemplate; 
		}  
	   
	   
	  
	   
	public static QueryRunner getQRunner() {
		return QRUNNER;
	}

	/*public static void setQRunner(QueryRunner runner) {
		qRunner = runner;
	}*/

	private static final QueryRunner  QRUNNER = new QueryRunner();
	private static  Map<String, String>	queries;
	
	protected static String selectAllFromRadService;
	protected static String selectAllFromRadGroup;
	protected static String selectAllFromRadServiceAttribute;
	protected static String selectAllFromBandWidthMaster;
	protected static String selectAllFromRule;
	protected static String selectAllFromRuleDetail;
	protected static String selectAllFromRuleGroupAssociation;
	protected static String SELECT_ALL_VENDOR_FROM_RAD_SERVICE_ATTRIBUTE_BY_SERVICEID;
	protected static String SELECT_ATTR_CODE_AND_VALUE_FROM_RAD_SERVICE_ATTRIBUTE_BY_SERVICEID_VENDOR;
	protected static String SELECT_GRP_LIST_FROM_RULE_GRP_ASSOCIATION;
	protected static String SELECT_RULE_BY_GRP_ID_AND_RULE_PRIORITY;
	
	
	public static  final ResultSetHandler  RAD_SERVICE_LIST_HANDLER 			= 		new  BeanListHandler(RadService.class, new BasicRowProcessor(new RadServiceDataHandler()));
	public static  final ResultSetHandler  RAD_SERVICE_HANDLER 					= 		new  BeanHandler(RadService.class, new BasicRowProcessor(new RadServiceDataHandler()));
	

	public  final ResultSetHandler radServiceAttributeListHandler = new  BeanListHandler(RadServiceAttribute.class, new BasicRowProcessor(new RadServiceAttributeDataHandler()));
	public  final ResultSetHandler radServiceAttributeHandler = new  BeanHandler(RadServiceAttribute.class, new BasicRowProcessor(new RadServiceAttributeDataHandler()));

	public static  final ResultSetHandler  RAD_GROUP_LIST_HANDLER 				=		new  BeanListHandler(RadGroup.class, new BasicRowProcessor(new RadGroupDataHandler()));
	public static  final ResultSetHandler  RAD_GROUP_HANDLER					=		new  BeanHandler(RadGroup.class, new BasicRowProcessor(new RadGroupDataHandler()));

	public static  final ResultSetHandler  RAD_SERVICE_ATTRIBUTE_LIST_HANDLER	= 		new  BeanListHandler(RadServiceAttribute.class, new BasicRowProcessor(new RadServiceAttributeDataHandler()));
	public static  final ResultSetHandler  RAD_SERVICE_ATTRIBUTE_HANDLER 		= 		new  BeanHandler(RadServiceAttribute.class, new BasicRowProcessor(new RadServiceAttributeDataHandler()));
	
	public static  final ResultSetHandler  BANDWIDTHMASTER_LIST_HANDLER 		= 		new BeanListHandler(BandWidthMaster.class,new BasicRowProcessor(new BandWidthMasterDataHandler()));
	public static  final ResultSetHandler  BANDWIDTHMASTER_HANDLER 				= 		new BeanHandler(BandWidthMaster.class,new BasicRowProcessor(new BandWidthMasterDataHandler()));

	public static  final  ResultSetHandler RULE_LIST_HANDLER 					= 		new BeanListHandler(Rule.class, new BasicRowProcessor(new RuleDataHandler()));
	public static  final ResultSetHandler  RULE_HANDLER 						= 		new BeanHandler(Rule.class, new BasicRowProcessor(new RuleDataHandler()));

	public static  final ResultSetHandler  RULE_DETAIL_HANDLER 					= 		new  BeanHandler(RuleDetail.class, new BasicRowProcessor(new RuleDetailDataHandler()));
	public static  final ResultSetHandler  RULE_DETAIL_LIST_HANDLER 			= 		new  BeanListHandler(RuleDetail.class, new BasicRowProcessor(new RuleDetailDataHandler()));

	public static  final ResultSetHandler  RULE_GROUP_ASSOCIATION_LIST_HANDLER 	= 		new BeanListHandler(RuleGroupAssociation.class,new BasicRowProcessor(new RuleGroupAssociationDataHandler()));   
	public static  final ResultSetHandler  RULE_GROUP_ASSOCIATION_HANDLER      	= 		new BeanHandler(RuleGroupAssociation.class,new BasicRowProcessor(new RuleGroupAssociationDataHandler()));
	

	
	
	private static void getQueries(){
		
		selectAllFromRadService			 	= 	queries.get("SELECT_ALL_FROM_RAD_SERVICE");

		selectAllFromRadGroup 			 	= 	queries.get("SELECT_ALL_FROM_RAD_GROUP");

		selectAllFromRadServiceAttribute 	= 	queries.get("SELECT_ALL_FROM_RAD_SERVICE_ATTRIBUTE");
	
		selectAllFromBandWidthMaster 		= 	queries.get("SELECT_ALL_FROM_BAND_WIDTH_MASTER");
		
		selectAllFromRule 					= 	queries.get("SELECT_ALL_FROM_RULE");
		
		selectAllFromRuleDetail 			= 	queries.get("SELECT_ALL_FROM_RULE_DETAIL");
		
		selectAllFromRuleGroupAssociation 	= 	queries.get("SELECT_ALL_FROM_RULE_GROUP_ASSOCIATION");
		
		SELECT_ALL_VENDOR_FROM_RAD_SERVICE_ATTRIBUTE_BY_SERVICEID = queries.get("SELECT_ALL_VENDOR_FROM_RAD_SERVICE_ATTRIBUTE_BY_SERVICEID");
		
		SELECT_ATTR_CODE_AND_VALUE_FROM_RAD_SERVICE_ATTRIBUTE_BY_SERVICEID_VENDOR = queries.get("SELECT_ATTR_CODE_AND_VALUE_FROM_RAD_SERVICE_ATTRIBUTE_BY_SERVICEID_VENDOR");
		
		SELECT_GRP_LIST_FROM_RULE_GRP_ASSOCIATION = queries.get("SELECT_GRP_LIST_FROM_RULE_GRP_ASSOCIATION");
		
		SELECT_RULE_BY_GRP_ID_AND_RULE_PRIORITY = queries.get("SELECT_RULE_BY_GRP_ID_AND_RULE_PRIORITY");
		
		}
	


	public static void loadQueries(String dataBaseType) {
		
		String resourcePath = "";
		try 
		{
			String str;
			if(dataBaseType.equalsIgnoreCase("MYSQL"))
				resourcePath = "conf/mysql-queries.properties";
			else if(dataBaseType.equalsIgnoreCase("oracle"))
				resourcePath = "conf/oracle-queries.properties";
			String pcrfHome = System.getProperty("MQPcrf.home");
			PropertyResourceBundle prop;
			if(pcrfHome==null){
				prop = new PropertyResourceBundle(new FileInputStream(resourcePath));
			}else{
				prop = new PropertyResourceBundle(new FileInputStream(pcrfHome+File.separator+resourcePath));
			}
			prop = new PropertyResourceBundle(new FileInputStream(resourcePath));
			Map<String, String> queriesMap = new LinkedHashMap<String,String>();
			for (Enumeration e = prop.getKeys() ; e.hasMoreElements() ;) {               
				str=(String)e.nextElement();				
				queriesMap.put(str,prop.getString(str));				
		}
			queries = queriesMap;
			getQueries();
			//Common.loadDBQueries("mysql"); //No Significance at present
		}
		catch (Exception e)
		{
			ServerLog.error("Cannot load properties "+resourcePath,e);
			throw new IllegalStateException(e);
		}
	}
  
	
	private  Connection getConnection() {
		try {
			 conn = jdbcTemplate.getDataSource().getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}



	/**
	 * Retrieves  all the PCRF Bandwidths .
	 * @return List of available PCRF Bandwidths.
	 * @throws SQLException : If database access error occurs .
	 */
	public  List<BandWidthMaster> getAllValuesBandWidthMaster()
	{
		ServerLog.debug("[INFO] Entered into  method  getAllValuesBandWidthMaster()");
		
		try
		{
			Timestamp ts5 = new Timestamp(new Date().getTime());
			conn=getConnection();		
			
			 
			List<BandWidthMaster> bandwidthlists  = (List<BandWidthMaster>) QRUNNER.query(conn, selectAllFromBandWidthMaster, this.BANDWIDTHMASTER_LIST_HANDLER);
			ServerLog.debug("inside PCRFDataHandler Time taken to get all BandWidthMaster : " +(new Timestamp(new Date().getTime()).getTime()-ts5.getTime())+" millisecs");
			return bandwidthlists;
		}
		catch(SQLException sqle)
		{
			ServerLog.error("Exception in the method getAllValuesBandWidthMaster.Error is:",sqle);
			return null;
		}
		catch(Exception e)
		{
			ServerLog.error("Exception in the method getAllValuesBandWidthMaster.Error is:",e);
			return null;
		}
		finally
		{
			ServerLog.debug("[INFO] Exiting from method  getAllValuesBandWidthMaster()");
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				} else {
					conn = null;
				}
			} catch (SQLException e) {
				ServerLog.error("Exception in closing connection.Error is:", e);
			}
		}
		
	}
	
	
	/**
	 * Retrieves  all the PCRF services .
	 * @return List of available PCRF Services.
	 * @throws SQLException : If database access error occurs .
	 */
	public  List<RadServiceAttribute> getAllRadServices()
	{
		ServerLog.debug("[INFO] Entered into  method  getAllRadServices()  ");
		
		try
		{
			conn=getConnection();
			return  (List<RadServiceAttribute>) QRUNNER.query(conn, selectAllFromRadService, this.radServiceAttributeListHandler);		
		}
		catch(SQLException sqle)
		{
			ServerLog.error("Exception in getAllRadServices method.Error is:",sqle);
			return null;
		}
		catch(Exception e)
		{
			ServerLog.error("Exception in getAllRadServices method.Error is:",e);
			return null;
		}
		finally
		{
			ServerLog.debug("[INFO] Exiting from method  getAllRadServices()");
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				} else {
					conn = null;
				}
			} catch (SQLException e) {
				ServerLog.error("Exception in closing connection.Error is:", e);
			}
		}
	}
	
	

	/**
	 * Retrieves  all the PCRF Groups .
	 * @return List of available PCRF Groups.
	 * @throws SQLException : If database access error occurs .
	 */
	public  List<RadGroup> getAllRadGroups()
	{
		ServerLog.debug("[INFO] Entered into  method  getAllRadGroups()  ");
		
		try
		{
			conn=getConnection();
			return (List<RadGroup>) QRUNNER.query(conn, selectAllFromRadGroup, RAD_GROUP_LIST_HANDLER);
		}
		catch(SQLException sqle)
		{
			ServerLog.error("Exception in getAllRadGroups() method.",sqle);
			return null;
		}
		catch(Exception e)
		{
			ServerLog.error("Exception in getAllRadGroups() method.",e);
			return null;
		}
		finally
		{
			ServerLog.debug("[INFO] Exited from method  getAllRadGroups()  ");
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				} else {
					conn = null;
				}
			} catch (SQLException e) {
				ServerLog.error("Exception in closing connection.Error is:", e);
			}
		}
	}
	
	
	/**
	 * Retrieves  all the PCRF service Attributes.
	 * @return List of available PCRF Service Attributes.
	 * @throws SQLException : If database access error occurs .
	 */
	public  List<RadServiceAttribute> getAllRadServiceAttributes()  
	{
		ServerLog.debug("[INFO] Entered into  method  getAllRadServiceAttributes()  ");
		
		try
		{
			conn=getConnection();
			
			return (List<RadServiceAttribute>) QRUNNER.query(conn, selectAllFromRadServiceAttribute, this.RAD_SERVICE_ATTRIBUTE_LIST_HANDLER);
		}
		catch(SQLException sqle)
		{
			ServerLog.error("Exception in getAllRadServiceAttributes method.Error is:",sqle);
			return null;
		}
		catch(Exception e)
		{
			ServerLog.error("Exception in getAllRadServiceAttributes method.Error is:",e);
			return null;
		}
		finally
		{
			ServerLog.debug("[INFO] Exited from method  getAllRadServiceAttributes()  ");
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				} else {
					conn = null;
				}
			} catch (SQLException e) {
				ServerLog.error("Exception in closing connection.Error is:", e);
			}
		}
	}	


/**
 * Retrieves  all the PCRF Rules.
 * @return List of available PCRF Rules.
 * @throws SQLException : If database access error occurs .
 */
public  List<Rule> getAllRules()
{
	ServerLog.debug("[INFO] Entered into  method  getAllRadServiceAttributes()  ");
	
	try
	{
		conn=getConnection();
		
		return (List<Rule>) QRUNNER.query(conn, selectAllFromRule, this.RULE_LIST_HANDLER);
	}
	catch(SQLException sqle)
	{
		ServerLog.error("Exception in getAllRadServiceAttributes method.Error is:",sqle);
		return null;
	}
	catch(Exception e)
	{
		ServerLog.error("Exception in getAllRadServiceAttributes method.Error is:",e);
		return null;
	}
	finally
	{
		ServerLog.debug("[INFO] Exited from method  getAllRadServiceAttributes()  ");
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			} else {
				conn = null;
			}
		} catch (SQLException e) {
			ServerLog.error("Exception in closing connection.Error is:", e);
		}
	}
}	



/**
 * Retrieves  all the PCRF Rules Details.
 * @return List of available PCRF Rules Details.
 * @throws SQLException : If database access error occurs .
 */
public  List<RuleDetail> getAllRuleDetails()
{
	ServerLog.debug("[INFO] Entered into  method  getAllRuleDetails() ");
	
	try
	{
		conn=getConnection();
		
		return (List<RuleDetail>) QRUNNER.query(conn, selectAllFromRuleDetail, this.RULE_DETAIL_LIST_HANDLER);
	}
	catch(SQLException sqle)
	{
		ServerLog.error("Exception in getAllRuleDetails method.Error is:",sqle);
		return null;
	}
	catch(Exception e)
	{
		ServerLog.error("Exception in getAllRuleDetails method.Error is:",e);
		return null;
	}
	finally
	{
		ServerLog.debug("[INFO] Exited from method  getAllRuleDetails()  ");
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			} else {
				conn = null;
			}
		} catch (SQLException e) {
			ServerLog.error("Exception in closing connection.Error is:", e);
		}
	}
}	

public  ArrayList getAllVendorListBySvcId(Long serviceId) throws SQLException 
{
	ServerLog.debug("[INFO] Entered into  method  getAllVendorListBySvcId()  ");
	
	ColumnListHandler ah = null;
	try
	{
		ah = new ColumnListHandler();
		conn=getConnection();
		
		List<String> lstVendor = new ArrayList<String>();
		assert(conn != null);
		return (ArrayList) QRUNNER.query(conn, SELECT_ALL_VENDOR_FROM_RAD_SERVICE_ATTRIBUTE_BY_SERVICEID,(Long)serviceId,ah);
	}
	catch(SQLException sqle)
	{
		ServerLog.error("Error in getAllVendorListBySvcId method.Error is:",sqle);
		return null;
	}
	catch(Exception e)
	{
		ServerLog.error("Error in getAllVendorListBySvcId method.Error is:",e);
		return null;
	}
	finally
	{
		if (conn!=null) {
			conn.close();
		}
		ah = null;
		ServerLog.debug("[INFO] Exiting from  method  getAllVendorListBySvcId()  ");
	}
}

/**
 * Returns Group id list from Rule-Group Association 
 * @return List<Integer>
 * @throws SQLException
 */
public List<Integer> getGrpListFromRuleGrpAssociation() throws SQLException
{
	ServerLog.debug("[INFO] Entered into  method  getGrpListFromRuleGrpAssociation()  ");
	
	ColumnListHandler objColumnListHandler = null;
	try
	{
		conn=getConnection();
		objColumnListHandler = new ColumnListHandler();
		
		assert(conn != null);
		
		return (List<Integer>) QRUNNER.query(conn, SELECT_GRP_LIST_FROM_RULE_GRP_ASSOCIATION, objColumnListHandler);
	}
	catch(SQLException sqle)
	{
		ServerLog.error("Exception in getGrpListFromRuleGrpAssociation.Error is:",sqle);
		return null;
	}
	catch(Exception e)
	{
		ServerLog.error("Exception in getGrpListFromRuleGrpAssociation.Error is:",e);
		return null;
	}
	finally
	{
		if (conn!=null) {
			conn.close();
		}
		objColumnListHandler = null;
		ServerLog.debug("[INFO] Exiting from method  getGrpListFromRuleGrpAssociation()  ");
	}

}

/**
 * Retrieves  all the Radius Rules based on Group and rule priority .
 * @param grpId 
 * @return List of available Rule list of Specific rule.
 * @throws SQLException : If database access error occurs .
 */
public  List<RuleGroupAssociation> getRuleListByGroupId(long grpId) throws SQLException
{
	ServerLog.debug("[INFO] Entered into  method  getRuleListByGrpId()  ");
	
	try
	{
		conn=getConnection();
		assert(conn != null);
		Object[] args = new Object[2];
		args[0] = (Long) grpId;
		args[1] = (Long) grpId;
		return (List<RuleGroupAssociation>) QRUNNER.query(conn, SELECT_RULE_BY_GRP_ID_AND_RULE_PRIORITY,args, RULE_GROUP_ASSOCIATION_LIST_HANDLER);
	}
	catch(SQLException sqle)
	{
		ServerLog.error("Exception in the method getRuleListByGroupId.Error is:",sqle);
		return null;
	}
	catch(Exception e)
	{
		ServerLog.error("Exception in the method getRuleListByGroupId.Error is:",e);
		return null;
	}
	finally
	
	{
		if (conn!=null) {
			conn.close();
		}
		ServerLog.debug("[INFO] Exiting from method  getRuleListByGrpId()  ");
	}

}


public  List<RadServiceAttribute> getAttributesListBySvcIdByVendor(Long serviceId,String strVendor) throws SQLException
{
	ServerLog.debug("[INFO] Entered into  method  getAttributesListBySvcIdByVendor()  ");
	
	try
	{
		//conn = getConnectionByThreadNumber("Main");
		conn=getConnection();
		assert(conn != null);
		Object[] args = new Object[2];
		args[0] = (Long)serviceId;
		args[1] = (String)strVendor;
		return (List<RadServiceAttribute>) QRUNNER.query(conn, SELECT_ATTR_CODE_AND_VALUE_FROM_RAD_SERVICE_ATTRIBUTE_BY_SERVICEID_VENDOR, args , this.radServiceAttributeListHandler);
	}
	catch(SQLException sqle)
	{
		ServerLog.error("Error in getAttributesListBySvcIdByVendor.Error is:",sqle);
		return null;
	}
	catch(Exception e)
	{
		ServerLog.error("Error in getAttributesListBySvcIdByVendor.Error is:",e);
		return null;
	}
	finally
	{
		if (conn!=null) {
			conn.close();
		}
		
		ServerLog.debug("[INFO] Exiting from  method  getAttributesListBySvcIdByVendor()  ");
	}
}


/**
 * Retrieves  all the PCRF Rules Details.
 * @return List of available PCRF Rules Details.
 * @throws SQLException : If database access error occurs .
 */
public  List<RuleGroupAssociation> getAllRuleGroupAssociations()
{
	ServerLog.debug("[INFO] Entered into  method  getAllRuleGroupAssociations() ");
	
	try
	{
		
		conn=getConnection();
		
		return (List<RuleGroupAssociation>) QRUNNER.query(conn, selectAllFromRuleGroupAssociation, this.RULE_GROUP_ASSOCIATION_LIST_HANDLER);
	}
	catch(SQLException sqle)
	{
		ServerLog.error("Exception in getAllRuleGroupAssociations method.Error is:",sqle);
		return null;
	}
	catch(Exception e)
	{
		ServerLog.error("Exception in getAllRuleGroupAssociations method.Error is:",e);
		return null;
	}
	finally
	{
		ServerLog.debug("[INFO] Exited from method  getAllRuleGroupAssociations()  ");
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			} else {
				conn = null;
			}
		} catch (SQLException e) {
			ServerLog.error("Exception in closing connection.Error is:", e);
		}
	}
}

}


class RadServiceDataHandler extends BeanProcessor{

	public RadService toBean(ResultSet rs, Class type) throws SQLException {
		if(rs == null){
			throw new NullPointerException();
		}
		RadService radServ=new RadService();		

		radServ.setServiceId(rs.getLong("serviceId"));						     
		radServ.setServiceCode(rs.getString("serviceCode"));
		radServ.setServiceDesc(rs.getString("serviceDesc"));		
		
		return radServ;
	}
}



class RadGroupDataHandler extends BeanProcessor{

	public RadGroup toBean(ResultSet rs, Class type) throws SQLException {
		
		if(rs == null){
			throw new NullPointerException();
		}
		RadGroup radgroup=new RadGroup();		
		radgroup.setGroupId(rs.getLong("groupId"));						     
		radgroup.setGroupCode(rs.getString("groupCode"));
		radgroup.setGroupDesc(rs.getString("groupDesc"));		
		radgroup.setServiceId(rs.getLong("serviceId"));
		radgroup.setMaxSimulSess(rs.getInt("maxSimulSess"));
		radgroup.setSessionIdleTime(rs.getInt("sessionIdleTime"));
		radgroup.setExpiryUnit(rs.getString("expiryUnit"));						     
		radgroup.setExpiryValue(rs.getInt("expiryValue"));
		radgroup.setUseDataBank(rs.getInt("useDataBank"));		
		radgroup.setDataBankUnit(rs.getString("dataBankUnit"));
		radgroup.setDataBankValue(rs.getLong("dataBankValue"));
		radgroup.setUseTimeBank(rs.getInt("useTimeBank"));
		radgroup.setTimeBankUnit(rs.getString("timeBankUnit"));						     
		radgroup.setTimeBankValue(rs.getLong("timeBankValue"));
		radgroup.setDowngradeGroupId(rs.getLong("DOWNGRADE_GROUP_ID"));
		radgroup.setIsPreActivate(rs.getInt("IS_PREACTIVATE"));
		radgroup.setIsFairUsageLimit(rs.getInt("IS_FAIR_USAGE_LIMIT"));
		
		return radgroup;
	}
}


class RadServiceAttributeDataHandler extends BeanProcessor{

	public RadServiceAttribute toBean(ResultSet rs, Class type) throws SQLException {
		if(rs == null){
			throw new NullPointerException();
		}
		RadServiceAttribute radServiceAttr=new RadServiceAttribute();		
		radServiceAttr.setServiceAttributeId(rs.getLong("serviceAttributeId"));		
		radServiceAttr.setServiceId(rs.getLong("serviceid"));						     
		radServiceAttr.setVendor(rs.getString("vendor"));
		radServiceAttr.setAttributeCode(rs.getString("attributeCode"));
		radServiceAttr.setAttributeValue(rs.getString("attributeValue"));
		
		return radServiceAttr;	

	}
}



class BandWidthMasterDataHandler extends BeanProcessor
{
	public BandWidthMaster toBean(ResultSet rs, Class type) throws SQLException
	{
		if(rs == null)
		{
			throw new NullPointerException();
		}
		BandWidthMaster objBandWidthMaster = new BandWidthMaster();
		objBandWidthMaster.setBandWidthId(rs.getLong("bandwidth_id"));
		objBandWidthMaster.setQoS(rs.getString("qos"));
		objBandWidthMaster.setActualQoS(rs.getString("actual_qos"));
		
		return objBandWidthMaster;	
	}
}

class RuleGroupAssociationDataHandler extends BeanProcessor{

	public RuleGroupAssociation toBean(ResultSet rs, Class type) throws SQLException {
		if(rs == null){
			throw new NullPointerException();
		}
		RuleGroupAssociation objRuleGroupAssociation = new RuleGroupAssociation();		
		objRuleGroupAssociation.setRuleGroupId(rs.getLong("RULE_GROUP_ID"));		
		objRuleGroupAssociation.setRuleId(rs.getLong("RULE_ID"));
		objRuleGroupAssociation.setGroupId(rs.getLong("GROUP_ID"));
		objRuleGroupAssociation.setStartDay(rs.getInt("START_DAY"));
		objRuleGroupAssociation.setEndDay(rs.getInt("END_DAY"));
		objRuleGroupAssociation.setStartTime(rs.getString("START_TIME"));
		objRuleGroupAssociation.setEndTime(rs.getString("END_TIME"));
		objRuleGroupAssociation.setStartNumericTime(rs.getLong("START_NUMERIC_TIME"));						     
		objRuleGroupAssociation.setEndNumericTime(rs.getLong("END_NUMERIC_TIME"));
		objRuleGroupAssociation.setUploadQoS(rs.getString("UPLOAD_QOS"));
		objRuleGroupAssociation.setDownloadQoS(rs.getString("DOWNLOAD_QOS"));
		objRuleGroupAssociation.setFreeUsageLimit(rs.getLong("FREE_USAGE_LIMIT"));
		objRuleGroupAssociation.setIsAccountable(rs.getString("IS_ACCOUNTABLE"));
		objRuleGroupAssociation.setIsServiceRestrict(rs.getString("IS_SERVICE_RESTRICT"));
		objRuleGroupAssociation.setPriority(rs.getInt("PRIORITY"));
		
		return objRuleGroupAssociation;	

	}
}

class RuleDetailDataHandler extends BeanProcessor{

  public RuleDetail toBean(ResultSet rs, Class type) throws SQLException {
	if(rs == null){
		throw new NullPointerException();
	}
	RuleDetail objRuleDetail = new RuleDetail();		
	objRuleDetail.setRuleDetailId(rs.getLong("RULE_DETAIL_ID"));
	objRuleDetail.setRuleId(rs.getLong("RULE_ID"));
	objRuleDetail.setCatagory(rs.getInt("CATAGORY"));
	objRuleDetail.setCatagoryOption(rs.getString("CATAGORY_OPTION"));
	objRuleDetail.setOptionValue(rs.getString("OPTION_VALUE"));

    return objRuleDetail;
}	
}



class RuleDataHandler extends BeanProcessor{

  public Rule toBean(ResultSet rs, Class type) throws SQLException {
	if(rs == null){
		throw new NullPointerException();
	}
	Rule objRule = new Rule();		
	objRule.setRuleId(rs.getLong("RULE_ID"));
	objRule.setRuleCode(rs.getString("RULE_CODE"));
	objRule.setRuleDesc(rs.getString("RULE_DESC"));
	objRule.setIsAccountable(rs.getString("IS_ACCOUNTABLE"));
	objRule.setRuleSummary(rs.getString("RULE_SUMMARY"));
	objRule.setIsServiceRestrict(rs.getString("IS_SERVICE_RESTRICT"));

	return objRule;
}	
  
	/*
	 * public class PCRFDataHandler{ public static void main(String[] args) {
	 * PCRFDataHandler1 obj=new PCRFDataHandler1(); obj.loadQueries("mysql");
	 * List radSList=obj.getAllValuesBandWidthMaster();
	 * System.out.println("Size of radSList is"+radSList.size());
	 * System.out.println("radSList="+radSList.toString()); } }
	 */
}


/*public class PCRFDataHandler{
	public static void main(String[] args) {
		PCRFDataHandler1 obj=new PCRFDataHandler1();
		obj.loadQueries("mysql");
		List radSList=obj.getAllValuesBandWidthMaster();
		System.out.println("Size of radSList is"+radSList.size());
		System.out.println("radSList="+radSList.toString());
	}*/


