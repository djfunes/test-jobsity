package com.davidfunes.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.davidfunes.test.model.AuthenticationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String generateToken() throws Exception {
        // Create a valid user credentials
        String username = "david.funes";
        String password = "EuE4mmV5JzEu";

        // Send a POST request to the AuthController to generate a token
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new AuthenticationRequest(username, password))))
                .andExpect(status().isOk())
                .andReturn();

         // Extract the token from the JSON response
        String responseBody = result.getResponse().getContentAsString();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
        String token = jsonObject.get("token").getAsString();

        return token;
    }

    @Test
    public void testGetAllContacts_Success() throws Exception {
        String token = generateToken();

        mockMvc.perform(MockMvcRequestBuilders.get("/contacts")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetContactById_Success() throws Exception {
        String token = generateToken();

        mockMvc.perform(MockMvcRequestBuilders.get("/contacts/1")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetContactById_InvalidId() throws Exception {
        String token = generateToken();

        mockMvc.perform(MockMvcRequestBuilders.get("/contacts/999")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}