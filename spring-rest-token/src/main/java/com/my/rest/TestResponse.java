package com.my.rest;

public class TestResponse {
    private String status;
    private String message;
    private int errorCode;
    private String deveolperMsg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getDeveolperMsg() {
        return deveolperMsg;
    }

    public void setDeveolperMsg(String deveolperMsg) {
        this.deveolperMsg = deveolperMsg;
    }
}
