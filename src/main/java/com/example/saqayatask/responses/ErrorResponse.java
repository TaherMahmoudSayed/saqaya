package com.example.saqayatask.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse {
    private Boolean success;
    private String errorMessage;
    private LocalDateTime dateTime;
    private List<String> details;

}
