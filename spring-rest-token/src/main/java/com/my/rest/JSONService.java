package com.my.rest;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.my.rest.dao.StoreSMSDetailsDAOImpl;
import com.my.rest.dao.UserTokenDetailsDAOImpl;
import com.my.rest.util.ErrorMessage;
import com.my.rest.util.Logout;
import com.my.rest.util.StoreSMSDetails;
import com.my.rest.util.TokenResponse;
import com.my.rest.util.UserTokenDetails;


/**
 * @author naveen.beldari
 *
 */
@Path("/NotificationServer")
public class JSONService {

	@GET
	@Path("/get")
	public Response validateUser(@Context HttpHeaders headers)
	{
		TokenResponse tokenResponse = new TokenResponse();
		ErrorMessage errorMessage = new ErrorMessage();
		boolean userValid = false;
		boolean userTokenUpdate = false;
		boolean userTokendetails = false;
		boolean userLoggedin = false;
		String headerDetails = null;
		UserTokenDetailsDAOImpl userDetailsDAO = null;
		UserTokenDetails userDetails = null;
		try
		{
				 userDetailsDAO = new UserTokenDetailsDAOImpl();
				 userDetails = new UserTokenDetails();
				 
				headerDetails = userDetailsDAO.validateHeaderDetails(headers);
				System.out.println("headerDetails :" +headerDetails);
				if (headerDetails!=null && !headerDetails.startsWith("access")) {
						// credentials = username:password
						final String[] values = headerDetails.split(":", 2);
						String username = values[0];
						String password = values[1];
						System.out.println("username : " + username);
						System.out.println("password : " + password);

						userValid = userDetailsDAO.getUserDetails(username, password);
						System.out.println("userValid : " + userValid);
						if (userValid)
						{
							userDetails.setUsername(username);
							userTokendetails = userDetailsDAO.getUserTokenDetails(userDetails);
							if (userTokendetails)
							{
								// generating token using SecureRandom class
								String token = getRandomNumber();
								System.out.println("token :" + token);

								Date createdDate = new Date();
								Timestamp createdDatetime = new Timestamp(createdDate.getTime());
								System.out.println("current time : " + createdDatetime);
								Calendar cal = Calendar.getInstance();
								cal.setTime(createdDate);
								cal.add(Calendar.MINUTE, 30);
								Date tokenExp = new Date();
								tokenExp = cal.getTime();
								Timestamp tokenExpTime = new Timestamp(tokenExp.getTime());
								System.out.println("tokenExpDate time : " + tokenExpTime);

								// updating token details with user
								userDetails.setUsername(username);
								userDetails.setToken(token);
								userDetails.setCreatedBy(username);
								userDetails.setCreatedDate(createdDatetime);
								userDetails.setModifiedBy(null);
								userDetails.setModifiedDate(null);
								userDetails.setTokenExpTime(tokenExpTime);
								userDetails.setUserLoggedin("Y");

								userTokenUpdate = userDetailsDAO.insertUserTokenDetails(userDetails);
								System.out.println("userTokenUpdate " + userTokenUpdate);
								
								tokenResponse.setMessage("Please use above token for further communicaton..");
								tokenResponse.setToken(token);

							}

							else
							{
								userDetails.setUsername(username);
								userDetails = userDetailsDAO.getTokenDetails(userDetails);
								if (userDetails!=null) {
									userDetails.setUserLoggedin("Y");
									userDetails.setModifiedBy(username);
									Date modDate = new Date();
									Timestamp modDatetime = new Timestamp(modDate.getTime());
									userDetails.setModifiedDate(modDatetime);
									Calendar cal = Calendar.getInstance();
									cal.setTime(modDate);
									cal.add(Calendar.MINUTE, 30);
									Date tokenExp = new Date();
									tokenExp = cal.getTime();
									Timestamp tokenModExpTime = new Timestamp(tokenExp.getTime());
									userDetails.setTokenExpTime(tokenModExpTime);
									userLoggedin = userDetailsDAO.updateUserTokenDetails(userDetails);
									
									if (userLoggedin) {
										errorMessage.setErrorMessage("Your generated token is already exist ");
										return Response.status(202).entity(errorMessage).build();
									}
									else{
										errorMessage.setErrorMessage("Your generated token is already exist ");
										return Response.status(202).entity(errorMessage).build();
									}
								}
							}
						}

						else
						{
							errorMessage.setErrorMessage("invalid userdetails");
							return Response.status(401).entity(errorMessage).build();
						}

						}
						
						else
						{
							errorMessage.setErrorMessage(headerDetails);
							return Response.status(401).entity(errorMessage).build();
						}
			}

		 catch(Exception e)
		{
			e.printStackTrace();
			errorMessage.setErrorMessage("access denied/invalid request");
			return Response.status(401).entity(errorMessage).build();
		}
		return Response.status(201).entity(tokenResponse).build();

	}

	public static String getRandomNumber() {
	    SecureRandom secureRandom = new SecureRandom();
	    return new BigInteger(130, secureRandom).toString(32);
	}
	
