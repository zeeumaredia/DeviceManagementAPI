package com.devicesapi.dto.response;

import java.time.Instant;

public class DeviceResponse {

    private Long Id;
    private String name;
    private String brand;
    private String state;
    private Instant createAt;

    public Long getId() { return Id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getState() { return state; }
    public Instant getCreateAt() { return createAt; }
}
