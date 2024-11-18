package com.ssafy.enjoytrip.user.mapper;

import com.ssafy.enjoytrip.domain.User;
import org.apache.ibatis.annotations.*;

public interface UserMapper {

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    @Insert("INSERT INTO user (username, password) VALUES (#{username}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(User member);
}
