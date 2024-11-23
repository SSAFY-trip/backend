package com.ssafy.enjoytrip.region;

import com.ssafy.enjoytrip.region.domain.Gugun;
import com.ssafy.enjoytrip.region.domain.Sido;
import com.ssafy.enjoytrip.region.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/sidos")
    public ResponseEntity<List<Sido>> getAllSidos() {
        return ResponseEntity.ok(locationService.getAllSidos());
    }

    @PostMapping("/sidos")
    public ResponseEntity<Void> addSido(@RequestBody Sido sido) {
        locationService.addSido(sido);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/guguns/{sidoCode}")
    public ResponseEntity<List<Gugun>> getGugunsBySido(@PathVariable Integer sidoCode) {
        return ResponseEntity.ok(locationService.getGugunsBySido(sidoCode));
    }

    @PostMapping("/guguns")
    public ResponseEntity<Void> addGugun(@RequestBody Gugun gugun) {
        locationService.addGugun(gugun);
        return ResponseEntity.ok().build();
    }
}
