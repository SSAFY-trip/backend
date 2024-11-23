package com.ssafy.enjoytrip.tripmember.controller;

import com.ssafy.enjoytrip.trip.domain.Trip;
import com.ssafy.enjoytrip.tripmember.service.TripMemberService;
import com.ssafy.enjoytrip.user.domain.User;
import com.ssafy.enjoytrip.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trip-member")
@RequiredArgsConstructor
public class TripMemberController {
    private final TripMemberService tripMemberService;
    private final UserService userService;

    /**
     * trip에 포함되어 있는 사용자들 가져오기
     */
    @GetMapping("/trip/{tripId}/users")
    public ResponseEntity<List<User>> getUsersByTripId(@PathVariable Long tripId) {
        List<User> users = tripMemberService.getUsersByTripId(tripId);
        return ResponseEntity.ok(users);
    }

    /**
     * 사용자가 참가했던 Trip 반환
     */
    @GetMapping("/trips")
    public ResponseEntity<List<Trip>> getTripsByUserId() {
        Long userId = userService.getAuthenticatedUser().getId();
        List<Trip> trips = tripMemberService.getTripsByUserId(userId);
        return ResponseEntity.ok(trips);
    }

    /**
     * trip에 User 추가하기
     */
    @PostMapping("/add")
    public ResponseEntity<Void> addMemberToTrip(@RequestParam Long userId, @RequestParam Long tripId) {
        tripMemberService.addMemberToTrip(userId, tripId);
        return ResponseEntity.ok().build();
    }

    /**
     * Trip에 User 삭제
     */
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeMemberFromTrip(@RequestParam Long userId, @RequestParam Long tripId) {
        tripMemberService.removeMemberFromTrip(userId, tripId);
        return ResponseEntity.ok().build();
    }
}
