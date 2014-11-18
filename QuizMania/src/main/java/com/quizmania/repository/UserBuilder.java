package com.quizmania.repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
 * UserBuilder is required for sake of simplicity of User object creation:
 * 
 * User.Builder().
 */
public class UserBuilder {
	
    public static class Builder {
    	 
        private Long id;
 
        private String username;
        
        private String email;
 
        private String firstName;
 
        private String lastName;
 
        private String password;
 
        private Set<String> role;
 
        public Builder() {
            role=new HashSet<>();
        }
 
        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
 
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
 
        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
 
        public Builder password(String password) {
            if (password == null) {
                password = "SocialUser";
            }
 
            this.password = password;
            return this;
        }
 
        public Builder role(String... role) {
        		Collections.addAll(this.role, role);
 
            return this;
        }
 
 
        public Builder username(String username) {
            this.username = username;
            return this;
        }
 
        public User build() {
            User user = new User(username, password);
 

            user.setFirstName(firstName); 
            user.setLastName(lastName);
            user.setEmail(email);
            user.setRole(role);
           
 
            return user;
        }
        
        public static Builder getBuilder(){
        	return new Builder();
        }
    }
}
