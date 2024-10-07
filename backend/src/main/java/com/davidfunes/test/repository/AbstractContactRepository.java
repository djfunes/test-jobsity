package com.davidfunes.test.repository;

import java.util.List;

import com.davidfunes.test.model.Contact;

public abstract class AbstractContactRepository {
    public abstract List<Contact> findAll();
    public abstract Contact findById(Long id);
}
