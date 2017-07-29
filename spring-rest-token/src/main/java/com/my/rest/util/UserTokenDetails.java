package com.my.rest.util;

import java.sql.Timestamp;

public class UserTokenDetails {
    
    private String username;
	private String token;
	private String createdBy;
	private Timestamp createdDate;
	private String modifiedBy;
	private Timestamp modifiedDate;
	private Timestamp tokenExpTime;
	private String userLoggedin;
	
	
	public String getUsername() {
	    return username;
	}
	public void setUsername(String username) {
	    this.username = username;
	}
	public String getToken() {
	    return token;
	}
	public void setToken(String token) {
	    this.token = token;
	}
	public String getCreatedBy() {
	    return createdBy;
	}
	public void setCreatedBy(String createdBy) {
	    this.createdBy = createdBy;
	}
	public Timestamp getCreatedDate() {
	    return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
	    this.createdDate = createdDate;
	}
	public String getModifiedBy() {
	    return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
	    this.modifiedBy = modifiedBy;
	}
	public Timestamp getModifiedDate() {
	    return modifiedDate;
	}
	public void setModifiedDate(Timestamp modifiedDate) {
	    this.modifiedDate = modifiedDate;
	}
	public Timestamp getTokenExpTime() {
	    return tokenExpTime;
	}
	public void setTokenExpTime(Timestamp tokenExpTime) {
	    this.tokenExpTime = tokenExpTime;
	}
	public String getUserLoggedin() {
		return userLoggedin;
	}
	public void setUserLoggedin(String userLoggedin) {
		this.userLoggedin = userLoggedin;
	}
	
	
	

}
