package com.example.androidMapApp;


public class Localisation {
    public String sitename, description, date;
    public double latitude, longitude;
    int numberVotes, sumVotes;

    public Localisation(String username, String description, String date, double latitude, double longitude) {
        this.sitename = username;
        this.description = description;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Localisation(String username, String description, String date, double latitude, double longitude, int numberVotes,
                        int sumVotes ) {
        this.sitename = username;
        this.description = description;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numberVotes = numberVotes;
        this.sumVotes = sumVotes;
    }

    @Override
    public String toString() {
        return "Location{" +
                "username='" + sitename + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
