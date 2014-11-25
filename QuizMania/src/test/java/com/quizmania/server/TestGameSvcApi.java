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
	GameRepositoryInit rep=new GameRepositoryInit();
	
	@Mock
	GameRepository games;
	
	@InjectMocks
	private GameController gamecontroller;
	
	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);
		// adding Mock repository
		

		
	}
	
	@Test
	public void test() {

		rep.addRepository(games);
		gamecontroller.addRepository(games);
		
			try{
				rep.create();
			}catch(Exception e){
				logger.error("Can't create new repository");
				e.printStackTrace();
				throw e;
			}
			
		
		g.setId(ConfigurationUtil.getUUID());
		boolean ok = gamecontroller.addGameRecord(g);

		// Test the servlet directly, without going through the network.

		assertTrue(ok);

		if (null!=games){
			Iterable<Game> res= games.findAll();
					List<Game> gameslist = Lists.newArrayList(res);
		assertTrue(gameslist.contains(g));
		}
	}

}