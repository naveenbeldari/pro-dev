package com.my.rest.util;

public class Track {

	String title;
	String user;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	

	@Override
	public String toString() {
		return "Track [title=" + title + ", User=" + user + "]";
	}

}
