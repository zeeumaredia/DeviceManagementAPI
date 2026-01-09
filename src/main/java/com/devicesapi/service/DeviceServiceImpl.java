package com.devicesapi.service;

import com.devicesapi.domain.Device;
import com.devicesapi.domain.DeviceState;
import com.devicesapi.dto.request.CreateDeviceRequest;
import com.devicesapi.dto.request.UpdateDeviceRequest;
import com.devicesapi.dto.response.DeviceResponse;
import com.devicesapi.exceptions.DeviceNotFoundException;
import com.devicesapi.exceptions.DeviceOperationException;
import com.devicesapi.repository.DeviceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeviceServiceImpl implements DeviceService{

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public DeviceResponse createDevice(CreateDeviceRequest request) {
        DeviceState state = request.getState() != null ?
                DeviceState.valueOf(request.getState().toUpperCase()) :
                DeviceState.AVAILABLE;

        Device device = new Device(request.getName(), request.getBrand(), state);
        device.setCreatedAt(Instant.now());

        deviceRepository.save(device);
        return toResponse(device);
    }

    @Override
    public DeviceResponse getDeviceById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device with Id "+ id + "not found "));
        return toResponse(device);
    }

    @Override
    public List<DeviceResponse> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceResponse> getDevicesByBrand(String brand) {
        return deviceRepository.getDevicesByBrand(brand).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceResponse> getDevicesByState(String state) {
        DeviceState deviceState = DeviceState.valueOf(state.toUpperCase());
        return deviceRepository.getDevicesByState(deviceState).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DeviceResponse updateDevice(Long id, UpdateDeviceRequest request) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found"));

        if (device.getState() == DeviceState.IN_USE) {
            if (request.getName() != null || request.getBrand() != null)
                throw new DeviceOperationException("Cannot update name or brand of device in use");
        }

        if (request.getName() != null) device.setName(request.getName());
        if (request.getBrand() != null) device.setBrand(request.getBrand());
        if (request.getState() != null)
            device.setState(DeviceState.valueOf(request.getState().toUpperCase()));

        deviceRepository.save(device);
        return toResponse(device);
    }

    @Override
    public void deleteDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found"));

        if (device.getState() == DeviceState.IN_USE)  // Preventing D
            throw new DeviceOperationException("Cannot delete a device that is in use");

        deviceRepository.delete(device);
    }

    private DeviceResponse toResponse(Device device) {
        return DeviceResponse.builder()
                .id(device.getId())
                .name(device.getName())
                .brand(device.getBrand())
                .state(device.getState().name())
                .createdAt(device.getCreatedAt())
                .build();
    }
}
