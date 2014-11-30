/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package com.quizmania.mobile.client;


import android.content.Context;
import android.content.Intent;

import com.quizmania.client.GameSvcApi;
import com.quizmania.client.SecuredRestBuilder;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;


public class GameSvc {

   // private Logger logger= Logger.getLogger(GameSvc.class);
    private static final String CLIENT_ID = "mobile";

   private static final String TEST_URL = "http://localhost:8888";//  we use fiddler port to intercept http request intended for our application running on 8443

    private static GameSvcApi gameSvc_,gameSvcUnsafe_;


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
                new SecuredRestBuilder()
                        .setLoginEndpoint(server + GameSvcApi.TOKEN_PATH)
                        .setUsername(user)
                        .setPassword(pass)
                        .setClientId(CLIENT_ID)
                        .setClient(
                                new ApacheClient(new EasyHttpClient()))
                        .setEndpoint(server).setLogLevel(LogLevel.FULL).build()
                        .create(GameSvcApi.class);

		return gameSvc_;
	}

    // it's needed when new user is to be created on server
    public static synchronized GameSvcApi initUnsafe(String server, String user,
                                               String pass) {

        gameSvcUnsafe_ =
                new RestAdapter.Builder()
                        .setClient(new ApacheClient(new EasyHttpClient()))
                        .setEndpoint(server).setLogLevel(RestAdapter.LogLevel.FULL)
                        .build()
                        .create(GameSvcApi.class);

        return gameSvcUnsafe_;
    }


}


