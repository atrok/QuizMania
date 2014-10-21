package com.quizmania.server;


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

@Controller
public class GameController implements GameSvcApi{

	@Autowired
	private GameRepository games;
	
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
		
}
