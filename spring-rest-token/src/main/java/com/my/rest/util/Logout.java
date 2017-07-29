package com.my.rest.util;

public class Logout {

	private String logoutMessage;

	public String getLogoutMessage() {
		return logoutMessage;
	}

	public void setLogoutMessage(String logoutMessage) {
		this.logoutMessage = logoutMessage;
	}
	
	@Override
	public String toString() {
		return "LogoutMessage [logoutMessage=" + logoutMessage + "]";
	}

}
