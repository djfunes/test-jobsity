package com.davidfunes.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.davidfunes.test.controller.AuthController;
import com.davidfunes.test.model.AuthenticationRequest;
import com.davidfunes.test.security.JwtUtil;
import com.davidfunes.test.security.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    public void testSuccessfulAuthentication() throws Exception {
        // Mock the response of the JwtUtil
        Mockito.when(jwtUtil.generateToken(Mockito.any())).thenReturn("mocked-jwt-token");

        // Mock the response of the UserDetailsService
        UserDetails userDetails = User.withUsername("user")
                .password("password")
                .roles("USER")
                .build();
        Mockito.when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);

        // Create a test authentication request
        AuthenticationRequest request = new AuthenticationRequest("david.funes", "EuE4mmV5JzEu");

        // Send a POST request to the authentication endpoint
        MvcResult result = mockMvc.perform(post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andReturn();

        // Verify that the response contains a valid JWT token
        String response = result.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testFailedAuthentication_InvalidCredentials() throws Exception {
        // Mock the behavior of the UserDetailsService to throw an exception for invalid user
        Mockito.when(userDetailsService.loadUserByUsername("invaliduser"))
            .thenThrow(new UsernameNotFoundException("User not found: invaliduser"));

        // Create a test authentication request with invalid credentials
        AuthenticationRequest request = new AuthenticationRequest("invaliduser", "invalidpassword");

        // Send a POST request to the authentication endpoint and expect a 401 Unauthorized response
        mockMvc.perform(post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isUnauthorized()); // Expect 401 Unauthorized status
    }


    

    @Test
    public void testFailedAuthentication_MissingCredentials() throws Exception {
        // Send a POST request to the authentication endpoint with missing credentials
        mockMvc.perform(post("/authenticate"))
                .andExpect(status().isBadRequest());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}