	@POST
	@Path("/getSMSDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSMSDetails(int msgId,@Context HttpHeaders headers) {
	    ErrorMessage errorMessage = new ErrorMessage();
		StoreSMSDetailsDAOImpl ssdao = new StoreSMSDetailsDAOImpl();
		StoreSMSDetails ssd = new StoreSMSDetails();
		boolean userTokendetails = false;
		boolean userLoggedin = false;
		String headerDetails = null;
		UserTokenDetailsDAOImpl userDetailsDAO = new UserTokenDetailsDAOImpl();
		UserTokenDetails userDetails = new UserTokenDetails();
	    try {
	    	headerDetails = userDetailsDAO.validateHeaderDetails(headers);
			System.out.println("headerDetails :" +headerDetails);
			if (headerDetails!=null && !headerDetails.startsWith("access")) {
		        userTokendetails = userDetailsDAO.validateTokenDetails(headerDetails);
		        if (userTokendetails) {
		        userDetails.setToken(headerDetails);
				userDetails = userDetailsDAO.getTokenDetails(headerDetails);
				if (userDetails!=null) {
					userDetails.setUserLoggedin("Y");
					userDetails.setModifiedBy(userDetails.getUsername());
					Date modDate = new Date();
					Timestamp modDatetime = new Timestamp(modDate.getTime());
					userDetails.setModifiedDate(modDatetime);
					Calendar cal = Calendar.getInstance();
					cal.setTime(modDate);
					cal.add(Calendar.MINUTE, 30);
					Date tokenExp = new Date();
					tokenExp = cal.getTime();
					Timestamp tokenModExpTime = new Timestamp(tokenExp.getTime());
					userDetails.setTokenExpTime(tokenModExpTime);
					userDetails.setToken(headerDetails);
					userLoggedin = userDetailsDAO.updateUserTokenDetails(userDetails);
		        if (userLoggedin) {
		            ssd = ssdao.getSMSDetails(msgId);
		            if(ssd!=null){
				    return Response.status(201).entity(ssd).build();
					}
				else{
					 errorMessage.setErrorMessage("No details found");
					 return Response.status(202).entity(errorMessage).build();
										}
									}

								}

							}
		        else{
		            errorMessage.setErrorMessage("invalid token/Your are not authorized person to avail these service");
				    return Response.status(202).entity(errorMessage).build();
		        	}
			
				}
			else{
				 errorMessage.setErrorMessage(headerDetails);
				 return Response.status(401).entity(errorMessage).build();
			    }
			
	}
		catch (Exception e) {
			e.printStackTrace();
			errorMessage.setErrorMessage("access denied/invalid request");
			return Response.status(401).entity(errorMessage).build();
		    }
		    return Response.status(201).entity(errorMessage).build();
	}
	
	@POST
	@Path("/logoutSession")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logoutSession(@Context HttpHeaders headers) {
	    ErrorMessage errorMessage = new ErrorMessage();
	    Logout logout = new Logout();
		boolean userTokendetails = false;
		boolean userLoggedout = false;
		String headerDetails = null;
		UserTokenDetailsDAOImpl userDetailsDAO = new UserTokenDetailsDAOImpl();
		UserTokenDetails userDetails = new UserTokenDetails();
	    try {
	    	headerDetails = userDetailsDAO.validateHeaderDetails(headers);
			System.out.println("headerDetails :" +headerDetails);
			if (headerDetails!=null && !headerDetails.startsWith("access")) {
		        userTokendetails = userDetailsDAO.validateTokenDetails(headerDetails);
		        userDetails = userDetailsDAO.getTokenDetails(headerDetails);
		        if (userTokendetails) {
		        	if (userDetails!=null) {
		        	userDetails.setUserLoggedin("N");
		        	userDetails.setModifiedBy(userDetails.getUsername());
					Date modDate = new Date();
					Timestamp modDatetime = new Timestamp(modDate.getTime());
					userDetails.setModifiedDate(modDatetime);
					userDetails.setTokenExpTime(userDetails.getTokenExpTime());
		        	userDetails.setToken(headerDetails);
		        	userLoggedout = userDetailsDAO.updateUserTokenDetails(userDetails);
		            if(userLoggedout){
		            	logout.setLogoutMessage("You are successfully logged out from session");
				    return Response.status(201).entity(logout).build();
					}
				else{
					 errorMessage.setErrorMessage("No details found ");
					 return Response.status(202).entity(errorMessage).build();
				}
			}
		        }
		        else{
		            errorMessage.setErrorMessage("invalid token/Your generated token has been expired");
				    return Response.status(202).entity(errorMessage).build();
		        }
		    }
		    else{
		    	errorMessage.setErrorMessage(headerDetails);
				 return Response.status(401).entity(errorMessage).build();
		    }
		    
		 }
		
	    catch (Exception e) {
		e.printStackTrace();
		errorMessage.setErrorMessage("access denied/invalid request");
		return Response.status(401).entity(errorMessage).build();
	    }
	    return Response.status(201).entity(errorMessage).build();
		
	}
	
	@POST
	@Path("/storeSMS")
	@Produces(MediaType.APPLICATION_JSON)
	public Response storeSMS(StoreSMSDetails ssd) {
		boolean res = false;
		String result = null;
		StoreSMSDetailsDAOImpl ssdao = new StoreSMSDetailsDAOImpl();
		res = ssdao.storeDetails(ssd);
		if(res){
		    return Response.status(201).entity(ssd).build();
		}
		else{
			 result = "Message Id "+ ssd.getMsgId() +" is alreay exist in database  ";
		}
		return Response.status(201).entity(result).build();
		
	}
	
	@DELETE
	@Path("/deleteSMS")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSMS(int msgId) {
		boolean res = false;
		String result = null;
		StoreSMSDetailsDAOImpl ssdao = new StoreSMSDetailsDAOImpl();
		res = ssdao.deleteDetails(msgId);
		if(res){
		 result = "Message ID " + msgId + " deleted successfully";
		}
		else{
		 result = "Message ID " + msgId + " not found";
		}
		return Response.status(201).entity(result).build();
		
	}
	
}
	    
	    