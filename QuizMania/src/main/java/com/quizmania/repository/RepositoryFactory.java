package com.quizmania.repository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryFactory {

	@Autowired
	private GameRepository games;
	
	@Autowired
	private ScoreBoardRepository scoreboard;
	@Autowired
	private static RepositoryFactory rf;//=new RepositoryFactory();
	
	//public RepositoryFactory getFactory(){return rf;}
	
	public static GameRepository getGameRepository(){
		
		if (null!=rf.games)		
			return rf.games;
		Logger.getLogger(RepositoryFactory.class).error("Game repository is Null in Repository Factory");
		return null;
	}
	public static ScoreBoardRepository getScoreBoardRepository(){
		
		return rf.scoreboard;

}
	
}
