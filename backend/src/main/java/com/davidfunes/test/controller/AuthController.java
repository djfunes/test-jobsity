package com.davidfunes.test.controller;

import com.davidfunes.test.model.AuthenticationRequest;
import com.davidfunes.test.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, String>> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        // Attempt to authenticate the user
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (Exception e) {
            // Throw a specific exception if authentication fails
            throw new RuntimeException("Incorrect username or password", e);
        }

        // Load user details
        final UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("User not found: " + authenticationRequest.getUsername(), e);
        }

        // Generate the JWT token using the UserDetails object
        final String jwt = jwtUtil.generateToken(userDetails);
        if (jwt == null) {
            throw new RuntimeException("Failed to generate JWT token");
        }

        // Return the token as a JSON response
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", jwt);
        return ResponseEntity.ok(tokenMap);
    }
}
