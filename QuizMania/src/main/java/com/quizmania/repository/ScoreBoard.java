package com.quizmania.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ScoreBoard {

	@Id
	private String userId;
	
	//private String username="";
	
	private String gameId;
	private int GameWin=0;
	private int GameLoss=0;
	//private int GameGuess=0;
	public ScoreBoard(String userId, String gameId){
		this.userId=userId;
		this.gameId=gameId;
	}
	public int getGameWin() {
		return GameWin;
	}
	public void setGameWin(int gameWin) {
		GameWin = gameWin;
	}
	public int getGameLoss() {
		return GameLoss;
	}
	public void setGameLoss(int gameLoss) {
		GameLoss = gameLoss;
	}
	public String getUserId() {
		return userId;
	}
	public String getGameId() {
		return gameId;
	}
	
}
