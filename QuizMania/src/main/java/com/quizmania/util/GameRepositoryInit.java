package com.quizmania.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.quizmania.repository.GameRepository;


public class GameRepositoryInit {
	
	@Autowired
	private GameRepository games;
	
	private final static Logger logger=Logger.getLogger(GameRepositoryInit.class);
	
	//private final static  GameRepositoryInit init=new GameRepositoryInit();

		
	public void create(){
		try{
			if(games.count()==0)
				games.save(ConfigurationUtil.readWithCsvReader());
			logger.info("Games repository is populated");
		}catch(Exception e){
			logger.error("Failed to create repository with data");
			e.printStackTrace();
		}
		
	}
	
	/*
	 * for testing purposes only - we would need to create mock repository for configuration to get created.
	 */
	public void addRepository(GameRepository g){
		
		games=g;
	}
}
