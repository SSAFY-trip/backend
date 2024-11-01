package com.ssafy.enjoytrip.repository;

import com.ssafy.enjoytrip.domain.Trip;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TripMapper {

    @Insert("INSERT INTO trip(name, start_date, end_date, trip_overview, img_url, is_public) VALUES(#{name}, #{startDate}, #{endDate}, #{tripOverview}, #{imgUrl}, #{isPublic})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertTrip(Trip trip);

    @Select("SELECT * FROM trip WHERE id = #{id}")
    Trip getTripById(Integer id);

    @Select("SELECT * FROM trip")
    List<Trip> getAllTrips();

    @Update("UPDATE trip SET name = #{name}, start_date = #{startDate}, end_date = #{endDate}, trip_overview = #{tripOverview}, img_url = #{imgUrl}, is_public = #{isPublic} WHERE id = #{id}")
    void updateTrip(Trip trip);

    @Delete("DELETE FROM trip WHERE id = #{id}")
    void deleteTrip(Integer id);
}