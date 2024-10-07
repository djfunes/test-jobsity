package com.davidfunes.test.service;

import java.util.List;

import com.davidfunes.test.model.Contact;

public abstract class AbstractContactService {
    public abstract List<Contact> getAllContacts();
    public abstract Contact getContactById(Long id);
}
