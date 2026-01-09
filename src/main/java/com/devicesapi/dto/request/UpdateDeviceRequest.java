package com.devicesapi.dto.request;

public class UpdateDeviceRequest {

    private String name;
    private String brand;
    private String state;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}
