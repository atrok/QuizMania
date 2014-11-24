package com.quizmania.exceptions;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import com.quizmania.client.User;
import com.quizmania.repository.UserRepository;

//@RepositoryEventHandler
public class UserEventHandler {

	Logger logger=Logger.getLogger(UserEventHandler.class);
	@Autowired
	private UserRepository userRepository;
	
  @HandleBeforeSave(User.class) 
  public void handleUserSave(User p) throws UserException {
    logger.info("BeforeSaveEvent is intercepted for "+p.toString());
    
    if (null!=userRepository.findByUserName(p.getUserName()))
    	throw new UserException("There is already user with this name");
    	
  }


  @HandleBeforeCreate(User.class)
  public void handleUserCreate(User p) throws UserException {

	  logger.info("BeforeCreateEvent is intercepted for "+p.toString());
	        
	    if (null!=userRepository.findByUserName(p.getUserName()))
	    	throw new UserException("user "+p.getUserName()+" already exists");
	 
	  
	  }


  @HandleAfterSave(User.class) 
  public void handleUserSaveAfter(User p) throws Exception {
    logger.info("BeforeSaveEvent is intercepted for "+p.toString());
    
  }

  @HandleAfterCreate(User.class)
  public void handleUserCreateAfter(User p) throws Exception {
	  logger.info("AfterCreateEvent is intercepted for "+p.toString());
	         	
	  }


}
