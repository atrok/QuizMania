package com.quizmania.repository;

import java.util.Collection;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import com.quizmania.client.UserSvcApi;

@RepositoryRestResource(path = UserSvcApi.USER_SVC_PATH)
@EnableScan
public interface UserRepository extends CrudRepository<User, String>{

	// Find all videos with a matching title (e.g., Video.name)
	public Collection<User> findById(
			@Param(UserSvcApi.USERID) String userId);
	
	public Collection<User> findByUserName(
			@Param(UserSvcApi.USERNAME) String userName);
	public Collection<User> findByFirstName(
			@Param(UserSvcApi.FIRSTNAME)String firstName);
	public Collection<User> findByLastName(
			@Param(UserSvcApi.LASTNAME)String string);
	public Collection<User> findAll();
	public User findByEmail(
			@Param(UserSvcApi.EMAIL)String username);
	
	
}

