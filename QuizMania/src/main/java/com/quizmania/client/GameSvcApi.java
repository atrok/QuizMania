package com.quizmania.client;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

import com.quizmania.repository.Game;
import com.quizmania.repository.ScoreBoard;

public interface GameSvcApi {
	
	public static final int QTY_QUESTIONS=10;
	
	public static final String TITLE_PARAMETER = "title";
	
	public static final String DURATION_PARAMETER = "duration";

	// The path where we expect the VideoSvc to live
	public static final String GAME_SVC_PATH = "/game";

	public static final String TOKEN_PATH = "/oauth/token";
	//
	public static final String LOGIN_PAGE="/login";
	//
	public static final String LOGOUT_PAGE="/logout";

	
	@GET(GAME_SVC_PATH)
	public List<Game> getListOfGames() throws IOException;
	
	@POST(value=GAME_SVC_PATH)
	public boolean addGameRecord( @Body Game g);
	
	@GET(GAME_SVC_PATH+"/{gameId}/scoreboard/")
	public Collection<ScoreBoard> getlistofResultsPerGame(@Path("gameId") String gameId);
	
	@GET(GAME_SVC_PATH+"/{userId}/scoreboard/")
	public Collection<ScoreBoard> getlistofResultsPerUser(@Path("userId") String userId);
	
	@GET(GAME_SVC_PATH+"/scoreboard/")
	public Collection<ScoreBoard> getlistofCombinedResults(@Query("gameId") String gameId, @Query("userId") String userId);
	
	
	
}
