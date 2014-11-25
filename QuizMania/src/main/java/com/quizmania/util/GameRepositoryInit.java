package com.quizmania.util;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.quizmania.client.Game;
import com.quizmania.repository.GameRepository;


public class GameRepositoryInit {
	
	
	private GameRepository games;
	
	private final static Logger logger=Logger.getLogger(GameRepositoryInit.class);
	
	// private final static GameRepositoryInit init=new GameRepositoryInit();

	public boolean create() throws Exception {
		try {
			if (games.count() == 0) {
				games.save(ConfigurationUtil.readWithCsvReader());
				return true;
			} else {
				//logger.info("Games repository is already populated");
				throw new IOException("Games repository is already populated");
			}
		} catch (Exception e) {
			logger.error("Failed to create repository with data");
			e.printStackTrace();
			throw e;
		}
	}
	
	public void addRepository(GameRepository g){
		
		games=g;
	}
	public GameRepository getRepository(){
		return games;
	}
	
	public List<Game> getAll(){
		
		return Lists.newArrayList(games.findAll());
				
	}
}
