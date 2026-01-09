package com.devicesapi.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceState state;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    // Instant is Immutable, which prevents Domain Violation # 1

    public Device(Long id, String name, String brand, DeviceState state, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.state = state;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getBrand() {
        return brand;
    }
    public DeviceState getState() {
        return state;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public void setState(DeviceState state) {
        this.state = state;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
