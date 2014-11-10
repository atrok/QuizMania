package com.quizmania.Dynamo;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.quizmania.DynamoDBConfiguration;
import com.quizmania.repository.User;
import com.quizmania.repository.UserRepository;

	
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DynamoDBConfiguration.class,UserRepository.class,})
public class TestDynamoUser {


	  @Autowired UserRepository repository;

	  @Test
	  public void sampleTestCase() {
	    User dave = new User("Andrey","Trok");
	    repository.save(dave);

	    User carter = new User("Carter","Matthews");
	    repository.save(carter);

	    Collection<User> result = repository.findByLastName("Matthews");
	   
	    assertEquals(result.size(), 1);
	    assertTrue("User Dave should be found", result.contains(dave));
	  }
}

