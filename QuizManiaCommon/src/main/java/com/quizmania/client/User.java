package com.quizmania.client;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



//@DynamoDBTable(tableName = "User")
public class User{


	private String userName="";
	private String firstName="";
	private String email="";
	private String lastName="";
	private String password="none";
	private Set<String> role=new HashSet<String>();
	private boolean AccountNonExpired=true,AccountNonLocked=true, CredentialsNonExpired=true, Enabled=true;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		
		this.password = password;
	}

	public User(String username,String password) {
		
		this.password=PassEncoder.get().encode(password);
		this.userName=username;
		
	}
	
	public User(){}
	

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


//	@DynamoDBAttribute
	public String getFirstName() {
		return firstName;
	}

//	@DynamoDBAttribute
	public String getLastName() {
		return lastName;
	}

	@Override
	public boolean equals(Object u) {
		if (u instanceof User) {
			User uu = (User) u;

			if (userName.equals(uu.userName))
				return true;
		}

		return false;
	}
	
//	@DynamoDBHashKey
	public String getUserName() {
		return userName;
	}

	public void setUserName(String username) {
		this.userName = username;
	}

//	@DynamoDBAttribute
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	
	public Set<String> getRole() {
		return role;
	}

/*
	public void setRole(String... role) {
		Collections.addAll(this.role, role);
	}
*/
	public void setRole(Set<String> role) {
		this.role=role;
	}



	public boolean isAccountNonExpired() {
		return AccountNonExpired;
	}



	public void setAccountNonExpired(boolean accountNonExpired) {
		AccountNonExpired = accountNonExpired;
	}



	public boolean isAccountNonLocked() {
		return AccountNonLocked;
	}



	public void setAccountNonLocked(boolean accountNonLocked) {
		AccountNonLocked = accountNonLocked;
	}



	public boolean isCredentialsNonExpired() {
		return CredentialsNonExpired;
	}



	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		CredentialsNonExpired = credentialsNonExpired;
	}



	public boolean isEnabled() {
		return Enabled;
	}



	public void setEnabled(boolean enabled) {
		Enabled = enabled;
	}

}

class PassEncoder{
	
	public static BCryptPasswordEncoder get(){
		return new BCryptPasswordEncoder(10);
	}
}