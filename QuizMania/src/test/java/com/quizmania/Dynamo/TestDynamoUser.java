package com.quizmania.Dynamo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.quizmania.DynamoDBConfiguration;
import com.quizmania.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DynamoDBConfiguration.class,
		UserRepository.class, })
public class TestDynamoUser {

	@Autowired
	UserRepository repository;

	@Test
	public void sampleTestCase() {
		User dave = new User("and.tr@aaa.com","none");
		repository.save(dave);

		User carter = new User("quiz@joke.com","none");
		
		repository.save(carter);

		User result = repository.findByEmail("quiz@joke.com");

		//assertEquals(result.size(), 1);
		assertTrue("User is not found", 
				result.equals(carter));
	}

	@Before
	public void cleanRepository() {

		// test reading
		Collection<User> result = repository.findAll();
		

		// test deletion of user
		Iterator<User> it = result.iterator();
		while (it.hasNext())
			repository.delete(it.next());
		assertEquals(repository.findAll().size(), 0);
	}
}
