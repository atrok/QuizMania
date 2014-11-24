package com.quizmania.client;

import java.io.IOException;
import java.util.List;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;



public interface UserSvcApi {
	public static final String USERNAME="username";
	public static final String FIRSTNAME="firstname";
	public static final String LASTNAME="lastname";
	public static final String EMAIL="email";
	public static final String PASSWORD="password";
	public static final String USERID = "userid";	
	
	public static final String USER_SVC_PATH = "/user";


	@GET(USER_SVC_PATH)
	public List<User> getUsersList() throws IOException;
	
	//@ExceptionHandler(UserException.class)
	@POST(value=USER_SVC_PATH)
	public boolean addUser( @Body User g); //throws UserException;
	
	@POST(value=USER_SVC_PATH+"/update")
	public boolean updateUser( @Body User g);
	
	@POST(value=USER_SVC_PATH+"/delete")
	public boolean deleteUser( @Body User g);

}