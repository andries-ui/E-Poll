package com.example.votingsystem.DataClasss;


import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

public class PartyClass {

    String key;
    String leader_name;
    String ID;
    String party_name;
    String email;
    String city;
    String url;
    String contact;
    String status;
    String nominated;

    public PartyClass() {
    }

    public PartyClass(String key, String leader_name, String ID, String party_name, String email, String city, String url, String contact, String status, String nominated) {
        this.key = key;
        this.leader_name = leader_name;
        this.ID = ID;
        this.party_name = party_name;
        this.email = email;
        this.city = city;
        this.url = url;
        this.contact = contact;
        this.status = status;
        this.nominated = nominated;
    }

    public String getKey() {
        return key;
    }

    public String getLeader_name() {
        return leader_name;
    }

    public String getID() {
        return ID;
    }

    public String getParty_name() {
        return party_name;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getUrl() {
        return url;
    }

    public String getContact() {
        return contact;
    }

    public String getStatus() {
        return status;
    }

    public String getNominated() {
        return nominated;
    }
}
