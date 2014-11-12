package com.quizmania.auth;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.quizmania.repository.User;
import com.quizmania.repository.UserRepository;
 
public class RepositoryUserDetailsService implements UserDetailsService {
 
	private Logger log=Logger.getLogger(RepositoryUserDetailsService.class);
    private UserRepository repository;
 
    //@Autowired
    public RepositoryUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }
 

	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		log.info("Entered into RepositoryUserDetailsService.loadUserByUsername, looking for:"+username );
        User user = repository.findByEmail(username);
 
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
 
        UserDetails principal = UserDetailsImpl.getBuilder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .username(user.getEmail())
                .build();
 
        return principal;
    }
}
