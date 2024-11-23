package com.ssafy.enjoytrip.tripgugun.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TripGugunMapper {

    @Insert("INSERT INTO trip_gugun (trip_id, gugun_id) VALUES (#{tripId}, #{gugunId})")
    void insertTripGugun(@Param("tripId") Integer tripId, @Param("gugunId") Integer gugunId);

    @Delete("DELETE FROM trip_gugun WHERE trip_id = #{tripId} AND gugun_id = #{gugunId}")
    void deleteTripGugun(@Param("tripId") Integer tripId, @Param("gugunId") Integer gugunId);

    @Select("SELECT gugun_id FROM trip_gugun WHERE trip_id = #{tripId}")
    List<Integer> findGugunsByTripId(@Param("tripId") Integer tripId);

    @Select("SELECT trip_id FROM trip_gugun WHERE gugun_id = #{gugunId}")
    List<Integer> findTripsByGugunId(@Param("gugunId") Integer gugunId);
}
