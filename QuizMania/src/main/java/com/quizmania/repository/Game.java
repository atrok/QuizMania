package com.quizmania.repository;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Game {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private Map<String, String> var1;
	private Map<String, String>  var2;
	private Map<String, String>  var3;
	private Map<String, String>  var4;
	private long  answer;
	private String description;
	
	public Game(){}

	public Map<String, String> getVar1() {
		return var1;
	}

	public void setVar1(Map<String, String> var1) {
		this.var1 = var1;
	}

	public Map<String, String> getVar2() {
		return var2;
	}

	public void setVar2(Map<String, String> var2) {
		this.var2 = var2;
	}

	public Map<String, String> getVar3() {
		return var3;
	}

	public void setVar3(Map<String, String> var3) {
		this.var3 = var3;
	}

	public Map<String, String> getVar4() {
		return var4;
	}

	public void setVar4(Map<String, String> var4) {
		this.var4 = var4;
	}

	public long getAnswer() {
		return answer;
	}

	public void setAnswer(long answer) {
		this.answer = answer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getId() {
		return id;
	}

	
}
