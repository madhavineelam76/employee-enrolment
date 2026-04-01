package employeeEnrollment.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // ✅ FIXED
import org.springframework.web.bind.annotation.*;

import employeeEnrollment.dto.EmployeeRequestDTO;
import employeeEnrollment.service.EmployeeService;
import employeeEnrollment.util.ApiResponse;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    // ============================
    // ✅ ADMIN APIs
    // ============================

    @PostMapping("/admin/employees")
    public ResponseEntity<ApiResponse> enroll(@Valid @RequestBody EmployeeRequestDTO dto) {

        return ResponseEntity.ok(
                new ApiResponse(200, "Employee created",
                        service.enrollEmployee(dto),
                        LocalDateTime.now())
        );
    }

    @GetMapping("/admin/employees")
    public ResponseEntity<ApiResponse> getAll() {

        return ResponseEntity.ok(
                new ApiResponse(200, "Success",
                        service.getAllEmployee(),
                        LocalDateTime.now())
        );
    }

    @GetMapping("/admin/employees/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse(200, "Success",
                        service.getEmployeeById(id),
                        LocalDateTime.now())
        );
    }

    @PutMapping("/admin/employees/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id,
                                              @Valid @RequestBody EmployeeRequestDTO dto) {

        return ResponseEntity.ok(
                new ApiResponse(200, "Employee updated",
                        service.updateEmployee(id, dto),
                        LocalDateTime.now())
        );
    }

    @PutMapping("/admin/employees/{id}/status")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Long id,
                                                    @RequestParam String status) {

        return ResponseEntity.ok(
                new ApiResponse(200,
                        service.updateStatus(id, status),
                        null,
                        LocalDateTime.now())
        );
    }

    // ============================
    // ✅ EMPLOYEE APIs
    // ============================

    // 🔹 GET own profile
    @GetMapping("/employee/profile")
    public ResponseEntity<ApiResponse> getProfile(Authentication auth) {

        String username = auth.getName();

        return ResponseEntity.ok(
                new ApiResponse(200, "Success",
                        service.getByUsername(username),
                        LocalDateTime.now())
        );
    }

    // 🔹 UPDATE own profile
    @PutMapping("/employee/profile")
    public ResponseEntity<ApiResponse> updateProfile(Authentication auth,
                                                     @Valid @RequestBody EmployeeRequestDTO dto) {

        String username = auth.getName();

        return ResponseEntity.ok(
                new ApiResponse(200, "Profile updated",
                        service.updateByUsername(username, dto),
                        LocalDateTime.now())
        );
    }
}