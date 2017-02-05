package com.example.phuong.blockcallapp.eventbus;

import com.example.phuong.blockcallapp.models.Contact;

import java.util.List;

/**
 * Created by phuong on 05/02/2017.
 */

public class ObjectListContact {
    private List<Contact> contacts;

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public ObjectListContact(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public ObjectListContact() {
    }
}
