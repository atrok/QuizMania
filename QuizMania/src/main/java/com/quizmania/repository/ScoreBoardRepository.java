package com.quizmania.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quizmania.client.ScoreBoard;

@Repository
public interface ScoreBoardRepository extends CrudRepository<ScoreBoard, Long>{

	// Find all videos with a matching title (e.g., Video.name)
	public Collection<ScoreBoard> findByUserId(String userId);
	public Collection<ScoreBoard> findByGameId(String gameId);
	public Collection<ScoreBoard> findByUserIdAndGameId(String userId, String gameId);
	
}

