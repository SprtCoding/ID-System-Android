package com.sprtcoding.semiraraidapp;

public class UserData {
    private String name;
    private String email;
    private String phone;
    private String address;

    public UserData() {
    }

    public UserData(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}
