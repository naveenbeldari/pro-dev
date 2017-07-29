package com.my.rest.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.my.rest.util.StoreSMSDetails;
import com.my.rest.util.UserTokenDetails;

public class StoreSMSDetailsDAOImpl implements SMSDetailsDAO {
	
	  

	public boolean storeDetails(StoreSMSDetails ssd) {
		boolean result = false;
		Connection connection = null;
		try {
			System.out.println("inside StoreSMSDetailsDAOImpl storeDetails  method");
			
			StoreSMSDetailsDAOImpl ssdao = new StoreSMSDetailsDAOImpl();
			
			//getting database connection object
		    connection = getConnection();
		    
			//checking duplicate entry for SMS details
			boolean chkDuplicate = ssdao.getMSGDetails(Integer.parseInt(ssd.getMsgId()));
			
			if(chkDuplicate){
			System.out.println("SMS Details exist in db....");
			return result;
			}
			
			else{
			String sql = "INSERT INTO `store_sms_details` VALUES (?,?,?,?)";
			PreparedStatement ps= connection.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(ssd.getMsgId()));
			ps.setString(2, ssd.getMsgType());
			ps.setLong(3, ssd.getRecipientId());
			ps.setString(4, ssd.getMsgDesc());
			
			int rs=ps.executeUpdate();
			if (rs==1) {
				System.out.println("SMS Details Saved....");
				result=true;
				
			}
			else{
				System.out.println("SMS Details not Saved....");
			}
			
		}
			
		} catch (Exception e) {
			
			System.out.println("Exception Message is " + e.getMessage());
			e.printStackTrace();
			return result;
		}
		finally{
			try {
				System.out.println("Closing db connection ");
				//Closing database connection
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//returning response
		return result;
		
		
	}
	
	public static Connection getConnection() throws SQLException{
		Connection conn=null;
		InputStream input = null;
		try {
			System.out.println("inside getConnection method");
			input = new FileInputStream("D:\\MyWorks\\Notification_WS\\src\\db.properties");
			Properties prop = new Properties();
			prop.load(input);
			String className = prop.getProperty("className");
			String url = prop.getProperty("url");
			String username = prop.getProperty("username");
			String password = prop.getProperty("password");
			
			Class.forName(className);
			//getting database connection object
			conn = DriverManager.getConnection(url,username,password);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
		
	}
	
	public static void main(String[] args) {
	    try {
		System.out.println(getConnection());
	    } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	@Override
	public boolean deleteDetails(int msgId) {
		boolean result = false;
		Connection connection = null;
		try {
			System.out.println("inside StoreSMSDetailsDAOImpl deleteDetails method");
			//getting database connection object
		    connection = getConnection();
			
			String sql = "DELETE FROM `store_sms_details` WHERE MSG_ID= ?";
			
			PreparedStatement ps= connection.prepareStatement(sql);
			ps.setInt(1, msgId);
			int rs=ps.executeUpdate();
			if (rs==1) {
				System.out.println("SMS Details deleted....");
				result=true;
			}
			else{
				System.out.println("SMS Details not found....");
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally{
			try {
				System.out.println("Closing db connection ");
				//Closing database connection
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//returning response
		return result;
		
	}

	@Override
	public StoreSMSDetails getSMSDetails(int msgId) {
		
		StoreSMSDetails sd = null;
		Connection connection = null;
		try {
			sd = new StoreSMSDetails();
			System.out.println("inside StoreSMSDetailsDAOImpl getSMSDetails method");
			//getting database connection object
			connection = getConnection();
			
			String sql = "SELECT * FROM `store_sms_details` WHERE MSG_ID = ? ";
			
			PreparedStatement ps= connection.prepareStatement(sql);
			ps.setInt(1, msgId);
			
			ResultSet rs =ps.executeQuery();
			
			if (rs.next()) {
				sd.setMsgId(rs.getString("MSG_ID"));
				sd.setMsgType(rs.getString("MSG_TYPE"));
				sd.setRecipientId(rs.getLong("RECIPIENT_NUMBER"));
				sd.setMsgDesc(rs.getString("MSG_DESC"));
				System.out.println("SMS Details fetched....");
			}
			else{
			System.out.println("SMS Details not found....");
			sd=null;
			return sd;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				System.out.println("Closing db connection ");
				//Closing database connection
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//returning response
		return sd;
	}

	@Override
	public boolean getMSGDetails(int msgId) {
		boolean result = false;
		StoreSMSDetails sd = null;
		Connection connection = null;
		try {
			sd = new StoreSMSDetails();
			System.out.println("inside StoreSMSDetailsDAOImpl getMSGDetails method");
			//getting database connection object
			connection = getConnection();
			
			String sql = "SELECT * FROM `store_sms_details` WHERE MSG_ID = ? ";
			
			PreparedStatement ps= connection.prepareStatement(sql);
			ps.setInt(1, msgId);
			
			ResultSet rs =ps.executeQuery();
			if (rs.next()) {
				System.out.println("SMS Details exist in database....");
				sd.setMsgId(rs.getString("MSG_ID"));
				result=true;
			}
			else{
				System.out.println("SMS Details not exist in database....");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				System.out.println("Closing db connection ");
				//Closing database connection
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//returning response
		return result;
	}

	@Override
	public boolean getUserDetails(String username, String password) {
	    	boolean result = false;
		Connection connection = null;
	    try {
		System.out.println("inside StoreSMSDetailsDAOImpl getUserDetails method");
		//getting database connection object
		connection = getConnection();
		
		String sql = "SELECT * FROM `userdetails` WHERE username = ? and password = ? ";
		
		PreparedStatement ps= connection.prepareStatement(sql);
		ps.setString(1, username);
		ps.setString(2, password);
		
		ResultSet rs =ps.executeQuery();
		if (rs.next()) {
			System.out.println("User Details exist in database....");
			result=true;
		}
		else{
			System.out.println("User Details not exist in database....");
			result=false;
		}
	
	} catch (Exception e) {
		e.printStackTrace();
	}
	finally{
		try {
			System.out.println("Closing db connection ");
			//Closing database connection
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	    return result;
	}

	@Override
	public boolean insertUserTokenDetails(UserTokenDetails userTokenDetails) {
	    	boolean result = false;
		Connection connection = null;
		try {
			System.out.println("inside StoreSMSDetailsDAOImpl insertUserTokenDetails  method");
			
			StoreSMSDetailsDAOImpl ssdao = new StoreSMSDetailsDAOImpl();
			
			//getting database connection object
			connection = getConnection();
		    
			//checking duplicate entry for SMS details
			boolean chkDuplicate = ssdao.getUserTokenDetails(userTokenDetails);
			
			if(chkDuplicate){
			System.out.println("UserTokenDetails Details exist in db....");
			return result;
			}
			
			else{
			String sql = "INSERT INTO `usertokendetails` VALUES (?,?,?,?,?,?,?)";
			PreparedStatement ps= connection.prepareStatement(sql);
			ps.setString(1, userTokenDetails.getUsername());
			ps.setString(2, userTokenDetails.getToken());
			ps.setString(3, userTokenDetails.getCreatedBy());
			ps.setTimestamp(4, userTokenDetails.getCreatedDate());
			ps.setString(5, userTokenDetails.getModifiedBy());
			ps.setTimestamp(6, userTokenDetails.getModifiedDate());
			ps.setTimestamp(7, userTokenDetails.getTokenExpTime());
			
			int rs=ps.executeUpdate();
			if (rs==1) {
				System.out.println("UserTokenDetails Details Saved....");
				result=true;
				
			}
			else{
				System.out.println("UserTokenDetails Details not Saved....");
			}
			
		}
			
		} catch (Exception e) {
			
			System.out.println("Exception Message is " + e.getMessage());
			e.printStackTrace();
			return result;
		}
		finally{
			try {
				System.out.println("Closing db connection ");
				//Closing database connection
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    return false;
	}

	@Override
	public boolean getUserTokenDetails(UserTokenDetails userTokenDetails) {
	    	boolean result = false;
		Connection connection = null;
	    try {
		System.out.println("inside StoreSMSDetailsDAOImpl getUserTokenDetails method");
		//getting database connection object
		connection = getConnection();
		//String sql = "SELECT * FROM `usertokendetails` WHERE username = ? and tokenexptime <= now() ";
		String sql = "SELECT * FROM `usertokendetails` WHERE username = ? ";
		
		PreparedStatement ps= connection.prepareStatement(sql);
		ps.setString(1, userTokenDetails.getUsername());
		//ps.setString(2, userTokenDetails.getToken());
		
		ResultSet rs =ps.executeQuery();
		if (rs.next()) {
			System.out.println("User token Details exist in database....");
			result=true;
		}
		else{
			System.out.println("User token Details does not exist in database....");
			result=false;
		}
	
	} catch (Exception e) {
		e.printStackTrace();
	}
	finally{
		try {
			System.out.println("Closing db connection ");
			//Closing database connection
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	    return result;
	}

	

}
