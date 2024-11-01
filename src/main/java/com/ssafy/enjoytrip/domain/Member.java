package com.ssafy.enjoytrip.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "member")
@Getter
public class Member {
    @Id
    @Column(name = "MEMBER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
