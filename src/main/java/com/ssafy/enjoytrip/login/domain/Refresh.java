package com.ssafy.enjoytrip.login.domain;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter @Setter
public class Refresh {
    private Long id;
    private String username;
    private String refresh;
    private Timestamp expiration;
}
