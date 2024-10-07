package com.davidfunes.test.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.davidfunes.test.model.Contact;
import com.davidfunes.test.repository.AuthRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Service
public class ContactServiceImpl extends AbstractContactService {

    private static final Logger logger = Logger.getLogger(ContactServiceImpl.class.getName());

    @Autowired
    private AuthRepository authRepository;

    private RestTemplate restTemplate = new RestTemplate();
    private Gson gson = new Gson();

    @Override
    public List<Contact> getAllContacts() {
        String apiUrl = authRepository.getApiUrl();
        String apiToken = authRepository.getApiToken();
        
        List<Contact> allContacts = new ArrayList<>();
        int page = 1;
    
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
    
        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
            int totalPages = Integer.parseInt(response.getHeaders().get("Total-Pages").get(0));
    
            while (page <= totalPages) {
                String pagedUrl = apiUrl + "?page=" + page;
                response = restTemplate.exchange(pagedUrl, HttpMethod.GET, entity, String.class);
                if (response.getStatusCode() == HttpStatus.OK) {
                    String jsonData = response.getBody();
                    JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
                    JsonArray contactsArray = jsonObject.get("contacts").getAsJsonArray();
                    List<Contact> contacts = gson.fromJson(contactsArray, new TypeToken<List<Contact>>() {}.getType());
                    allContacts.addAll(contacts);
                    page++;
                } else {
                    logger.info("Failed to fetch contacts. Status code: " + response.getStatusCode());
                    break;
                }
            }
        } catch (RestClientException e) {
            logger.warning("Error fetching contacts: " + e.getMessage());
        }
        return allContacts;
    }

    @Override
    public Contact getContactById(Long id) {
        String apiUrl = authRepository.getApiUrl();
        String apiToken = authRepository.getApiToken();

        Contact contact = null;

        String url = apiUrl + "/" + id;
        logger.info("Fetching contact " + url + "...");
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                String jsonData = response.getBody();
                JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
                contact = gson.fromJson(jsonObject.get("contact"), new TypeToken<Contact>() {}.getType());
            } else {
                logger.info("Failed to fetch contact. Status code: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            logger.warning("Error fetching contact: " + e.getMessage());
        }
        
        return contact;
    }
    
}
