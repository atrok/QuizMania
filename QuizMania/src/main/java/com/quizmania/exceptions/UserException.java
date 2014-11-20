package com.quizmania.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="user with given username already exists")
public class UserException extends QuizmaniaException{
	
	private int status=HttpStatus.CONFLICT.value();

	public UserException(String msg){
		super(msg);
	}
	
	public int getHTTPStatus(){
		return status;
	}
}
