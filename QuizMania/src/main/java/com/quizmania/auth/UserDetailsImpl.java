package com.quizmania.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.social.security.SocialUser;
 
import org.springframework.security.core.userdetails.UserDetails;

import com.quizmania.repository.UserBuilder.Builder;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
 
public class UserDetailsImpl implements UserDetails {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -2757238772493661219L;

	private Long id;
	private String username;
	private String password;
    private String firstName;
    private String lastName;
    private Role role;
    private Set<SimpleGrantedAuthority> authorities_;
	private boolean isAccountNonExpired=false;
	private boolean isEnabled=false;
	private boolean isCredentialsNonExpired=false;
	private boolean isAccountNonLocked=false;
    
    //private SocialMediaService socialSignInProvider;
 
    public UserDetailsImpl(String username, String password, Collection<GrantedAuthority> authorities) {
        this.username=username;
        this.password=password;
        authorities_=new HashSet<SimpleGrantedAuthority>();
        
        Set<String> t=AuthorityUtils.authorityListToSet(authorities);
        for (String tt: t){
        	authorities_.add(new SimpleGrantedAuthority(tt));
        }
    }
 
    //Getters are omitted for the sake of clarity.
 
    public static class Builder {
 
        private Long id;
 
        private String username;
 
        private String firstName;
 
        private String lastName;
 
        private String password;
 
        private Role role;
    	private boolean isAccountNonExpired=true;
    	private boolean isEnabled=true;
    	private boolean isCredentialsNonExpired=true;
    	private boolean isAccountNonLocked=true;
 
        private Set<GrantedAuthority> authorities;
 
        public Builder() {
            this.authorities = new HashSet<>();
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
        	            
        	for (String r: role){
            GrantedAuthority authority = new SimpleGrantedAuthority(r);
            this.authorities.add(authority);
        	}
 
            return this;
        }
 
 
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        
        public Builder isAccountNonExpired(boolean cond) {
            this.isAccountNonExpired = cond;
            return this;
        }
        public Builder isAccountNonLocked(boolean cond) {
            this.isAccountNonLocked = cond;
            return this;
        }
        public Builder isCredentialsNonExpired(boolean cond) {
            this.isCredentialsNonExpired= cond;
            return this;
        }
        public Builder isEnabled(boolean cond) {
            this.isEnabled = cond;
            return this;
        }
 
        public UserDetails build() {
            UserDetailsImpl user = new UserDetailsImpl(username, password, authorities);
 
            user.id = id;
            user.firstName = firstName;
            user.lastName = lastName;
            user.role = role;
            user.isAccountNonExpired=isAccountNonExpired;
            user.isAccountNonLocked=isAccountNonLocked;
            user.isCredentialsNonExpired=isCredentialsNonExpired;
            user.isEnabled=isEnabled;
 
            return user;
        }
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities_;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return isAccountNonLocked;
	}



	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return isEnabled;
	}
	
	public static Builder getBuilder(){
		return new Builder();
	}
}