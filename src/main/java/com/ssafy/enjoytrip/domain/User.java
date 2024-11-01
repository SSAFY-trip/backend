package com.ssafy.enjoytrip.domain;

import lombok.Getter;

@Getter
public class User {
    private Long id;
    private String username;
    private String password;

    public void addUsername(String username){
        this.username = username;
    }
    public void addPassword(String password){
        this.password = password;
    }
}
