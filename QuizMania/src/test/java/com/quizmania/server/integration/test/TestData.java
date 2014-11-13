package com.quizmania.server.integration.test;

import java.util.Random;
import java.util.UUID;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizmania.repository.Game;
import com.quizmania.repository.User;

/**
 * This is a utility class to aid in the construction of
 * Video objects with random names, urls, and durations.
 * The class also provides a facility to convert objects
 * into JSON using Jackson, which is the format that the
 * VideoSvc controller is going to expect data in for
 * integration testing.
 * 
 * @author jules
 *
 */
public class TestData {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	
	public static User randomUser() {
		// Information about the video
		// Construct a random identifier using Java's UUID class
		String password = UUID.randomUUID().toString();
		String email=UUID.randomUUID().toString()+"@"+UUID.randomUUID().toString()+".com";
		
		return new User(email,password);
	}

	public static Game randomGame() {
		// Information about the video
		// Construct a random identifier using Java's UUID class
		Random r=new Random(47);
		String var1=UUID.randomUUID().toString();
		String var2=UUID.randomUUID().toString();
		String var3=UUID.randomUUID().toString();
		String var4=UUID.randomUUID().toString();
		int answer=r.nextInt(3);
		String description=UUID.randomUUID().toString();
		int rate=r.nextInt(1);
		return new Game(var1,var2,var3,var4,answer,description,rate);
	}	
	/**
	 *  Convert an object to JSON using Jackson's ObjectMapper
	 *  
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object o) throws Exception{
		return objectMapper.writeValueAsString(o);
	}

	public static User randomUser(String uSERNAME, String pASSWORD) {
		// TODO Auto-generated method stub
		User u=new User(uSERNAME, pASSWORD);
		String[] i={"ROLE_ADMIN","ROLE_USER"};
		u.setRole(i);
		
		return u;
	}
}
