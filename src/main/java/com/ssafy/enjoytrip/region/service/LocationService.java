package com.ssafy.enjoytrip.region.service;

import com.ssafy.enjoytrip.region.adaptor.LocationAdaptor;
import com.ssafy.enjoytrip.region.domain.Gugun;
import com.ssafy.enjoytrip.region.domain.Sido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationAdaptor locationAdaptor;

    public List<Sido> getAllSidos() {
        return locationAdaptor.getAllSidos();
    }

    public void addSido(Sido sido) {
        locationAdaptor.addSido(sido);
    }

    public List<Gugun> getGugunsBySido(Integer sidoCode) {
        return locationAdaptor.getGugunsBySido(sidoCode);
    }

    public void addGugun(Gugun gugun) {
        locationAdaptor.addGugun(gugun);
    }
}
