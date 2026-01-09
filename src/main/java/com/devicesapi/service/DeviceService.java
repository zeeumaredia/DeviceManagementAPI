package com.devicesapi.service;

import com.devicesapi.dto.request.CreateDeviceRequest;
import com.devicesapi.dto.request.UpdateDeviceRequest;
import com.devicesapi.dto.response.DeviceResponse;

import java.util.List;

public interface DeviceService {
    DeviceResponse createDevice(CreateDeviceRequest deviceRequest);
    DeviceResponse getDeviceById(Long id);
    List<DeviceResponse> getAllDevices();
    List<DeviceResponse> getDevicesByBrand(String brand);
    List<DeviceResponse> getDevicesByState(String state);
    DeviceResponse updateDevice(UpdateDeviceRequest updateDeviceRequest);
    void deleteDevice(Long id);
}
