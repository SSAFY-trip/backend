package com.ssafy.enjoytrip.event.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.*;

import com.ssafy.enjoytrip.event.domain.Event;

@Mapper
public interface EventMapper {
    @Insert("""
            INSERT INTO event (trip_id, place_id, name, date, `order`, memo, latitude, longitude, category)
            SELECT 
                #{tripId},
                #{event.placeId},
                #{event.name},
                #{date},
                COALESCE((SELECT COUNT(*) FROM event WHERE trip_id = #{tripId} AND date = #{date}), 0) + 1,
                #{event.memo},
                #{event.latitude},
                #{event.longitude},
                #{event.category}
            """)
    @Options(useGeneratedKeys = true, keyProperty = "event.id")
    void insertEvent(@Param("tripId") Integer tripId, @Param("date") LocalDate date, @Param("event") Event event);

    @Select("SELECT  * FROM event WHERE id = #{id}")
    Event getEventById(Integer id);

    @Select("SELECT id FROM event WHERE trip_id = #{tripId} AND `date` = #{date}")
    List<Integer> getEventIdsOfTripIdAndDate(@Param("tripId") Integer tripId, @Param("date") LocalDate date);

    @Select("SELECT * FROM event WHERE trip_id = #{tripId} AND `date` = #{date}")
    List<Event> getEventsOfTripIdAndDate(@Param("tripId") Integer tripId, @Param("date") LocalDate date);

    @Select("SELECT place_id FROM event WHERE trip_id = #{tripId} ")
    List<String> getPlaceIdsOfTripId(@Param("tripId") Integer tripId);

    @Select("SELECT * FROM event WHERE trip_id = #{tripId} ORDER BY `date` ASC, `order` ASC")
    List<Event> getOrderedEventsByTripId(Integer tripId);

    @Update("UPDATE event SET memo=#{memo} WHERE id=#{id}")
    int updateEventMemo(Event event);

    @Update("UPDATE event SET `order`=#{order} WHERE id=#{id} AND trip_id = #{tripId}")
    int updateEventOrder(Event event);

    @Update("""
            <script>
                UPDATE event
                <set>
                    `order` = CASE
                    <foreach collection='events' item='event'>
                        WHEN id = #{event.id} THEN #{event.order}
                    </foreach>
                    END
                </set>
                WHERE trip_id = #{tripId} and `date` = #{date}
                AND id IN 
                <foreach collection='events' item='event' open='(' separator=',' close=')'>
                    #{event.id}
                </foreach>
            </script>
            """)
    int updateOrderOfEvents(@Param("tripId") Integer tripId, @Param("date") LocalDate date, @Param("events") List<Event> events);

    @Delete("DELETE FROM event WHERE trip_id=#{tripId} and id=#{id}")
    int deleteEvent(Integer tripId, Integer id);

    @Delete("DELETE FROM event")
    int deleteAllEvents();

    @Update("ALTER TABLE event AUTO_INCREMENT = 1")
    void resetAutoIncrement();
}
