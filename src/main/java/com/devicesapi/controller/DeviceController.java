package com.devicesapi.controller;

import com.devicesapi.dto.request.CreateDeviceRequest;
import com.devicesapi.dto.request.UpdateDeviceRequest;
import com.devicesapi.dto.response.DeviceResponse;
import com.devicesapi.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public DeviceResponse create(@Valid @RequestBody CreateDeviceRequest request) {
        return deviceService.createDevice(request);
    }

    @GetMapping("/{id}")
    public DeviceResponse get(@PathVariable Long id) {
        return deviceService.getDeviceById(id);
    }

    @GetMapping
    public List<DeviceResponse> getAll(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String state) {

        if (brand != null) return deviceService.getDevicesByBrand(brand);
        if (state != null) return deviceService.getDevicesByState(state);

        return deviceService.getAllDevices();
    }



    @PatchMapping("/{id}")
    public DeviceResponse update(@PathVariable Long id, @RequestBody UpdateDeviceRequest request) {
        return deviceService.updateDevice(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deviceService.deleteDevice(id);
    }
}
