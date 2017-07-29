package com.my.rest.dao;

import com.my.rest.util.StoreSMSDetails;
import com.my.rest.util.UserTokenDetails;

public interface SMSDetailsDAO {
	
	public boolean storeDetails(StoreSMSDetails ssd);
	public boolean deleteDetails(int msgId);
	public StoreSMSDetails getSMSDetails(int msgId);
	public boolean getMSGDetails(int msgId);
	public boolean getUserDetails(String username , String password);
	public boolean getUserTokenDetails(UserTokenDetails userTokenDetails);
	public boolean insertUserTokenDetails(UserTokenDetails userTokenDetails);
	

}
