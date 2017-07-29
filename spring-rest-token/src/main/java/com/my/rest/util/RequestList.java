package com.my.rest.util;

import java.util.ArrayList;
import java.util.List;

public class RequestList {
	private List<ChangeUserName> changeUserName = new ArrayList<ChangeUserName>();

	public List<ChangeUserName> getContacts() {
		return changeUserName;
	}

	public void setContacts(List<ChangeUserName> contacts) {
		this.changeUserName = contacts;
	}

	@Override
	public String toString() {
		return "RequestList [changeUserName=" + changeUserName + "]";
	}

}

