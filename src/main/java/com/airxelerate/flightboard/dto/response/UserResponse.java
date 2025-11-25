package com.airxelerate.flightboard.dto.response;

import com.airxelerate.flightboard.model.Role;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private Role role;

    private Boolean enabled;

    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
