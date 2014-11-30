package com.quizmania.client;

import java.io.IOException;
import java.util.List;
import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;



public interface GameSvcApi {
	
	public static final int QTY_QUESTIONS=10;
	
	public static final String TITLE_PARAMETER = "title";
	
	public static final String DURATION_PARAMETER = "duration";

	// The path where we expect the VideoSvc to live
	public static final String GAME_SVC_PATH = "/games";
	
	public static final String GAME_POPULATE_PATH=GAME_SVC_PATH+"/populate";

	public static final String TOKEN_PATH = "/oauth/token";
	//
	public static final String LOGIN_PAGE="/login";
	//
	public static final String LOGOUT_PAGE="/logout";

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
	
	@GET(value=USER_SVC_PATH+"/{userId}/login")
	public List<ScoreBoard> login(@Path("userId") String userId);
	
	@GET(GAME_SVC_PATH)
	public List<Game> getListOfGames() throws IOException;
	
	@POST(value=GAME_SVC_PATH)
	public boolean addGameRecord( @Body Game g);
	
	@PUT(value=GAME_SVC_PATH)
	public boolean updateGameRecord( @Body Game g);
	
	
	@GET(GAME_SVC_PATH+"/{gameId}/scoreboard/")
	public List<ScoreBoard> getlistofResultsPerGame(@Path("gameId") String gameId);
	
	@GET(USER_SVC_PATH+"/{userId}/scoreboard")
	public List<ScoreBoard> getlistofResultsPerUser(@Path("userId") String userId);
	
	@GET(GAME_SVC_PATH+"/scoreboard/")
	public List<ScoreBoard> getlistofCombinedResults(@Query("gameId") String gameId, @Query("userId") String userId);
	
	@GET(GAME_POPULATE_PATH)
	public boolean populate() throws IOException;
	
	@GET(value="/games/scoreboard")
	public List<ScoreBoard> getlistofResults() throws IOException;
	
	
}
