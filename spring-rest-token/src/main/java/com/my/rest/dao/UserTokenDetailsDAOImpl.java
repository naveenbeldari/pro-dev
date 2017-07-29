package com.my.rest.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.codec.binary.Base64;

import com.my.rest.util.UserTokenDetails;

/**
 * @author naveen.beldari
 *
 */

public class UserTokenDetailsDAOImpl implements UserTokenDetailsDAO {
	
		
    public static Connection getConnection() throws SQLException{
	Connection conn=null;
	InputStream input = null;
	try {
		System.out.println("inside getConnection method");
		input = new FileInputStream("F:\\MyWorks\\spring-rest-token\\src\\main\\resources\\db.properties");
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
	@Override
	public boolean getUserDetails(String username, String password) {
	    	boolean result = false;
		Connection connection = null;
	    try {
		System.out.println("inside UserTokenDetailsDAOImpl getUserDetails method");
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
			System.out.println("inside UserTokenDetailsDAOImpl insertUserTokenDetails  method");
			
			UserTokenDetailsDAOImpl ssdao = new UserTokenDetailsDAOImpl();
			
			//getting database connection object
			connection = getConnection();
		    
			//checking duplicate entry for SMS details
			boolean chkDuplicate = ssdao.getUserTokenDetails(userTokenDetails);
			
			if(!chkDuplicate){
			System.out.println("UserTokenDetails Details exist in db....");
			return result;
			}
			
			else{
			String sql = "INSERT INTO `usertokendetails` VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement ps= connection.prepareStatement(sql);
			ps.setString(1, userTokenDetails.getUsername());
			ps.setString(2, userTokenDetails.getToken());
			ps.setString(3, userTokenDetails.getCreatedBy());
			ps.setTimestamp(4, userTokenDetails.getCreatedDate());
			ps.setString(5, userTokenDetails.getModifiedBy());
			ps.setTimestamp(6, userTokenDetails.getModifiedDate());
			ps.setTimestamp(7, userTokenDetails.getTokenExpTime());
			ps.setString(8, userTokenDetails.getUserLoggedin());
			
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
	    return result;
	}

	@Override
	public boolean getUserTokenDetails(UserTokenDetails userTokenDetails) {
	    	boolean result = false;
		Connection connection = null;
	    try {
		System.out.println("inside UserTokenDetailsDAOImpl getUserTokenDetails method");
		//getting database connection object
		connection = getConnection();
		//String sql = "SELECT * FROM `usertokendetails` WHERE username = ? and tokenexptime >= now() ";
		String sql = "SELECT * FROM `usertokendetails` WHERE username = ? ";
		
		PreparedStatement ps= connection.prepareStatement(sql);
		ps.setString(1, userTokenDetails.getUsername());
		//ps.setString(2, userTokenDetails.getToken());
		
		ResultSet rs =ps.executeQuery();
		if (rs.next()) {
			System.out.println("User token Details exist in database....");
			result=false;
		}
		else{
			System.out.println("User token Details does not exist in database....");
			result=true;
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
	public boolean validateTokenDetails(String token) {
	    	boolean result = false;
		Connection connection = null;
	    try {
		System.out.println("inside UserTokenDetailsDAOImpl validateTokenDetails method");
		//getting database connection object
		connection = getConnection();
		//String sql = "SELECT * FROM `usertokendetails` WHERE token = ? and tokenexptime > now() and isuserloggedin = 'Y' ";
		String sql = "SELECT * FROM `usertokendetails` WHERE token = ? and isuserloggedin = 'Y' ";
		
		PreparedStatement ps= connection.prepareStatement(sql);
		ps.setString(1, token);
		
		ResultSet rs =ps.executeQuery();
		if (rs.next()) {
		    System.out.println("User token details exist in database....");
			result=true;
			return result;
		}
		else{
		    System.out.println("User token Details expired....");
			result=false;
			return result;
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
	public boolean updateUserTokenDetails(UserTokenDetails userTokenDetails) {
		boolean result = false;
		Connection connection = null;
	    try {
		System.out.println("inside UserTokenDetailsDAOImpl updateUserTokenDetails method");
		//getting database connection object
		connection = getConnection();
		String sql = "UPDATE  `usertokendetails` set isuserloggedin = ? , modifiedby = ? , modifieddate = ? , tokenexptime =? WHERE token = ? ";
		PreparedStatement ps= connection.prepareStatement(sql);
		ps.setString(1, userTokenDetails.getUserLoggedin());
		ps.setString(2, userTokenDetails.getModifiedBy());
		ps.setTimestamp(3, userTokenDetails.getModifiedDate());
		ps.setTimestamp(4, userTokenDetails.getTokenExpTime());
		ps.setString(5, userTokenDetails.getToken());
		

		
		int rs =ps.executeUpdate();
		if (rs==1) {
		    System.out.println("User token details updated in database....");
			result=true;
		}
		else{
		    System.out.println("User token Details not found....");
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
	public UserTokenDetails getTokenDetails(UserTokenDetails userTokenDetails) {
		UserTokenDetails tokenDetails = new UserTokenDetails();
		Connection connection = null;
	    try {
		System.out.println("inside UserTokenDetailsDAOImpl getUserTokenDetails method");
		//getting database connection object
		connection = getConnection();
		String sql = "SELECT * FROM `usertokendetails` WHERE username = ? ";
		
		PreparedStatement ps= connection.prepareStatement(sql);
		ps.setString(1, userTokenDetails.getUsername());
		
		ResultSet rs =ps.executeQuery();
		if (rs.next()) {
			System.out.println("User token Details exist in database....");
			tokenDetails.setUsername(rs.getString("USERNAME"));
			tokenDetails.setToken(rs.getString("TOKEN"));
			tokenDetails.setCreatedBy(rs.getString("CREATEDBY"));
			tokenDetails.setCreatedDate(rs.getTimestamp("CREATEDDATE"));
			tokenDetails.setModifiedBy(rs.getString("MODIFIEDBY"));
			tokenDetails.setModifiedDate(rs.getTimestamp("MODIFIEDDATE"));
			tokenDetails.setTokenExpTime(rs.getTimestamp("TOKENEXPTIME"));
			tokenDetails.setUserLoggedin(rs.getString("ISUSERLOGGEDIN"));
		}
		else{
			System.out.println("User token Details does not exist in database....");
			return null;
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
		return tokenDetails;
	}
	@Override
	public UserTokenDetails getTokenDetails(String token) {
		UserTokenDetails tokenDetails = new UserTokenDetails();
		Connection connection = null;
	    try {
		System.out.println("inside UserTokenDetailsDAOImpl getUserTokenDetails() method");
		//getting database connection object
		connection = getConnection();
		String sql = "SELECT * FROM `usertokendetails` WHERE token = ? ";
		
		PreparedStatement ps= connection.prepareStatement(sql);
		ps.setString(1, token);
		
		ResultSet rs =ps.executeQuery();
		if (rs.next()) {
			System.out.println("User token Details exist in database....");
			tokenDetails.setUsername(rs.getString("USERNAME"));
			tokenDetails.setToken(rs.getString("TOKEN"));
			tokenDetails.setCreatedBy(rs.getString("CREATEDBY"));
			tokenDetails.setCreatedDate(rs.getTimestamp("CREATEDDATE"));
			tokenDetails.setModifiedBy(rs.getString("MODIFIEDBY"));
			tokenDetails.setModifiedDate(rs.getTimestamp("MODIFIEDDATE"));
			tokenDetails.setTokenExpTime(rs.getTimestamp("TOKENEXPTIME"));
			tokenDetails.setUserLoggedin(rs.getString("ISUSERLOGGEDIN"));
		}
		else{
			System.out.println("User token Details does not exist in database....");
			return null;
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
		return tokenDetails;
	}
	@Override
	public String validateHeaderDetails(HttpHeaders headers) {
			String result =null;
		try {
			if(headers == null)
			{
				result="access denied/invalid request";
			return result;
			}
			if (headers != null && headers.getRequestHeaders().containsKey("Authorization")) {
			    List<String> authorization = headers.getRequestHeader("Authorization");
			    for (String authHeaders : authorization) {
			    if (authHeaders != null) {
				 // Authorization: Bearer
				System.out.println("authHeaders :" +authHeaders);
				if ((authHeaders.startsWith("Bearer"))) {
			        String bearerCredentials = authHeaders.substring("Bearer".length()).trim();
			        return bearerCredentials;
						}
				else if ((authHeaders.startsWith("Basic"))) {
					String base64Credentials = authHeaders.substring("Basic".length()).trim();
					String credentials = new String(Base64.decodeBase64(base64Credentials), Charset.forName("UTF-8"));
					return credentials;
				}
					else{
					result="access denied/invalid request";
					return result;
						}
					}
				}

			}
			else{
				result="access denied/invalid request";
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}