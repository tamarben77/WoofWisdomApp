package com.example.woofwisdomapplication.DTO;

public class Vet {
    private String name;
    private String address;
    private double rating;

    public Vet(String name, String address, double rating) {
        this.name = name;
        this.address = address;
        this.rating = rating;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}

