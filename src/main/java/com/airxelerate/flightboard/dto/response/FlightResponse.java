package com.airxelerate.flightboard.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightResponse {
    
    private Long id;

    private String carrierCode;

    private String flightNumber;

    private LocalDate flightDate;

    private String origin;

    private String destination;

    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
