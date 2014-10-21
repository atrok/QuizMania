package com.quizmania.server;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import com.quizmania.repository.Game;

public class TestGameSvcApi {

	private final String QUIZ_FILE="quizes.csv";
	
	List<Game> quiz;
	@Test
	public void test() {

		GameController gamecontroller = new GameController();
		Game g=new Game();

		boolean ok = gamecontroller.addGameRecord(g);
		
		
		// Test the servlet directly, without going through the network.
		
		assertTrue(ok);
		
		List<Game> games = gamecontroller.getListofGames();
		assertTrue(games.contains(g));
	}
	
	private Game createGame(Game g){

	       // -define .csv file in app
 //       String fileNameDefined = QUIZ_FILE;
        // -File class needed to turn stringName to actual file
        File file = new File(QUIZ_FILE);

        try{
            // -read from filePooped with Scanner class
            Scanner inputStream = new Scanner(file);
            // hashNext() loops line-by-line
            while(inputStream.hasNext()){
                //read single line, put in string
                String data = inputStream.next();
                g.
                System.out.println(data + "***");

            }
            // after loop, close scanner
            inputStream.close();


        }catch (FileNotFoundException e){

            e.printStackTrace();
        }
	}

}
