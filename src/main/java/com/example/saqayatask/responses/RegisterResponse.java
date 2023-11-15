package com.example.saqayatask.responses;


import lombok.Builder;

@Builder
public record RegisterResponse(String Id,String accessToken) {
}
