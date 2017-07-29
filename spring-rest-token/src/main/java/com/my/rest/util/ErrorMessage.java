package com.my.rest.util;

public class ErrorMessage {
    
	String errorMessage;
	
	public String getErrorMessage() {
	    return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
	    this.errorMessage = errorMessage;
	}
	
	@Override
	public String toString() {
		return "ErrorMessage [errorMessage=" + errorMessage + "]";
	}

}
