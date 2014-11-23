/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package com.quizmania.mobile.client;


import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import android.content.Context;
import android.content.Intent;

import com.quizmania.client.GameSvcApi;
import com.quizmania.repository.Game;

public class GameSvc {

	private static GameSvcApi gameSvc_;

	public static synchronized GameSvcApi getOrShowLogin(Context ctx) {
		if (gameSvc_ != null) {
			return gameSvc_;
		} else {
			Intent i = new Intent(ctx, LoginScreenActivity.class);
			ctx.startActivity(i);
			return null;
		}
	}

	public static synchronized GameSvcApi init(String server, String user,
			String pass) {

		gameSvc_ =
                new RestAdapter.Builder()
				.setEndpoint(server).setLogLevel(LogLevel.FULL).build()
				.create(GameSvcApi.class);

		return gameSvc_;
	}
}
