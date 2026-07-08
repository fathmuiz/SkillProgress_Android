package com.skillprogress.app.model;

public class User {
    private int id;
    private String nama;
    private String email;

    public User(int id, String nama, String email) {
        this.id = id;
        this.nama = nama;
        this.email = email;
    }

    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
}
