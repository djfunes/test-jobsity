package com.davidfunes.test.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 
@Getter
public class AuthRepository {
    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.token}")
    private String apiToken;
}
