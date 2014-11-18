package com.quizmania.repository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="user with given username already exists")
public class UserException extends RuntimeException{
	
	UserException(){
		super();
	}
}
