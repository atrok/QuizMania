package com.quizmania.mobile.client;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.quizmania.client.Game;
import com.quizmania.client.GameSvcApi;
import com.quizmania.client.ScoreBoard;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;


public class QuizActivity extends Activity implements QuizHolderFragment.OnSelectedListener,QuizHolderFragment.OnRatingChangeListener {


    List<Game> games=new ArrayList<Game>();
    String category;
    Game game;

    private static int curr_game_loss=0;
    private static int curr_game_win=0;
    private static int curr_game_count=0; // number of current game;
    private final static int GAME_LOSS_COUNT=3;


    Random random=new Random(47);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        ((TextView)findViewById(R.id.username)).setText(UserDTO.getUser().getUserName());
        ((TextView)findViewById(R.id.wintxt))
                .setText(Integer.toString(curr_game_win));
        ((TextView)findViewById(R.id.losstxt))
                .setText(Integer.toString(curr_game_loss));



        category=getIntent().getStringExtra("category");

        for (Game g: Games.init().getGames()){
            if (g.getType().equals(category))
                games.add(g);
        };

        if (findViewById(R.id.quizEmbed) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            game=getRandomGame();
            QuizHolderFragment quiz=QuizHolderFragment.newInstance(game);

            // Execute a transaction, replacing any existing fragment
            // with this one inside the frame.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.quizEmbed, quiz).commit();

        }
    }

    private void runGame(){

        game=getRandomGame();

        if (curr_game_loss==GAME_LOSS_COUNT){
            Toast.makeText(QuizActivity.this,
                    "You lost with "+curr_game_win+" questions out of "+curr_game_count,
                    Toast.LENGTH_LONG).show();
            return;
            /*
            todo update Scoreboard with results;
             */
        }
        if(null==game) {
            Toast.makeText(QuizActivity.this,
                    "You answered "+curr_game_win+" questions out of "+curr_game_count,
                    Toast.LENGTH_LONG).show();
            /*
            todo update ScoreBoard with results
             */
            return;
        }


            // Check what fragment is currently shown, replace if needed.
            QuizHolderFragment quiz = QuizHolderFragment.newInstance(game);

            // Execute a transaction, replacing any existing fragment
            // with this one inside the frame.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.quizEmbed, quiz);
            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();


    }

    public void onSelected(boolean success){

        if (!success){
            UserDTO.getSb().setGameLoss(curr_game_loss++);
            ((TextView)findViewById(R.id.losstxt)).setText(Integer.toString(curr_game_loss));
            Toast.makeText(
                    this,
                    "It's incorrect answer, ",
                    Toast.LENGTH_LONG).show();

        }else{
            UserDTO.getSb().setGameWin(curr_game_win++);
            ((TextView)findViewById(R.id.wintxt)).setText(Integer.toString(curr_game_win));

            Toast.makeText(
                    this,
                    "It's correct answer! Rate this question to move on",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void onRatingChange(float rating){
        /* todo
        save rating

         */

        game.setRate((int)rating);

        final GameSvcApi svc = GameSvc.getOrShowLogin(this);

        if (svc != null) {
            CallableTask.invoke(new Callable<Result>() {

                @Override
                public Result call() throws Exception {
                    return new Result(svc.updateGameRecord(game));
                }
            }, new TaskCallback<Result>() {

                @Override
                public void success(Result result) {

                    /*
                    nothing to do, we are glad that we succeeded in adding the game rating
                     */

                }

                @Override
                public void error(Exception e) {
                    Toast.makeText(
                            QuizActivity.this,
                            "Unable to update game rating.",
                            Toast.LENGTH_SHORT).show();

                    /*
                    no need to start any new activities
                    startActivity(new Intent(GameListActivity.this,
                            LoginScreenActivity.class));

                     */
                }
            });
        }

        runGame();

    }

    /*
    we need this class to disguise boolean values returned by GameSvc - CallbackTask doesn't accept primitive values;
     */



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private Game getRandomGame(){
        if(games.size()!=0)
            return games.remove(random.nextInt(games.size()));
        return null;
    }

    private void updateScoreboard(){
        ScoreBoard sb=UserDTO.getSb();

    }

}


class Result{
    boolean result;

    public Result(boolean res){
        result=res;
    }

    public void set(boolean res){
        result=res;
    }

    public boolean isTrue(){
        return result;
    }
}