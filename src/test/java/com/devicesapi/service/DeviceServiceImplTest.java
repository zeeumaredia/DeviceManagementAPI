package com.devicesapi.service;

import com.devicesapi.domain.Device;
import com.devicesapi.domain.DeviceState;
import com.devicesapi.dto.request.CreateDeviceRequest;
import com.devicesapi.dto.request.UpdateDeviceRequest;
import com.devicesapi.dto.response.DeviceResponse;
import com.devicesapi.exceptions.DeviceNotFoundException;
import com.devicesapi.exceptions.DeviceOperationException;
import com.devicesapi.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeviceServiceImplTest {

    private DeviceRepository deviceRepository;
    private DeviceServiceImpl deviceService;

    @BeforeEach
    void setup() {
        deviceRepository = mock(DeviceRepository.class);
        deviceService = new DeviceServiceImpl(deviceRepository);
    }

    // ---------------- CREATE ----------------
    @Test
    void testCreateDevice_withProvidedState() {
        CreateDeviceRequest request = new CreateDeviceRequest();
        request.setName("Tablet A");
        request.setBrand("Apple");
        request.setState("IN_USE");

        Device device = new Device("Tablet A", "Apple", DeviceState.IN_USE);
        device.setId(1L);
        device.setCreatedAt(Instant.now());

        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        DeviceResponse response = deviceService.createDevice(request);

        assertEquals(DeviceState.IN_USE.name(), response.getState());
        assertEquals("Tablet A", response.getName());
        verify(deviceRepository).save(any(Device.class));
    }

    @Test
    void testCreateDevice_withDefaultState() {
        CreateDeviceRequest request = new CreateDeviceRequest();
        request.setName("Phone B");
        request.setBrand("Samsung");
        request.setState(null);

        Device device = new Device("Phone B", "Samsung", DeviceState.AVAILABLE);
        device.setId(2L);
        device.setCreatedAt(Instant.now());

        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        DeviceResponse response = deviceService.createDevice(request);

        assertEquals(DeviceState.AVAILABLE.name(), response.getState());
    }

    // ---------------- GET BY ID ----------------
    @Test
    void testGetDeviceById_success() {
        Device device = new Device("Device 1", "BrandX", DeviceState.AVAILABLE);
        device.setId(1L);
        device.setCreatedAt(Instant.now());

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        DeviceResponse response = deviceService.getDeviceById(1L);

        assertEquals(1L, response.getId());
    }

    @Test
    void testGetDeviceById_notFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> deviceService.getDeviceById(1L));
    }

    // ---------------- GET ALL / FILTER ----------------
    @Test
    void testGetAllDevices_returnsList() {
        Device d1 = new Device("Phone X", "Samsung", DeviceState.AVAILABLE);
        d1.setId(1L);
        Device d2 = new Device("Tablet Y", "Apple", DeviceState.IN_USE);
        d2.setId(2L);

        when(deviceRepository.findAll()).thenReturn(List.of(d1, d2));

        List<DeviceResponse> responses = deviceService.getAllDevices();

        assertEquals(2, responses.size());
    }

    @Test
    void testGetAllDevices_emptyList() {
        when(deviceRepository.findAll()).thenReturn(Collections.emptyList());
        List<DeviceResponse> responses = deviceService.getAllDevices();
        assertTrue(responses.isEmpty());
    }

    @Test
    void testGetDevicesByBrand_success() {
        Device device = new Device("Phone Z", "Samsung", DeviceState.AVAILABLE);
        device.setId(1L);

        when(deviceRepository.getDevicesByBrand("Samsung")).thenReturn(List.of(device));

        List<DeviceResponse> responses = deviceService.getDevicesByBrand("Samsung");

        assertEquals(1, responses.size());
        assertEquals("Samsung", responses.get(0).getBrand());
    }

    @Test
    void testGetDevicesByBrand_emptyList() {
        when(deviceRepository.getDevicesByBrand("XYZ")).thenReturn(Collections.emptyList());
        List<DeviceResponse> responses = deviceService.getDevicesByBrand("XYZ");
        assertTrue(responses.isEmpty());
    }

    @Test
    void testGetDevicesByState_success() {
        Device device = new Device("Device A", "BrandA", DeviceState.AVAILABLE);
        device.setId(1L);

        when(deviceRepository.getDevicesByState(DeviceState.AVAILABLE)).thenReturn(List.of(device));

        List<DeviceResponse> responses = deviceService.getDevicesByState("AVAILABLE");

        assertEquals(1, responses.size());
        assertEquals("AVAILABLE", responses.get(0).getState());
    }

    @Test
    void testGetDevicesByState_invalidState() {
        assertThrows(IllegalArgumentException.class, () -> deviceService.getDevicesByState("XYZ"));
    }

    // ---------------- UPDATE ----------------
    @Test
    void testUpdateDevice_successAllFields() {
        Device device = new Device("Old Name", "Old Brand", DeviceState.AVAILABLE);
        device.setId(1L);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        when(deviceRepository.save(device)).thenReturn(device);

        UpdateDeviceRequest request = new UpdateDeviceRequest();
        request.setName("New Name");
        request.setBrand("New Brand");
        request.setState("IN_USE");

        DeviceResponse response = deviceService.updateDevice(1L, request);

        assertEquals("New Name", response.getName());
        assertEquals("New Brand", response.getBrand());
        assertEquals("IN_USE", response.getState());
    }

    @Test
    void testUpdateDevice_inUseNameChange_throws() {
        Device device = new Device("Old Name", "BrandX", DeviceState.IN_USE);
        device.setId(1L);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        UpdateDeviceRequest request = new UpdateDeviceRequest();
        request.setName("New Name");

        assertThrows(DeviceOperationException.class, () -> deviceService.updateDevice(1L, request));
    }

    @Test
    void testUpdateDevice_inUseStateChange_allowed() {
        Device device = new Device("Name", "BrandX", DeviceState.IN_USE);
        device.setId(1L);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        when(deviceRepository.save(device)).thenReturn(device);

        UpdateDeviceRequest request = new UpdateDeviceRequest();
        request.setState("AVAILABLE");

        DeviceResponse response = deviceService.updateDevice(1L, request);

        assertEquals("AVAILABLE", response.getState());
    }

    @Test
    void testUpdateDevice_notFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateDeviceRequest request = new UpdateDeviceRequest();
        request.setState("AVAILABLE");

        assertThrows(DeviceNotFoundException.class, () -> deviceService.updateDevice(1L, request));
    }

    @Test
    void testUpdateDevice_invalidState() {
        Device device = new Device("Name", "BrandX", DeviceState.AVAILABLE);
        device.setId(1L);
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        UpdateDeviceRequest request = new UpdateDeviceRequest();
        request.setState("XYZ");

        assertThrows(IllegalArgumentException.class, () -> deviceService.updateDevice(1L, request));
    }

    // ---------------- DELETE ----------------
    @Test
    void testDeleteDevice_success() {
        Device device = new Device("Device", "Brand", DeviceState.AVAILABLE);
        device.setId(1L);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        deviceService.deleteDevice(1L);

        verify(deviceRepository).delete(device);
    }

    @Test
    void testDeleteDevice_inUse_throws() {
        Device device = new Device("Device", "Brand", DeviceState.IN_USE);
        device.setId(1L);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        assertThrows(DeviceOperationException.class, () -> deviceService.deleteDevice(1L));
    }

    @Test
    void testDeleteDevice_notFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> deviceService.deleteDevice(1L));
    }
}
