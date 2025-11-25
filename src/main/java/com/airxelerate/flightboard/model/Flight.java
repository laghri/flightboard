package com.airxelerate.flightboard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 2)
    @Pattern(regexp = "^[A-Z0-9]{2}$", message = "Carrier code must be 2 characters (IATA format)")
    private String carrierCode;
    
    @Column(nullable = false, length = 4)
    @Pattern(regexp = "^\\d{4}$", message = "Flight number must be 4 digits")
    private String flightNumber;
    
    @Column(nullable = false)
    private LocalDate flightDate;
    
    @Column(nullable = false, length = 3)
    @Pattern(regexp = "^[A-Z]{3}$", message = "Origin must be 3-letter IATA airport code")
    private String origin;
    
    @Column(nullable = false, length = 3)
    @Pattern(regexp = "^[A-Z]{3}$", message = "Destination must be 3-letter IATA airport code")
    private String destination;
}
