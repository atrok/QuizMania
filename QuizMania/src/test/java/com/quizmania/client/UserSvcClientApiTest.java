package com.quizmania.client;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.Test;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;

import com.google.gson.JsonObject;
import com.quizmania.client.GameSvcApi;
import com.quizmania.client.SecuredRestBuilder;
import com.quizmania.client.UserSvcApi;
import com.quizmania.repository.User;
import com.quizmania.server.integration.test.TestData;
import com.quizmania.server.integration.test.UnsafeHttpsClient;

/**
 * 
 * This integration test sends a POST request to the VideoServlet to add a new
 * video and then sends a second GET request to check that the video showed up
 * in the list of videos. Actual network communication using HTTP is performed
 * with this test.
 * 
 * The test requires that the VideoSvc be running first (see the directions in
 * the README.md file for how to launch the Application).
 * 
 * To run this test, right-click on it in Eclipse and select
 * "Run As"->"JUnit Test"
 * 
 * Pay attention to how this test that actually uses HTTP and the test that just
 * directly makes method calls on a VideoSvc object are essentially identical.
 * All that changes is the setup of the videoService variable. Yes, this could
 * be refactored to eliminate code duplication...but the goal was to show how
 * much Retrofit simplifies interaction with our service!
 * 
 * @author jules
 *
 */
public class UserSvcClientApiTest {

	private Logger log=Logger.getLogger(UserSvcClientApiTest.class);
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
	
	private UserSvcApi userService = new RestAdapter.Builder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL)
			.setErrorHandler(error).build()
			.create(UserSvcApi.class);

	private UserSvcApi authUserService = new SecuredRestBuilder()
			.setLoginEndpoint(TEST_URL + GameSvcApi.TOKEN_PATH)
			.setUsername(USERNAME)
			.setPassword(PASSWORD)
			.setClientId(CLIENT_ID)
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(UserSvcApi.class);

	private UserSvcApi invalidClientVideoService = new SecuredRestBuilder()
			.setLoginEndpoint(TEST_URL + GameSvcApi.TOKEN_PATH)
			.setUsername(UUID.randomUUID().toString())
			.setPassword(UUID.randomUUID().toString())
			.setClientId(UUID.randomUUID().toString())
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(UserSvcApi.class);

	private User user = TestData.randomUser(USERNAME,PASSWORD);

	/**
	 * This test creates a Video, adds the Video to the VideoSvc, and then
	 * checks that the Video is included in the list when getVideoList() is
	 * called.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testVideoAddAndList() throws Exception {
		// Add the video
		try{
		userService.addUser(user);
		}catch(Exception e){
			
			e.printStackTrace();
		}
		List<User> users=authUserService.getUsersList();
		// We should get back boolean true in case user is added
		//assertTrue(ok);
		assertTrue("Failed to find newly added User "+user.toString(),users.contains(user));
		users.clear();
		//update User
		user.setFirstName("Andrey");
		boolean ok=authUserService.updateUser(user);
		assertTrue("Failed to update user "+user.toString(), ok);
		ok=authUserService.deleteUser(user);
		
		assertTrue("Failed to delete user "+user.toString(), ok);
		
		// check if we indeed deleted it
		
		users=userService.getUsersList();
		// We should get back boolean true in case user is added
		assertTrue(ok);
		assertFalse("Found user supposedly delete User "+user.toString(),users.contains(user));
		
	}

	/**
	 * This test ensures that clients with invalid credentials cannot get
	 * access to videos.
	 * 
	 * @throws Exception
	
	@Test
	public void testAccessDeniedWithIncorrectCredentials() throws Exception {

		try {
			// Add the video
			invalidClientVideoService.addVideo(video);

			fail("The server should have prevented the client from adding a video"
					+ " because it presented invalid client/user credentials");
		} catch (RetrofitError e) {
			assert (e.getCause() instanceof SecuredRestException);
		}
	}
	
	/**
	 * This test ensures that read-only clients can access the video list
	 * but not add new videos.
	 * 
	 * @throws Exception
	
	@Test
	public void testReadOnlyClientAccess() throws Exception {

		Collection<Video> videos = readOnlyVideoService.getVideoList();
		assertNotNull(videos);
		
		try {
			// Add the video
			readOnlyVideoService.addVideo(video);

			fail("The server should have prevented the client from adding a video"
					+ " because it is using a read-only client ID");
		} catch (RetrofitError e) {
			JsonObject body = (JsonObject)e.getBodyAs(JsonObject.class);
			assertEquals("insufficient_scope", body.get("error").getAsString());
		}
	}
*/

}
