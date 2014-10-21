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
	
	private Map<Long, String> var1;
	private Map<Long, String>  var2;
	private Map<Long, String>  var3;
	private Map<Long, String>  var4;
	private long  answer;
	private String description;
	
	public Game(){}

	public Map<Long, String> getVar1() {
		return var1;
	}

	public void setVar1(Map<Long, String> var1) {
		this.var1 = var1;
	}

	public Map<Long, String> getVar2() {
		return var2;
	}

	public void setVar2(Map<Long, String> var2) {
		this.var2 = var2;
	}

	public Map<Long, String> getVar3() {
		return var3;
	}

	public void setVar3(Map<Long, String> var3) {
		this.var3 = var3;
	}

	public Map<Long, String> getVar4() {
		return var4;
	}

	public void setVar4(Map<Long, String> var4) {
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
