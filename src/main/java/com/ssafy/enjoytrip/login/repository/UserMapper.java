package com.ssafy.enjoytrip.login.repository;

import com.ssafy.enjoytrip.login.domain.User;
import org.apache.ibatis.annotations.*;
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE username = #{username}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "email", column = "email"),
            @Result(property = "name", column = "name"),
            @Result(property = "provider", column = "provider"),
            @Result(property = "role", column = "role")
    })
    User findByUsername(@Param("username") String username);

    @Insert("INSERT INTO user (username, password, email, name, provider, role) VALUES (#{username}, #{password}, #{email}, #{name}, #{provider}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(User user);

    @Select("SELECT COUNT(*) > 0 FROM user WHERE username = #{username}")
    boolean existsByUsername(@Param("username") String username);

    @Select("SELECT COUNT(*) > 0 FROM user WHERE name = #{name}")
    boolean existsByName(@Param("name") String name);

}
