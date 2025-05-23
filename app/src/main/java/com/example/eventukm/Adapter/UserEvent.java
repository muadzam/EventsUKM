package com.example.eventukm.Adapter;

public class UserEvent {
    private String name;
    private String matrix;
    private String email;
    private String phone;
    private String receiptURL;
    private String eventName;
    private String eventTime;
    private String eventDate;
    private String eventFee;
    private String eventMerit;
    private String eventLocation;
    private String organiserUID;

    public UserEvent() {
    }

    public UserEvent(String name, String matrix, String email, String phone, String receiptURL, String eventName, String eventTime, String eventDate, String eventFee, String eventMerit, String eventLocation, String organiserUID) {
        this.name = name;
        this.matrix = matrix;
        this.email = email;
        this.phone = phone;
        this.receiptURL = receiptURL;
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.eventDate = eventDate;
        this.eventFee = eventFee;
        this.eventMerit = eventMerit;
        this.eventLocation = eventLocation;
        this.organiserUID = organiserUID;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getName() {
        return name;
    }

    public String getMatrix() {
        return matrix;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getReceiptURL() {
        return receiptURL;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventFee() {
        return eventFee;
    }

    public String getEventMerit() {
        return eventMerit;
    }

    public String getOrganiserUID() {
        return organiserUID;
    }

    public void setOrganiserUID(String organiserUID) {
        this.organiserUID = organiserUID;
    }

}
