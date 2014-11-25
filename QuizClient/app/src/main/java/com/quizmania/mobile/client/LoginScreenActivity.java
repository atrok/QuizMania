package com.quizmania.mobile.client;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.quizmania.client.GameSvcApi;
import com.quizmania.client.Game;

import org.magnum.videoup.client.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 
 * This application uses ButterKnife. AndroidStudio has better support for
 * ButterKnife than Eclipse, but Eclipse was used for consistency with the other
 * courses in the series. If you have trouble getting the login button to work,
 * please follow these directions to enable annotation processing for this
 * Eclipse project:
 * 
 * http://jakewharton.github.io/butterknife/ide-eclipse.html
 * 
 */
public class LoginScreenActivity extends Activity {

	@InjectView(R.id.userName)
	protected EditText userName_;

	@InjectView(R.id.password)
	protected EditText password_;

	@InjectView(R.id.server)
	protected EditText server_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);

		ButterKnife.inject(this);
	}

	@OnClick(R.id.loginButton)
	public void login() {
		String user = userName_.getText().toString();
		String pass = password_.getText().toString();
		String server = server_.getText().toString();

		final GameSvcApi svc = GameSvc.init(server, user, pass);

		CallableTask.invoke(new Callable<List<Game>>() {

			@Override
			public List<Game> call() throws Exception {
				return svc.getListOfGames();
			}
		}, new TaskCallback<List<Game>>() {

			@Override
			public void success(List<Game> result) {
				// OAuth 2.0 grant was successful and we
				// can talk to the server, open up the video listing
				startActivity(new Intent(
						LoginScreenActivity.this,
						GameListActivity.class));
			}

			@Override
			public void error(Exception e) {
				Log.e(LoginScreenActivity.class.getName(), "Error logging in via OAuth.", e);
				
				Toast.makeText(
						LoginScreenActivity.this,
						"Login failed, check your Internet connection and credentials.",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}
