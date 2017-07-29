package com.my.rest.dao;

import javax.ws.rs.core.HttpHeaders;

import com.my.rest.util.UserTokenDetails;

public interface UserTokenDetailsDAO {

    public boolean getUserDetails(String username , String password);
	public boolean getUserTokenDetails(UserTokenDetails userTokenDetails);
	public UserTokenDetails getTokenDetails(UserTokenDetails userTokenDetails);
	public UserTokenDetails getTokenDetails(String token);
	public boolean updateUserTokenDetails(UserTokenDetails userTokenDetails);
	public String validateHeaderDetails(HttpHeaders headers);
	public boolean validateTokenDetails(String token);
	public boolean insertUserTokenDetails(UserTokenDetails userTokenDetails);
}
