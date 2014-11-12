package com.quizmania.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.social.security.SocialUser;
 
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
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
    
    //private SocialMediaService socialSignInProvider;
 
    public UserDetailsImpl(String username, String password, Collection<GrantedAuthority> authorities) {
        this.username=username;
        this.password=password;
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
 
        public Builder role(Role role) {
            this.role = role;
 
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.toString());
            this.authorities.add(authority);
 
            return this;
        }
 
 
        public Builder username(String username) {
            this.username = username;
            return this;
        }
 
        public UserDetailsImpl build() {
            UserDetailsImpl user = new UserDetailsImpl(username, password, authorities);
 
            user.id = id;
            user.firstName = firstName;
            user.lastName = lastName;
            user.role = role;
 
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
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static Builder getBuilder(){
		return new Builder();
	}
}