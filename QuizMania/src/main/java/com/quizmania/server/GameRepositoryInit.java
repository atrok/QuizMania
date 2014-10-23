package com.quizmania.server;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.quizmania.repository.GameRepository;
import com.quizmania.util.ConfigurationUtil;


public class GameRepositoryInit {
	
	@Autowired
	private static GameRepository games;
	private final static Logger logger=Logger.getLogger(GameRepositoryInit.class);
	
	//private final static  GameRepositoryInit init=new GameRepositoryInit();

	private GameRepositoryInit(){
	}
	
	public static void create(){
		try{
		if (games.count()==0)
			games.save(ConfigurationUtil.readWithCsvReader());
		else 
			logger.info("Games repository is already initialized");
		}catch(Exception e){
			logger.error("Failed to create repository with data");
			e.printStackTrace();
		}
		
	}
	
	/*
	 * for testing purposes only - we would need to create mock repository for configuration to get created.
	 */
	public static void addRepository(GameRepository g){
		
		games=g;
	}
}
