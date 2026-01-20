package com.sara.allmart.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse (
        String message,
        int StatusCode,
        LocalDateTime timestamp
){
}
