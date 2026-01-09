package com.devicesapi.dto.response;

import java.time.Instant;

public class DeviceResponse {

    private final Long id;
    private final String name;
    private final String brand;
    private final String state;
    private final Instant createdAt;

    private DeviceResponse(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.brand = builder.brand;
        this.state = builder.state;
        this.createdAt = builder.createdAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getState() { return state; }
    public Instant getCreatedAt() { return createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private String brand;
        private String state;
        private Instant createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder brand(String brand) { this.brand = brand; return this; }
        public Builder state(String state) { this.state = state; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public DeviceResponse build() {
            return new DeviceResponse(this);
        }
    }
}
