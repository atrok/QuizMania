package com.quizmania.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class RepositoryFactory {

	@Autowired
	private GameRepository games;
	
	@Autowired
	private ScoreBoardRepository scoreboard;
	
	private static RepositoryFactory rf=new RepositoryFactory();
	
	//public RepositoryFactory getFactory(){return rf;}
	
	public static GameRepository getGameRepository(){
		
			return rf.games;

	}
	public static ScoreBoardRepository getScoreBoardRepository(){
		
		return rf.scoreboard;

}
	
}
