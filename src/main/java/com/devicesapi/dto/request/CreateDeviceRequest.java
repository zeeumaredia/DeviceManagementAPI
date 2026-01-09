package com.devicesapi.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateDeviceRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    private String state; // optional, defaults to AVAILABLE

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}
