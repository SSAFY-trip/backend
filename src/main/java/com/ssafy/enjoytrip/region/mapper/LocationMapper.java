package com.ssafy.enjoytrip.region.mapper;

import com.ssafy.enjoytrip.region.domain.Gugun;
import com.ssafy.enjoytrip.region.domain.Sido;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface LocationMapper {
    @Select("SELECT COUNT(*) > 0 FROM sido WHERE sido_code = #{sidoCode}")
    boolean existsBySidoCode(Integer sidoCode);
    @Select("SELECT * FROM sido")
    List<Sido> getAllSidos();

    @Insert("INSERT INTO sido (sido_code, sido_name) VALUES (#{sidoCode}, #{sidoName})")
    void insertSido(Sido sido);

    @Select("SELECT * FROM sido WHERE sido_code = #{sidoCode}")
    Optional<Sido> findSidoByCode(Integer sidoCode);

    @Select("SELECT * FROM guguns WHERE sido_code = #{sidoCode}")
    List<Gugun> getGugunsBySido(Integer sidoCode);

    @Insert("INSERT INTO guguns (sido_code, gugun_code, gugun_name) VALUES (#{sidoCode}, #{gugunCode}, #{gugunName})")
    void insertGugun(Gugun gugun);

    @Select("SELECT * FROM guguns WHERE gugun_code = #{gugunCode}")
    Optional<Gugun> findGugunByCode(Integer gugunCode);


}
