package com.quizmania.server;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.quizmania.client.Game;
import com.quizmania.repository.GameRepository;
import com.quizmania.util.ConfigurationUtil;
import com.quizmania.util.GameRepositoryInit;

public class TestGameSvcApi {

	private final static Logger logger=Logger.getLogger(TestGameSvcApi.class);
	List<Game> quiz = null;
	Set<String> SetOfUUID = new HashSet<String>();
	
	@Mock
	GameRepository games;
	
	@InjectMocks
	private GameController gamecontroller;
	
	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);
		// adding Mock repository
		GameRepositoryInit.addRepository(games);
		gamecontroller.addRepository(games);
		
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

		
		Game g = new Game("test", "test2", "test3", "test4", 2, "test quiz: answer 2", 0);
		g.setId(ConfigurationUtil.getUUID());
		boolean ok = gamecontroller.addGameRecord(g);

		// Test the servlet directly, without going through the network.

		assertTrue(ok);

		List<Game> gameslist = Lists.newArrayList(games.findAll());
		assertTrue(gameslist.contains(g));
	}

}