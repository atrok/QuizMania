package com.quizmania.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game, Long>{

	// Find all videos with a matching title (e.g., Video.name)
	public Collection<Game> findByGameId(String gameId);
	
}
