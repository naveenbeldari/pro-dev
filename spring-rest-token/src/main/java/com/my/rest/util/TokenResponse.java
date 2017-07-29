package com.my.rest.util;

public class TokenResponse {
    
    	String token=null;
	String message=null;
	
	public String getToken() {
	    return token;
	}
	public void setToken(String token) {
	    this.token = token;
	}
	
	public String getMessage() {
	    return message;
	}
	public void setMessage(String message) {
	    this.message = message;
	}
	@Override
	public String toString() {
	    return "TokenResponse [Message=" + message + " , Token=" + token + "]";
		
	}
	
	
	

}
