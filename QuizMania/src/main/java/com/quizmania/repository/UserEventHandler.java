package com.quizmania.repository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RepositoryEventHandler
public class UserEventHandler {

	Logger log=Logger.getLogger(UserEventHandler.class);
	@Autowired
	private UserRepository userRepository;
	
  @HandleBeforeSave(User.class) 
  public void handleUserSave(User p) throws Exception {
    log.info("BeforeSaveEvent is intercepted for "+p.toString());
    
    if (null!=userRepository.findByUserName(p.getUserName()))
    	throw new Exception("There is already user with this name");
    	
  }

  @ExceptionHandler(UserException.class)
  @HandleBeforeCreate(User.class)
  public void handleUserCreate(User p) throws Exception {
	  log.info("BeforeCreateEvent is intercepted for "+p.toString());
	        
	    if (null!=userRepository.findByUserName(p.getUserName()))
	    	throw new UserException();
	    	
	  }


  @HandleAfterSave(User.class) 
  public void handleUserSaveAfter(User p) throws Exception {
    log.info("BeforeSaveEvent is intercepted for "+p.toString());
    
  }

  @HandleAfterCreate(User.class)
  public void handleUserCreateAfter(User p) throws Exception {
	  log.info("AfterCreateEvent is intercepted for "+p.toString());
	         	
	  }


}
