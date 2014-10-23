package com.quizmania.server;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.quizmania.repository.Game;
import com.quizmania.repository.GameRepository;
import com.quizmania.util.ConfigurationUtil;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestGameSvcApi {

	private final static Logger logger=Logger.getLogger(TestGameSvcApi.class);
	List<Game> quiz = null;
	Set<String> SetOfUUID = null;
	
	@Mock
	GameRepository games;
	
	@InjectMocks
	private GameController gamecontroller;
	
	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);
		// adding Mock repository
		GameRepositoryInit.addRepository(games);
		
		
			try{
				GameRepositoryInit.create();
			}catch(Exception e){
				logger.error("Can't create new repository");
				e.printStackTrace();
				throw e;
			}
		
	}
	
	@Test
	public void test() {

		
		Game g = Game.getBuilder("test", "test2", "test3", "test4", 2, "test quiz: answer 2", 0).build();

		boolean ok = gamecontroller.addGameRecord(g);

		// Test the servlet directly, without going through the network.

		assertTrue(ok);

		List<Game> games = gamecontroller.getListofGames();
		assertTrue(games.contains(g));
	}



	private String getUUID() {
		String uuid = "";
		do {

			uuid = UUID.randomUUID().toString();

		} while (!SetOfUUID.add(uuid));
		return uuid;
	}


}