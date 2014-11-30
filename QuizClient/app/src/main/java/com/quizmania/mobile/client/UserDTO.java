package com.quizmania.mobile.client;

import com.quizmania.client.ScoreBoard;
import com.quizmania.client.User;

/**
 * Created by atrok on 11/29/2014.
 *
 * Class that will combine User, ScoreBoard and Game data
 */
public class UserDTO {

    private static User user;
    private static ScoreBoard sb;
    private static UserDTO userDTO;

    public static ScoreBoard getSb() {
        return sb;
    }

    public static void setSb(ScoreBoard sb) {
        UserDTO.sb = sb;

    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserDTO.user = user;
    }


/*
this private class serves the purpose of lazy initizaliation of UserDTO singleton:
UserDTO.getBuilder().setUser(User).setScoreboard(Scoreboard).build();
then we access UserDTO directly from any point of application
UserDTO.getUser();
UserDTO.setUser();
UserDTO.getSb();
UserDTO.setSb()
 */
    public static class Builder{

        private User user_;
        private ScoreBoard sb_;

        public Builder(){

            if (userDTO == null) {
                userDTO = new UserDTO();
            }
        }

        public Builder setUser(User user){
            this.user_=user;
            return this;
        }
        public Builder setScoreboard(ScoreBoard sb){
            this.sb_=sb;
            return this;
        }

        public UserDTO build(){
            userDTO.setUser(user_);
            userDTO.setSb(sb_);
            return userDTO;
        }

    }

    public static Builder getBuilder(){

        return new Builder();
    }
}
