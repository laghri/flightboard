package com.airxelerate.flightboard.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airxelerate.flightboard.dto.request.FlightRequest;
import com.airxelerate.flightboard.dto.response.FlightResponse;
import com.airxelerate.flightboard.exception.DuplicateFlightException;
import com.airxelerate.flightboard.exception.FlightNotFoundException;
import com.airxelerate.flightboard.model.Flight;
import com.airxelerate.flightboard.repository.FlightRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightService {

    private final FlightRepository flightRepository;

    @Transactional
    public FlightResponse createFlight(FlightRequest request) {
        log.info("Creating flight: {}{} on {}",
                request.getCarrierCode(), request.getFlightNumber(), request.getFlightDate());

        // Check for duplicate flight
        flightRepository.findByCarrierCodeAndFlightNumberAndFlightDate(
                request.getCarrierCode(),
                request.getFlightNumber(),
                request.getFlightDate()
        ).ifPresent(f -> {
            throw new DuplicateFlightException(
                    String.format("Flight %s%s on %s already exists",
                            request.getCarrierCode(),
                            request.getFlightNumber(),
                            request.getFlightDate())
            );
        });

        Flight flight = Flight.builder()
                .carrierCode(request.getCarrierCode())
                .flightNumber(request.getFlightNumber())
                .flightDate(request.getFlightDate())
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .build();

        Flight savedFlight = flightRepository.save(flight);
        log.info("Flight created successfully with ID: {}", savedFlight.getId());

        return mapToResponse(savedFlight);
    }

    @Transactional(readOnly = true)
    public FlightResponse getFlightById(Long id) {
        log.debug("Fetching flight with ID: {}", id);

        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException(
                        "Flight not found with ID: " + id
                ));

        return mapToResponse(flight);
    }

    @Transactional(readOnly = true)
    public List<FlightResponse> getAllFlights() {
        log.debug("Fetching all flights");

        return flightRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFlight(Long id) {
        log.info("Deleting flight with ID: {}", id);

        if (!flightRepository.existsById(id)) {
            throw new FlightNotFoundException("Flight not found with ID: " + id);
        }

        flightRepository.deleteById(id);
        log.info("Flight deleted successfully: {}", id);
    }

    private FlightResponse mapToResponse(Flight flight) {
        return FlightResponse.builder()
                .id(flight.getId())
                .carrierCode(flight.getCarrierCode())
                .flightNumber(flight.getFlightNumber())
                .flightDate(flight.getFlightDate())
                .origin(flight.getOrigin())
                .destination(flight.getDestination())
                .createdAt(flight.getCreatedAt())
                .updatedAt(flight.getUpdatedAt())
                .build();
    }
}