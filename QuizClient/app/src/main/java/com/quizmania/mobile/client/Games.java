package com.quizmania.mobile.client;

import com.quizmania.client.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atrok on 11/29/2014.
 */
public class Games {
    private List<Game> games=new ArrayList<Game>();
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
}
