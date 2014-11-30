package com.quizmania.mobile.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quizmania.client.GameSvcApi;
import com.quizmania.client.Game;
import com.quizmania.client.ScoreBoard;
import com.quizmania.client.User;


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


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

	@OnClick(R.id.loginButton)
	public void login(View view) {
		final String user = userName_.getText().toString();
		final String pass = password_.getText().toString();
		String server = server_.getText().toString();

        // Reset errors.
        userName_.setError(null);
        password_.setError(null);

        // UserDTO is supposed to be static and available from every point of application
        // it's going to hold user data that we will send to our server finally
        UserDTO.getBuilder()
                .setUser(new User(user,pass))
                .setScoreboard(new ScoreBoard(user,0,0,null))// Initialize ScoreBoard with null results, we update it when get results from server
                .build();

		final GameSvcApi svc = GameSvc.init(server, user, pass);


        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(pass) && !isPasswordValid(pass)) {
            password_.setError(getString(R.string.error_invalid_password));
            focusView = password_;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(user)) {
            userName_.setError(getString(R.string.error_field_required));
            focusView = userName_;
            cancel = true;
        } else if (!isEmailValid(user)) {
            userName_.setError(getString(R.string.error_invalid_email));
            focusView = userName_;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            switch(view.getId()) {
                case R.id.loginButton:

                CallableTask.invoke(new Callable<List<ScoreBoard>>() {

                    @Override
                    public List<ScoreBoard> call() throws Exception {
                        return svc.getlistofResultsPerUser(user);
                    }
                }, new TaskCallback<List<ScoreBoard>>() {

                    @Override
                    public void success(List<ScoreBoard> result) {
                        // OAuth 2.0 grant was successful and we
                        // can talk to the server, open up the video listing

                        // we should expect server to return 1 object only, we put it into UserDTO here for future references
                        UserDTO.setSb(result.get(0));

                        Intent intent = new Intent(
                                LoginScreenActivity.this,
                                GameListActivity.class);
                        intent.putExtra("username", result.get(0).getUserId()); // result should contain 1 object only
                        intent.putExtra("wins", Integer.toString(result.get(0).getGameWin()));
                        intent.putExtra("losses", Integer.toString(result.get(0).getGameLoss()));
                        startActivity(intent);
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
                    break;
                case R.id.signinButton:

                    CallableTask.invoke(new Callable<List<ScoreBoard>>() {

                        @Override
                        public List<ScoreBoard> call() throws Exception {
                            svc.addUser(new User(user, pass));
                            List<ScoreBoard> sb = new ArrayList<ScoreBoard>();
                            sb.add(new ScoreBoard(user,0,0,null));
                            return sb;
                        }
                    }, new TaskCallback<List<ScoreBoard>>() {

                        @Override
                        public void success(List<ScoreBoard> result) {
                            // OAuth 2.0 grant was successful and we
                            // can talk to the server, open up the video listing

                            // we should expect server to return 1 object only, we put it into UserDTO here for future references
                            UserDTO.setSb(result.get(0));

                            Intent intent = new Intent(
                                    LoginScreenActivity.this,
                                    GameListActivity.class);
                            intent.putExtra("username", result.get(0).getUserId()); // result should contain 1 object only
                            intent.putExtra("wins", Integer.toString(result.get(0).getGameWin()));
                            intent.putExtra("losses", Integer.toString(result.get(0).getGameLoss()));
                            startActivity(intent);
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
	}

}
