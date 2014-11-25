package com.quizmania.repository;

import java.util.Collection;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quizmania.client.Game;

@Repository
@EnableScan
public interface GameRepository extends CrudRepository<Game, Long>{

	// Find all videos with a matching title (e.g., Video.name)
	public Collection<Game> findByGameId(String gameId);
	
	
	
}
