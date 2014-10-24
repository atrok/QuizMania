package com.quizmania.server;


import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.quizmania.client.GameSvcApi;
import com.quizmania.repository.Game;
import com.quizmania.repository.GameRepository;
import com.quizmania.repository.RepositoryFactory;
import com.quizmania.repository.ScoreBoard;
import com.quizmania.repository.ScoreBoardRepository;

@Controller
public class GameController implements GameSvcApi{

	private final static Logger logger = Logger.getLogger(GameController.class);
	
	private GameRepository games;
	
	@Autowired
	private ScoreBoardRepository scoreboards;
	
	@RequestMapping(value="/games",method=RequestMethod.GET)
	public @ResponseBody List<Game> getListOfGames(HttpServletResponse resp) throws IOException{
		games=RepositoryFactory.getGameRepository();
		
		if (null!=games)
				return Lists.newArrayList(
						games.findAll()
						);
		
		
        resp.setContentType("text/html");
        resp.sendError(404, "Game repository object is not initialized");
        
		return null;
	}
	
	@RequestMapping(value="/games/populate",method=RequestMethod.GET)
	public @ResponseBody List<Game> populateGames(HttpServletResponse resp) throws IOException{
		games=RepositoryFactory.getGameRepository();
		
		GameRepositoryInit.addRepository(games);
		GameRepositoryInit.create();
		if (null!=games)
			return Lists.newArrayList(
					games.findAll()
					);
		
		logger.error("Gamerepository is null");
		
        resp.setContentType("text/html");
        resp.sendError(404, "Game repository object is not initialized");
        
		return null;
	}
	
	
	@RequestMapping(value="/games/add",method=RequestMethod.POST)
	public @ResponseBody boolean addGameRecord( @RequestBody Game g)
	{
		Game t=games.save(g);
		return (g!=t);
	}

	@Override
	public Collection<ScoreBoard> getlistofResultsPerGame(String gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ScoreBoard> getlistofResultsPerUser(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ScoreBoard> getlistofCombinedResults(String gameId,
			String userId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * for testing purposes only
	 */
	
	public void addRepository(GameRepository games){
		this.games=games;
	}
		
}
