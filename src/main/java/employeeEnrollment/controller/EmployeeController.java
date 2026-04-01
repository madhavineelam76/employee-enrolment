package employeeEnrollment.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import employeeEnrollment.dto.EmployeeRequestDTO;
import employeeEnrollment.dto.EmployeeStatusUpdateRequestDTO;
import employeeEnrollment.service.EmployeeService;
import employeeEnrollment.util.ApiResponse;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping("/enroll")
    public ResponseEntity<ApiResponse> enroll(@Valid @RequestBody EmployeeRequestDTO dto) {
        return ResponseEntity.status(201).body(new ApiResponse(
                201,
                "Employee enrolled successfully",
                service.enrollEmployee(dto),
                LocalDateTime.now()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "Employees fetched successfully",
                service.getAllEmployee(),
                LocalDateTime.now()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "Employee fetched successfully",
                service.getEmployeeById(id),
                LocalDateTime.now()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDTO dto) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "Employee updated successfully",
                service.updateEmployee(id, dto),
                LocalDateTime.now()));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeStatusUpdateRequestDTO request) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "Employee status updated successfully",
                service.updateStatus(id, request.getStatus()),
                LocalDateTime.now()));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getProfile(Authentication auth) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "Employee profile fetched successfully",
                service.getByUsername(auth.getName()),
                LocalDateTime.now()));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse> updateProfile(Authentication auth, @Valid @RequestBody EmployeeRequestDTO dto) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "Employee profile updated successfully",
                service.updateByUsername(auth.getName(), dto),
                LocalDateTime.now()));
    }
}
