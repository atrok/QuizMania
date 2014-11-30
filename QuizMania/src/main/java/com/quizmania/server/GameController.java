package com.quizmania.server;


import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import retrofit.http.Body;
import retrofit.http.POST;

import com.google.common.collect.Lists;
import com.quizmania.client.Game;
import com.quizmania.client.GameSvcApi;
import com.quizmania.client.ScoreBoard;
import com.quizmania.client.User;
import com.quizmania.repository.GameRepository;
import com.quizmania.repository.RepositoryFactory;
import com.quizmania.repository.ScoreBoardRepository;
import com.quizmania.repository.UserRepository;
import com.quizmania.util.GameRepositoryInit;

@Controller
public class GameController {

	private final static Logger logger = Logger.getLogger(GameController.class);
	
	@Autowired
	private GameRepository games;
	
	@Autowired
	private ScoreBoardRepository scoreboards;
	

	
	@RequestMapping(value="/games",method=RequestMethod.GET)
	public @ResponseBody List<Game> getListOfGames(HttpServletResponse resp) throws IOException{
		//games=RepositoryFactory.getGameRepository();
		
		if (null!=games)
				return Lists.newArrayList(
						games.findAll()
						);
		
		
        sendHTTPError(resp, 404, "Game repository object is not initialized");
        
		return null;
	}
	
	@RequestMapping(value = "/user/{name}/login", method=RequestMethod.POST)
	public @ResponseBody List<ScoreBoard> login(@PathVariable("name") String name){
		
		
		return Lists.newArrayList(scoreboards.findByUserId(name));
		
	}
	
	@RequestMapping(value = "/games/populate", method = RequestMethod.GET)
	public @ResponseBody boolean populate() throws IOException {
		// games=RepositoryFactory.getGameRepository();
		try{
		

			GameRepositoryInit rep = new GameRepositoryInit();
			rep.addRepository(games); 
			
			if (rep.create()) {

				List<Game> res = rep.getAll();

				if (null == res) {
					//logger.error("Gamerepository is empty");
					throw new IOException("Gamerepository is empty");
				}
				return true;
			}
			return false;

		} catch(Exception e) {

			logger.error("Can't populate GameRepository, the reason is"+e.getCause());
			/*
			 * resp.setContentType("text/html"); resp.sendError(404,
			 * "Game repository object is not initialized");
			 */

			throw new IOException(e);
		}
		
	}
	
	
	@RequestMapping(value="/games",method=RequestMethod.POST)
	public @ResponseBody boolean addGameRecord( @RequestBody Game g)
	{
		Game t=games.save(g);
		return (g==t);
	}

	@RequestMapping(value="/games",method=RequestMethod.PUT)
	public @ResponseBody boolean updateGameRecord( @RequestBody Game g)
	{
		return addGameRecord(g);
	}
	
	public Collection<ScoreBoard> getlistofResultsPerGame(String gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value=GameSvcApi.USER_SVC_PATH+"/{userId}/scoreboard",method=RequestMethod.GET)
	public @ResponseBody List<ScoreBoard> getlistofResultsPerUser(@PathVariable("userId") String userId, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		List<ScoreBoard> res=Lists.newArrayList(scoreboards.findByUserId(userId));
		if (res.size()==0){// we check list of results of user and if there is no records per user we create empty one for future references
			ScoreBoard sb=scoreboards.save(new ScoreBoard(userId,0,0,""));
			res.add(sb);
		}
		return res;

	}

	public Collection<ScoreBoard> getlistofCombinedResults(String gameId,
			String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value="/games/scoreboard",method=RequestMethod.GET)
	public @ResponseBody Collection<ScoreBoard> getlistofResults(HttpServletResponse resp) throws IOException {
		if (null!=scoreboards)
			return Lists.newArrayList(
					scoreboards.findAll()
					);
	
	
    resp.setContentType("text/html");
    resp.sendError(404, "Game repository object is not initialized");
    
	return null;	
	
	}
	/*
	 * for testing purposes only
	 */
	
	public void addRepository(GameRepository games){
		this.games=games;
	}


	private void sendHTTPError(HttpServletResponse resp, int errcode, String msg) throws IOException{
        resp.setContentType("text/html");
        resp.sendError(errcode, msg);
	}
		
}
