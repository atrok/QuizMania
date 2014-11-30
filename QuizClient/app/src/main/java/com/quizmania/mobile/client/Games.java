package com.quizmania.mobile.client;

import com.quizmania.client.Game;
import com.quizmania.client.ScoreBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atrok on 11/29/2014.
 */
public class Games {
    private List<Game> games=new ArrayList<Game>();
    private List<ScoreBoard> scoreboards=new ArrayList<ScoreBoard>();
    private static Games gamesObj;

    public static Games init(){
              if (gamesObj == null) {
            gamesObj = new Games();
        }
        return gamesObj;
    }

    public void addGames(List<Game> g){
        games=g;
    }
    public List<Game> getGames(){return games;}

    public void addScoreboards(List<ScoreBoard> sb){scoreboards=sb;}
    public List<ScoreBoard> getScoreboards(){return scoreboards;}
}
