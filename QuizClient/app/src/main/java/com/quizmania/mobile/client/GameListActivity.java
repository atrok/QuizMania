package com.quizmania.mobile.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.quizmania.client.Game;
import com.quizmania.client.GameSvcApi;
import com.quizmania.client.ScoreBoard;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GameListActivity extends Activity {

	@InjectView(R.id.categorylist)
	protected ListView videoList_;
    private String username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_list);

		ButterKnife.inject(this);

        //mHistoryView = (ListView)findViewById(R.id.history);
        videoList_.setOnItemClickListener(mMessageClickedHandler);

        Intent intent=getIntent();
        username=intent.getStringExtra("username");
        ((TextView)findViewById(R.id.username)).setText(username);
        ((TextView)findViewById(R.id.wintxt)).setText(intent.getStringExtra("wins"));
        ((TextView)findViewById(R.id.losstxt)).setText(intent.getStringExtra("losses"));

        //getCategories();

    }


    // Create a message handling object as an anonymous class.
    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id)
        {


            Intent intent=new Intent(
                    GameListActivity.this,
                    QuizActivity.class);

            intent.putExtra("category",parent.getItemAtPosition(position).toString());
            startActivity(intent);
        }
    };

// Now hook into our object and set its onItemClickListener member
// to our class handler object.

	@Override
	protected void onResume() {
		super.onResume();
		
		refreshVideos();
        getCategories();
	}

    private void getCategories(){

        final GameSvcApi svc = GameSvc.getOrShowLogin(this);

        if (svc != null) {
            CallableTask.invoke(new Callable<List<Game>>() {

                @Override
                public List<Game> call() throws Exception {
                    return svc.getListOfGames();
                }
            }, new TaskCallback<List<Game>>() {

                @Override
                public void success(List<Game> result) {
                    int gameWin, gameLoss;
                    String username;
                    Games g=Games.init();
                    g.addGames(result);

                    Set<String> categories = new TreeSet<String>();
                    for (Game v : result) {
                        categories.add(v.getType());
                    }
                    videoList_.setAdapter(new ArrayAdapter<String>(
                            GameListActivity.this,
                            android.R.layout.simple_list_item_1, categories.toArray(new String[0])));


                }

                @Override
                public void error(Exception e) {
                    Toast.makeText(
                            GameListActivity.this,
                            "Unable to fetch the categories list, please login again.",
                            Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(GameListActivity.this,
                            LoginScreenActivity.class));
                }
            });
        }


    }

	private void refreshVideos() {
		final GameSvcApi svc = GameSvc.getOrShowLogin(this);

		if (svc != null) {
			CallableTask.invoke(new Callable<List<ScoreBoard>>() {

				@Override
				public List<ScoreBoard> call() throws Exception {
					return svc.getlistofResultsPerUser(username);
				}
			}, new TaskCallback<List<ScoreBoard>>() {

				@Override
				public void success(List<ScoreBoard> result) {
                    int gameWin, gameLoss;
                    String username;
					List<String> names = new ArrayList<String>();
					for (ScoreBoard v : result) {
						gameWin=v.getGameWin();
                        gameLoss=v.getGameLoss();
                        username=v.getUserId();
					}
					videoList_.setAdapter(new ArrayAdapter<String>(
							GameListActivity.this,
							android.R.layout.simple_list_item_1, names));
				}

				@Override
				public void error(Exception e) {
					Toast.makeText(
							GameListActivity.this,
							"Unable to fetch the video list, please login again.",
							Toast.LENGTH_SHORT).show();

					startActivity(new Intent(GameListActivity.this,
							LoginScreenActivity.class));
				}
			});
		}
	}

}
