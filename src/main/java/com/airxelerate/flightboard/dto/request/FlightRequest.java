package com.airxelerate.flightboard.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightRequest {

    @NotBlank(message = "Carrier code is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Carrier code must be 2 uppercase letters (IATA code)")
    private String carrierCode;

    @NotBlank(message = "Flight number is required")
    @Pattern(regexp = "^\\d{4}$", message = "Flight number must be exactly 4 digits")
    private String flightNumber;

    @NotNull(message = "Flight date is required")
    private LocalDate flightDate;

    @NotBlank(message = "Origin airport is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Origin must be 3 uppercase letters (IATA airport code)")
    private String origin;

    @NotBlank(message = "Destination airport is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Destination must be 3 uppercase letters (IATA airport code)")
    private String destination;
}
