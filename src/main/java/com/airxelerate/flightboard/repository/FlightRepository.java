package com.airxelerate.flightboard.repository;

import com.airxelerate.flightboard.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByCarrierCode(String carrierCode);

    List<Flight> findByOrigin(String origin);

    List<Flight> findByDestination(String destination);

    List<Flight> findByFlightDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT f FROM Flight f WHERE " +
            "f.carrierCode = :carrierCode AND " +
            "f.flightNumber = :flightNumber AND " +
            "f.flightDate = :flightDate")
    Optional<Flight> findByCarrierCodeAndFlightNumberAndFlightDate(
            @Param("carrierCode") String carrierCode,
            @Param("flightNumber") String flightNumber,
            @Param("flightDate") LocalDate flightDate);
}
