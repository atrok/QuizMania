package com.quizmania.server;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
import com.quizmania.repository.ScoreBoard;
import com.quizmania.repository.ScoreBoardRepository;

@Controller
public class GameController implements GameSvcApi{

	@Autowired
	private GameRepository games;
	
	@Autowired
	private ScoreBoardRepository scoreboards;
	
	@RequestMapping(value="/games",method=RequestMethod.GET)
	public @ResponseBody List<Game> getListofGames(){
		return Lists.newArrayList(games.findAll());
	}
	
	@RequestMapping(value="/games/add",method=RequestMethod.POST)
	public @ResponseBody boolean addGameRecord( @RequestBody Game g)
	{
		Game t=games.save(g);
		return (g!=t);
	}

	@Override
	public Collection<Game> getListOfGames() {
		// TODO Auto-generated method stub
		Iterator<Game> it=games.findAll().iterator();
		
		Collection<Game> g=null;
		while (it.hasNext()){
			g.add(it.next());
		}
		
		return g;
			
			
		
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
		
}
