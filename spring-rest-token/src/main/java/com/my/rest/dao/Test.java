/*package com.my.rest.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Test {

	public static void main(String[] args) throws SQLException {
		Connection conn=null;
		InputStream input = null;
		try {
			System.out.println("inside getConnection method");
			input = new FileInputStream("F:\\MyWorks\\spring-rest-token\\src\\main\\resources\\db.properties");
			Properties prop = new Properties();
			//input=this.getClass().getResourceAsStream("/sample.properties");
			prop.load(input);
			String className = prop.getProperty("className");
			String url = prop.getProperty("url");
			String username = prop.getProperty("username");
			String password = prop.getProperty("password");
			
			Class.forName(className);
			//getting database connection object
			conn = DriverManager.getConnection(url,username,password);
			System.out.println("conn: " +conn);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
*/