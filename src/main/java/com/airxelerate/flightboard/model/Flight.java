package com.airxelerate.flightboard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights", uniqueConstraints = @UniqueConstraint(columnNames = { "carrier_code", "flight_number",
        "flight_date" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Carrier code is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Carrier code must be 2 uppercase letters (IATA code)")
    @Column(name = "carrier_code", nullable = false, length = 2)
    private String carrierCode;

    @NotNull(message = "Flight number is required")
    @Pattern(regexp = "^\\d{4}$", message = "Flight number must be exactly 4 digits")
    @Column(name = "flight_number", nullable = false, length = 4)
    private String flightNumber;

    @NotNull(message = "Flight date is required")
    @Column(name = "flight_date", nullable = false)
    private LocalDate flightDate;

    @NotNull(message = "Origin airport is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Origin must be 3 uppercase letters (IATA airport code)")
    @Column(name = "origin", nullable = false, length = 3)
    private String origin;

    @NotNull(message = "Destination airport is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Destination must be 3 uppercase letters (IATA airport code)")
    @Column(name = "destination", nullable = false, length = 3)
    private String destination;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
