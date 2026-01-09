package com.devicesapi.controller;

import com.devicesapi.dto.request.CreateDeviceRequest;
import com.devicesapi.dto.request.UpdateDeviceRequest;
import com.devicesapi.dto.response.DeviceResponse;
import com.devicesapi.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
@Tag(
        name = "Devices",
        description = "Operations for managing device resources"
)
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // ---------------- CREATE ----------------

    @Operation(
            summary = "Create a new device",
            description = "Creates a new device with name, brand and initial state"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device successfully created",
                    content = @Content(schema = @Schema(implementation = DeviceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content)
    })
    @PostMapping
    public DeviceResponse create(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Device creation request",
                    required = true
            )
            CreateDeviceRequest request
    ) {
        return deviceService.createDevice(request);
    }

    // ---------------- READ (BY ID) ----------------

    @Operation(
            summary = "Fetch a device by ID",
            description = "Returns a single device by its unique identifier"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device found",
                    content = @Content(schema = @Schema(implementation = DeviceResponse.class))),
            @ApiResponse(responseCode = "404", description = "Device not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public DeviceResponse get(
            @Parameter(
                    name = "id",
                    description = "Unique device identifier",
                    required = true,
                    in = ParameterIn.PATH
            )
            @PathVariable Long id
    ) {
        return deviceService.getDeviceById(id);
    }

    // ---------------- READ (FILTERED / ALL) ----------------

    @Operation(
            summary = "Fetch devices",
            description = "Fetches all devices or filters them by brand or state"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Devices retrieved successfully")
    })
    @GetMapping
    public List<DeviceResponse> getAll(
            @Parameter(
                    name = "brand",
                    description = "Filter devices by brand",
                    example = "Samsung",
                    in = ParameterIn.QUERY
            )
            @RequestParam(required = false) String brand,

            @Parameter(
                    name = "state",
                    description = "Filter devices by state (AVAILABLE, IN_USE, INACTIVE)",
                    example = "AVAILABLE",
                    in = ParameterIn.QUERY
            )
            @RequestParam(required = false) String state
    ) {
        if (brand != null) {
            return deviceService.getDevicesByBrand(brand);
        }
        if (state != null) {
            return deviceService.getDevicesByState(state);
        }
        return deviceService.getAllDevices();
    }

    // ---------------- UPDATE ----------------

    @Operation(
            summary = "Update an existing device",
            description = "Partially updates device fields. Creation time cannot be updated. Name and brand cannot be updated if device is IN_USE."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device updated successfully",
                    content = @Content(schema = @Schema(implementation = DeviceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid update request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Device not found",
                    content = @Content)
    })
    @PatchMapping("/{id}")
    public DeviceResponse update(
            @Parameter(
                    name = "id",
                    description = "Unique device identifier",
                    required = true,
                    in = ParameterIn.PATH
            )
            @PathVariable Long id,

            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Fields to update on the device",
                    required = true
            )
            UpdateDeviceRequest request
    ) {
        return deviceService.updateDevice(id, request);
    }

    // ---------------- DELETE ----------------

    @Operation(
            summary = "Delete a device",
            description = "Deletes a device by ID. Devices in IN_USE state cannot be deleted."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Device deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Device cannot be deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Device not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(
                    name = "id",
                    description = "Unique device identifier",
                    required = true,
                    in = ParameterIn.PATH
            )
            @PathVariable Long id
    ) {
        deviceService.deleteDevice(id);
    }
}
