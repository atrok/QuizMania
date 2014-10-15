package com.quizmania.server;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.quizmania.repository.Game;
import com.quizmania.repository.GameRepository;

@Controller
public class GameController {

	@Autowired
	private GameRepository games;
	
	@RequestMapping(value="/games",method=RequestMethod.GET)
	public @ResponseBody Collection<Game> getListofGames(){
		return Lists.newArrayList(games.findAll());
	}
	
	
}
