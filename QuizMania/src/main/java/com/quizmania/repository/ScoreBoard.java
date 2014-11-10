package com.quizmania.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Scoreboards")
public class ScoreBoard {

	private String boardId;
	private String userId;
	private String gameId;
	private int GameWin=0;
	private int GameLoss=0;
	
	//private int GameGuess=0;
	public ScoreBoard(String userId, int GameWin, int GameLoss, String gameId){
		this.userId=userId;
		this.gameId=gameId;
		this.GameWin=GameWin;
		this.GameLoss=GameLoss;
	}
	
	@DynamoDBHashKey
	@DynamoDBAutoGeneratedKey 
	public String getboardId(){
		return boardId;
	}
	
	@DynamoDBAttribute
	public int getGameWin() {
		return GameWin;
	}

	public void setGameWin(int gameWin) {
		GameWin = gameWin;
	}

	@DynamoDBAttribute
	public int getGameLoss() {
		return GameLoss;
	}
	
	public void setGameLoss(int gameLoss) {
		GameLoss = gameLoss;
	}

	@DynamoDBAttribute
	public String getUserId() {
		return userId;
	}
	
	@DynamoDBAttribute
	public String getGameId() {
		return gameId;
	}
	
}