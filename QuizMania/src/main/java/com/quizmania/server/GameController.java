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
import com.quizmania.client.Game;
import com.quizmania.client.GameSvcApi;
import com.quizmania.client.ScoreBoard;
import com.quizmania.repository.GameRepository;
import com.quizmania.repository.RepositoryFactory;
import com.quizmania.repository.ScoreBoardRepository;
import com.quizmania.util.GameRepositoryInit;

@Controller
public class GameController implements GameSvcApi{

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
		
		
        resp.setContentType("text/html");
        resp.sendError(404, "Game repository object is not initialized");
        
		return null;
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

	@Override
	public List<Game> getListOfGames() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}


		
}
