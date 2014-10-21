package com.quizmania.client;

import java.util.Collection;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

import com.quizmania.repository.Game;

public interface GameSvcApi {
	
	public static final String TITLE_PARAMETER = "title";
	
	public static final String DURATION_PARAMETER = "duration";

	// The path where we expect the VideoSvc to live
	public static final String GAME_SVC_PATH = "/game";
	//public static final String GAME_SVC_PATH_ADD = GAME_SVC_PATH +"/add";

	// The path to search videos by title
	//public static final String VIDEO_TITLE_SEARCH_PATH = VIDEO_SVC_PATH + "/search/findByName";
	
	// The path to search videos by title
	//public static final String VIDEO_DURATION_SEARCH_PATH = VIDEO_SVC_PATH + "/search/findByDurationLessThan";
	
	//
	public static final String LOGIN_PAGE="/login";
	//
	public static final String LOGOUT_PAGE="/logout";

	@GET(GAME_SVC_PATH)
	public Collection<Game> getListofGames();
	
	@POST(value=GAME_SVC_PATH)
	public boolean addGameRecord( @RequestBody Game g);
	
}
