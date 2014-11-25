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

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import retrofit.ErrorHandler;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;


public class GameSvc {

    private Logger logger= Logger.getLogger(GameSvc.class);
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "none";
    private static final String CLIENT_ID = "mobile";
    private static final String READ_ONLY_CLIENT_ID = "mobileReader";

    private static final String TEST_URL = "http://localhost:8888";//  we use fiddler port to intercept http request intended for our application running on 8443

    private static GameSvcApi gameSvc_;



    private static ErrorRecorder error=new ErrorRecorder();

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
                        .setLoginEndpoint(TEST_URL + GameSvcApi.TOKEN_PATH)
                        .setUsername(USERNAME)
                        .setPassword(PASSWORD)
                        .setClientId(CLIENT_ID)
                        .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
                        .setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL)
                        .setErrorHandler(error).build()
                        .create(GameSvcApi.class);

		return gameSvc_;
	}



}

class UnsafeHttpsClient {

    public static HttpClient createUnsafeClient() {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build());
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf).build();

            return httpclient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class ErrorRecorder implements ErrorHandler {

    private RetrofitError error;

    @Override
    public Throwable handleError(RetrofitError cause) {
        error = cause;
        return error.getCause();
    }

    public RetrofitError getError() {
        return error;
    }
}