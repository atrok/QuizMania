/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package com.quizmania.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;

import com.quizmania.repository.UserRepository;

/**
 * A class that combines a UserDetailsService and ClientDetailsService
 * into a single object.
 * 
 * @author jules
 *
 */
public class ClientAndUserDetailsService implements UserDetailsService,
		ClientDetailsService {

    
	private final ClientDetailsService clients_;

	private final UserDetailsService users_;
	
	private final ClientDetailsUserDetailsService clientDetailsWrapper_;

	public ClientAndUserDetailsService(ClientDetailsService clients,
			UserDetailsService users) {
		super();
		clients_ = clients;
		users_ = users;
		clientDetailsWrapper_ = new ClientDetailsUserDetailsService(clients_);
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId)
			throws ClientRegistrationException {
		return clients_.loadClientByClientId(clientId);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserDetails user = null;
		try{
			user = users_.loadUserByUsername(username);
		}catch(UsernameNotFoundException e){
			user = clientDetailsWrapper_.loadUserByUsername(username);
			e.printStackTrace();
			throw e;
		}
		return user;
	}

}
