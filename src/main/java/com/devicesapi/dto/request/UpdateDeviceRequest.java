package com.devicesapi.dto.request;

public class UpdateDeviceRequest {
    private String name;
    private String brand;
    private String state;

    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getState() { return state; }

    public void setName(String name) { this.name = name; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setState(String state) { this.state = state; }

}
