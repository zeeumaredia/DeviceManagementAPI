package com.devicesapi.repository;

import com.devicesapi.domain.Device;
import com.devicesapi.domain.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device,Long> {
    List<Device> getDevicesByBrand(String brand);
    List<Device> getDevicesByState(DeviceState state);
}