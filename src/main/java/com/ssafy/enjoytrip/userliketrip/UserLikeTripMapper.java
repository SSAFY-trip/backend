package com.ssafy.enjoytrip.userliketrip;

import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.user.dto.UserResponseDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserLikeTripMapper {
    @Insert("INSERT INTO user_like_trip (user_id, trip_id) VALUES (#{userId}, #{tripId})")
    void insertLike(@Param("userId") Long userId, @Param("tripId") Long tripId);

    @Delete("DELETE FROM user_like_trip WHERE user_id = #{userId} AND trip_id = #{tripId}")
    void deleteLike(@Param("userId") Long userId, @Param("tripId") Long tripId);

    @Select("SELECT COUNT(*) > 0 FROM user_like_trip WHERE user_id = #{userId} AND trip_id = #{tripId}")
    boolean isLiked(@Param("userId") Long userId, @Param("tripId") Long tripId);

    @Select("SELECT t.id, t.name, t.start_date, t.end_date, t.trip_overview, t.img_url, t.is_public " +
            "FROM trip t " +
            "JOIN user_like_trip ult ON t.id = ult.trip_id " +
            "WHERE ult.user_id = #{userId}")
    List<TripResponseDto> findLikedTripsByUser(@Param("userId") Long userId);
    @Select("""
        SELECT u.id, u.username, u.name, u.email, u.role
        FROM user u
        JOIN user_like_trip ult ON u.id = ult.user_id
        WHERE ult.trip_id = #{tripId}
    """)
    List<UserResponseDTO> selectUsersWhoLikedTrip(@Param("tripId") Long tripId);
}
