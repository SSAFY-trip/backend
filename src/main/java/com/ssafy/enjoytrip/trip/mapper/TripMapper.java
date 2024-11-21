package com.ssafy.enjoytrip.trip.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.ssafy.enjoytrip.trip.domain.Trip;

@Mapper
public interface TripMapper {
    @Insert("INSERT INTO trip(uid, name, start_date, end_date, trip_overview, img_url, is_public) VALUES(#{uid}, #{name}, #{startDate}, #{endDate}, #{tripOverview}, #{imgUrl}, #{isPublic})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertTrip(Trip trip);

    @Select("SELECT * FROM trip WHERE id = #{id}")
    Trip getTripById(Integer id);

    @Select("SELECT * FROM trip")
    List<Trip> getAllTrips();

    @Update("UPDATE trip SET name = #{name}, start_date = #{startDate}, end_date = #{endDate}, trip_overview = #{tripOverview}, img_url = #{imgUrl}, is_public = #{isPublic} WHERE id = #{id}")
    int updateTrip(Trip trip);

    @Delete("DELETE FROM trip WHERE id = #{id}")
    int deleteTrip(Integer id);

    @Delete("DELETE FROM trip")
    int deleteAllTrips();

    @Update("ALTER TABLE trip AUTO_INCREMENT = 1")
    void resetAutoIncrement();
}
