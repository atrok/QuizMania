package com.quizmania.repository;

import java.util.Collection;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@EnableScan
//@Repository
public interface UserRepository extends CrudRepository<User, Long>{

	// Find all videos with a matching title (e.g., Video.name)
	public Collection<User> findByUserId(String userId);
	public Collection<User> findByUserName(String userName);
	public Collection<User> findByFirstName(String firstName);
	public Collection<User> findByLastName(String string);
	
}

