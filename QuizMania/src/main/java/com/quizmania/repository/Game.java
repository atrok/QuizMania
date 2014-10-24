package com.quizmania.repository;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Game {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	private String var1;
	private String var2;
	private String var3;
	private String var4;
	private int answer;
	private String description;
	private Integer rate;
	
	public Game(){}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVar1() {
		return var1;
	}

	public void setVar1(String var1) {
		this.var1 = var1;
	}

	public String getVar2() {
		return var2;
	}

	public void setVar2(String var2) {
		this.var2 = var2;
	}

	public String getVar3() {
		return var3;
	}

	public void setVar3(String var3) {
		this.var3 = var3;
	}

	public String getVar4() {
		return var4;
	}

	public void setVar4(String var4) {
		this.var4 = var4;
	}

	public int getAnswer() {
		return answer;
	}

	public void setAnswer(int answer) {
		this.answer = answer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getRates() {
		return rate;
	}

	public void setRates(Integer rate) {
		this.rate = rate;
	}

	public boolean equalTo(Game g){
		return  (id==g.id);
		
	}
	public String toString(){
		return String.format("Game object ID:%s,\n " +
				"Question1:%s,\n" +
				"Question2:%s,\n" +
				"Question3:%s,\n" +
				"Question4:%s,\n" +
				"Answer:%s,\n" +
				"Explanation:%s,\n" +
				"Rate:%s",id,var1,var2,var3,var4,answer,description,rate);
		
	}
	
	
	public static Builder getBuilder(String var1, String var2,String var3, String var4,
    		int answer, String description, int rates) {
		
        return new Builder(var1, var2,var3, var4,
        		answer, description, rates);
    }
    /**
     * A Builder class used to create new Person objects.
     */
    public static class Builder {
        Game built;
 
        /**
         * Creates a new Builder instance.
         * @param firstName The first name of the created Person object.
         * @param lastName  The last name of the created Person object.
         */
        Builder(String var1, String var2,String var3, String var4,
        		int answer, String description, int rates) {
            built = new Game();
            built.var1 = var1;
            built.var2 = var2;
            built.var3 = var3;
            built.var4 = var4;
            built.answer = answer;
            built.description = description;
            built.rate=rates;
        }
 
        /**
         * Builds the new Person object.
         * @return  The created Person object.
         */
        public Game build() {
            return built;
        }
    }
		
		
	
	
}
