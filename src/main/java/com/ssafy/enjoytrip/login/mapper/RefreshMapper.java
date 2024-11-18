package com.ssafy.enjoytrip.login.mapper;

import com.ssafy.enjoytrip.login.domain.Refresh;
import org.apache.ibatis.annotations.*;
@Mapper
public interface RefreshMapper {

    @Select("SELECT COUNT(*) > 0 FROM refresh_entity WHERE refresh = #{refresh}")
    boolean existsByRefresh(@Param("refresh") String refresh);

    @Delete("DELETE FROM refresh_entity WHERE refresh = #{refresh}")
    void deleteByRefresh(@Param("refresh") String refresh);

    @Insert("INSERT INTO refresh_entity (username, refresh, expiration) VALUES (#{username}, #{refresh}, #{expiration})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Refresh refresh);
}
