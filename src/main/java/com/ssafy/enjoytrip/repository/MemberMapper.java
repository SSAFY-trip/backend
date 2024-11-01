package com.ssafy.enjoytrip.repository;

import com.ssafy.enjoytrip.domain.Member;
import org.apache.ibatis.annotations.*;

public interface MemberMapper {

    @Select("SELECT * FROM member WHERE username = #{username}")
    Member findByUsername(@Param("username") String username);

    @Insert("INSERT INTO member (username, password) VALUES (#{username}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Member member);
}
