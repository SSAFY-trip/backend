package com.ssafy.enjoytrip.region.adaptor;

import com.ssafy.enjoytrip.global.annotation.Adaptor;
import com.ssafy.enjoytrip.region.domain.Gugun;
import com.ssafy.enjoytrip.region.domain.Sido;
import com.ssafy.enjoytrip.region.exception.SidoAlreadyExistsException;
import com.ssafy.enjoytrip.region.exception.SidoNotFoundException;
import com.ssafy.enjoytrip.region.mapper.LocationMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Adaptor
@RequiredArgsConstructor
public class LocationAdaptor {

    private final LocationMapper locationMapper;

    public List<Sido> getAllSidos() {
        return locationMapper.getAllSidos();
    }

    public void addSido(Sido sido) {
        if (locationMapper.existsBySidoCode(sido.getSidoCode())) {
            throw new SidoAlreadyExistsException();
        }
        locationMapper.insertSido(sido);
    }

    public List<Gugun> getGugunsBySido(Integer sidoCode) {
        findSidoByCode(sidoCode); // Validate Sido
        return locationMapper.getGugunsBySido(sidoCode);
    }

    private Sido findSidoByCode(Integer sidoCode) {
        return locationMapper.findSidoByCode(sidoCode)
                .orElseThrow(SidoNotFoundException::new);
    }

    public void addGugun(Gugun gugun) {
        if (!sidoExists(gugun.getSidoCode())) {
            throw new SidoNotFoundException();
        }
        locationMapper.insertGugun(gugun);
    }

    private boolean sidoExists(Integer sidoCode) {
        return locationMapper.findSidoByCode(sidoCode).isPresent();
    }
}
