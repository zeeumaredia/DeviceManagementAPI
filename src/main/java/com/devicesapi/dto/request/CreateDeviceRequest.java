package com.devicesapi.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateDeviceRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    private String state;

    public String getName() { return name; }
    public String getState() { return state; }
    public String getBrand() { return brand; }

    public void setName(String name) { this.name = name; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setState(String state) { this.state = state; }
}
