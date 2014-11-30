package com.quizmania.mobile.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.quizmania.client.Game;
import com.quizmania.client.GameSvcApi;
import com.quizmania.client.ScoreBoard;
import com.quizmania.mobile.client.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ScoreBoardActivity extends Activity {

    @InjectView(R.id.scoreboardlist)
    protected ListView sbList_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        ButterKnife.inject(this);

        Button bt=(Button)findViewById(R.id.backCategoriesbutton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScoreBoardActivity.this,GameListActivity.class));
            }
        });

        getScoreboards();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_score_board, menu);
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

    private void getScoreboards(){

        final GameSvcApi svc = GameSvc.getOrShowLogin(this);

        if (svc != null) {
            CallableTask.invoke(new Callable<List<ScoreBoard>>() {

                @Override
                public List<ScoreBoard> call() throws Exception {
                    return svc.getlistofResults();
                }
            }, new TaskCallback<List<ScoreBoard>>() {

                @Override
                public void success(List<ScoreBoard> result) {
                    int gameWin, gameLoss;
                    String username;
                    Games g=Games.init();
                    g.addScoreboards(result);

                    String[] from= new String[]{"GameWin","userId"};
                    int[] to={R.id.userText,R.id.scoreText};

                    ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>(
                            g.getScoreboards().size());
                    Map m;
                    for (ScoreBoard s: g.getScoreboards()){
                        m=new HashMap<String, String>();
                        m.put("GameWin",s.getGameWin());
                        m.put("userId",s.getUserId());
                        data.add(m);
                    }

                    sbList_.setAdapter(new SimpleAdapter(
                            ScoreBoardActivity.this,
                            data,
                            R.layout.scoreboard_item,
                            from, to));



                }

                @Override
                public void error(Exception e) {
                    Toast.makeText(
                            ScoreBoardActivity.this,
                            "Unable to fetch the scoreboards list, please login again.",
                            Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(ScoreBoardActivity.this,
                            GameListActivity.class));
                }
            });
        }


    }

}
