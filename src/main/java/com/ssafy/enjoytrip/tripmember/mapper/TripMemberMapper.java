package com.ssafy.enjoytrip.tripmember.mapper;

import com.ssafy.enjoytrip.trip.domain.Trip;
import com.ssafy.enjoytrip.user.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TripMemberMapper {

    @Select("SELECT * FROM users WHERE id IN (SELECT user_id FROM trip_member WHERE trip_id = #{tripId})")
    List<User> findUsersByTripId(@Param("tripId") Long tripId);

    @Select("SELECT * FROM trips WHERE id IN (SELECT trip_id FROM trip_member WHERE user_id = #{userId})")
    List<Trip> findTripsByUserId(@Param("userId") Long userId);
    @Select("SELECT COUNT(*) > 0 FROM trip_member WHERE user_id = #{userId} AND trip_id = #{tripId}")
    boolean existsByUserIdAndTripId(@Param("userId") Long userId, @Param("tripId") Long tripId);

    @Insert("INSERT INTO trip_member (user_id, trip_id) VALUES (#{userId}, #{tripId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Long userId, Long tripId);

    @Delete("DELETE FROM trip_member WHERE user_id = #{userId} AND trip_id = #{tripId}")
    void remove(@Param("userId") Long userId, @Param("tripId") Long tripId);
}
