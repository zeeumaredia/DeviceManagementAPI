package com.devicesapi.controller;

import com.devicesapi.dto.request.CreateDeviceRequest;
import com.devicesapi.dto.request.UpdateDeviceRequest;
import com.devicesapi.dto.response.DeviceResponse;
import com.devicesapi.service.DeviceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeviceController.class)
class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeviceService deviceService;

    @Test
    @DisplayName("POST /devices - success")
    void testCreateDevice() throws Exception {
        CreateDeviceRequest request = new CreateDeviceRequest();
        request.setName("Phone X");
        request.setBrand("Samsung");
        request.setState("AVAILABLE");

        DeviceResponse response = DeviceResponse.builder()
                .id(1L)
                .name("Phone X")
                .brand("Samsung")
                .state("AVAILABLE")
                .createdAt(Instant.now())
                .build();

        Mockito.when(deviceService.createDevice(any(CreateDeviceRequest.class))).thenReturn(response);

        mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.brand").value(response.getBrand()))
                .andExpect(jsonPath("$.state").value(response.getState()));
    }

    @Test
    @DisplayName("GET /devices/{id} - success")
    void testGetDeviceById() throws Exception {
        DeviceResponse response = DeviceResponse.builder()
                .id(1L)
                .name("Phone X")
                .brand("Samsung")
                .state("AVAILABLE")
                .createdAt(Instant.now())
                .build();

        Mockito.when(deviceService.getDeviceById(1L)).thenReturn(response);

        mockMvc.perform(get("/devices/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.brand").value(response.getBrand()))
                .andExpect(jsonPath("$.state").value(response.getState()));
    }

    @Test
    @DisplayName("GET /devices - get all devices")
    void testGetAllDevices() throws Exception {
        DeviceResponse device1 = DeviceResponse.builder()
                .id(1L).name("Phone X").brand("Samsung").state("AVAILABLE").createdAt(Instant.now()).build();
        DeviceResponse device2 = DeviceResponse.builder()
                .id(2L).name("Tablet Y").brand("Apple").state("IN_USE").createdAt(Instant.now()).build();

        Mockito.when(deviceService.getAllDevices()).thenReturn(List.of(device1, device2));

        mockMvc.perform(get("/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @DisplayName("GET /devices?brand=Samsung - filter by brand")
    void testGetDevicesByBrand() throws Exception {
        DeviceResponse device1 = DeviceResponse.builder()
                .id(1L).name("Phone X").brand("Samsung").state("AVAILABLE").createdAt(Instant.now()).build();

        Mockito.when(deviceService.getDevicesByBrand("Samsung")).thenReturn(List.of(device1));

        mockMvc.perform(get("/devices").param("brand", "Samsung"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].brand").value("Samsung"));
    }

    @Test
    @DisplayName("GET /devices?state=AVAILABLE - filter by state")
    void testGetDevicesByState() throws Exception {
        DeviceResponse device1 = DeviceResponse.builder()
                .id(1L).name("Phone X").brand("Samsung").state("AVAILABLE").createdAt(Instant.now()).build();

        Mockito.when(deviceService.getDevicesByState("AVAILABLE")).thenReturn(List.of(device1));

        mockMvc.perform(get("/devices").param("state", "AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].state").value("AVAILABLE"));
    }

    @Test
    @DisplayName("PATCH /devices/{id} - update device")
    void testUpdateDevice() throws Exception {
        UpdateDeviceRequest request = new UpdateDeviceRequest();
        request.setState("IN_USE");

        DeviceResponse updatedResponse = DeviceResponse.builder()
                .id(1L)
                .name("Phone X")
                .brand("Samsung")
                .state("IN_USE")
                .createdAt(Instant.now())
                .build();

        Mockito.when(deviceService.updateDevice(eq(1L), any(UpdateDeviceRequest.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(patch("/devices/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("IN_USE"));
    }

    @Test
    @DisplayName("DELETE /devices/{id} - delete device")
    void testDeleteDevice() throws Exception {
        Mockito.doNothing().when(deviceService).deleteDevice(1L);

        mockMvc.perform(delete("/devices/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
