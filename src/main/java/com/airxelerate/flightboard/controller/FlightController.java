package com.airxelerate.flightboard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.airxelerate.flightboard.dto.request.FlightRequest;
import com.airxelerate.flightboard.dto.response.ApiResponse;
import com.airxelerate.flightboard.dto.response.FlightResponse;
import com.airxelerate.flightboard.service.FlightService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
@Slf4j
public class FlightController {

    private final FlightService flightService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FlightResponse>> createFlight(
            @Valid @RequestBody FlightRequest request) {
        FlightResponse response = flightService.createFlight(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Flight created successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<FlightResponse>> getFlightById(@PathVariable Long id) {
        FlightResponse response = flightService.getFlightById(id);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Flight retrieved successfully"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<FlightResponse>>> getAllFlights() {
        List<FlightResponse> responses = flightService.getAllFlights();

        return ResponseEntity.ok(
                ApiResponse.success(responses, "Flights retrieved successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);

        return ResponseEntity.ok(
                ApiResponse.success(null, "Flight deleted successfully"));
    }
}
