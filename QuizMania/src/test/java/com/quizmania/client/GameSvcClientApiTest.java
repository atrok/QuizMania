package com.quizmania.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;










import com.google.common.collect.Lists;
import com.quizmania.server.integration.test.UnsafeHttpsClient;

public class GameSvcClientApiTest {
	
	private Logger logger=Logger.getLogger(GameSvcClientApiTest.class);
	private final String USERNAME = "admin";
	private final String PASSWORD = "none";
	private final String CLIENT_ID = "mobile";
	private final String READ_ONLY_CLIENT_ID = "mobileReader";

	private final String TEST_URL = "http://localhost:8888";//  we use fiddler port to intercept http request intended for our application running on 8443
	private class ErrorRecorder implements ErrorHandler {

		private RetrofitError error;

		@Override
		public Throwable handleError(RetrofitError cause) {
			error = cause;
			return error.getCause();
		}

		public RetrofitError getError() {
			return error;
		}
	}
	
	private ErrorRecorder error=new ErrorRecorder();
	
	private GameSvcApi gamesService = new RestAdapter.Builder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL)
			.setErrorHandler(error).build()
			.create(GameSvcApi.class);

	private GameSvcApi authGameService = new SecuredRestBuilder()
			.setLoginEndpoint(TEST_URL + GameSvcApi.TOKEN_PATH)
			.setUsername(USERNAME)
			.setPassword(PASSWORD)
			.setClientId(CLIENT_ID)
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL)
			.setErrorHandler(error).build()
			.create(GameSvcApi.class);

	@Test
	public void testPopulateGameRepository() {

		try {
			authGameService.populate();

		} catch (Exception e) {
			e.printStackTrace();
			assert (e.getCause() instanceof SecuredRestException);

		}

		Game g = new Game("test", "test2", "test3", "test4", 2,
				"test quiz: answer 2", 0, 0, "test2", "Horror");

		boolean ok = authGameService.addGameRecord(g);

		assertTrue(ok);

			Iterable<Game> res;
			try {
				res = authGameService.getListOfGames();
				List<Game> gameslist = Lists.newArrayList(res);
				assertTrue(gameslist.contains(g));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		g.setRate(5);
		g.setLevel(10);
		ok = authGameService.updateGameRecord(g);

			assertTrue(ok);
			res=null;
			try {
				res = authGameService.getListOfGames();
				List<Game> gameslist = Lists.newArrayList(res);
				assertTrue(gameslist.contains(g));
				assertEquals(gameslist.get(gameslist.indexOf(g)).getLevel(),10);
				assertEquals(gameslist.get(gameslist.indexOf(g)).getRate(),5);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

	}
}
