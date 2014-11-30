package com.quizmania.client;


public class ScoreBoard {

	private String boardId;
	private String userId;
	private String gameId;
	private int gameWin=0;
	private int gameLoss=0;
	
	//private int GameGuess=0;
	public ScoreBoard(){}
	
	public ScoreBoard(String userId, int GameWin, int GameLoss, String gameId){
		this.userId=userId;
		this.gameId=gameId;
		this.gameWin=GameWin;
		this.gameLoss=GameLoss;
	}
	public String getboardId(){
		return boardId;
	}
	


	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public void setboardId(String boardId) {
		this.boardId = boardId;
	}

	public int getGameWin() {
		return gameWin;
	}

	public void setGameWin(int gameWin) {
		this.gameWin = gameWin;
	}

	public int getGameLoss() {
		return gameLoss;
	}
	
	public void setGameLoss(int gameLoss) {
		this.gameLoss = gameLoss;
	}

	public String getUserId() {
		return userId;
	}
	
	public String getGameId() {
		return gameId;
	}
	
}
