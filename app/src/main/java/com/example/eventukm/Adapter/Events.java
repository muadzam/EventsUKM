package com.example.eventukm.Adapter;

import java.io.Serializable;


public class Events implements Serializable {
    private String name;
    private String date;
    private String time;
    private String fee;
    private String description;
    private  String type;
    private String merit;
    private String location;
    private String imageURL;
    private String qrimageURL;
    private String organiserUID;

    public Events(String name, String date, String time, String fee, String description, String type, String merit, String location, String imageURL, String qrimageURL, String organiserUID) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.fee = fee;
        this.description = description;
        this.type = type;
        this.merit = merit;
        this.location = location;
        this.imageURL = imageURL;
        this.qrimageURL = qrimageURL;
        this.organiserUID = organiserUID;
    }

    public Events() {
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getFee() {
        return fee;
    }

    public String getDescription() {
        return description;
    }

    public String getMerit() {
        return merit;
    }

    public String getType(){
        return type;
    }

    public String getLocation() {
        return location;
    }

    public String getImageURL() {return imageURL;}

    public String getQrimageURL() {return qrimageURL;}

    public String getOrganiserUID() {
        return organiserUID;
    }

    public void setOrganiserUID(String organiserUID) {
        this.organiserUID = organiserUID;
    }

}
