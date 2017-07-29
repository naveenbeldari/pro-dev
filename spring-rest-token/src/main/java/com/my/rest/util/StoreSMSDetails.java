package com.my.rest.util;

public class StoreSMSDetails {
	
	private String msgId;
	private String msgType;
	private Long recipientId;
	private String msgDesc;
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	public Long getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(Long recipientId) {
		this.recipientId = recipientId;
	}
	public String getMsgDesc() {
		return msgDesc;
	}
	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}
	
	@Override
	public String toString() {
		return "StoreSMSDetails [msgId=" + msgId + ", msgType=" + msgType + ", recipientId=" + recipientId + ", msgDesc=" + msgDesc + "]";
	}
	
}
