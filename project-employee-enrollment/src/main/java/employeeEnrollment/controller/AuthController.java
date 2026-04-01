package employeeEnrollment.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import employeeEnrollment.dto.EmployeeRequestDTO;
import employeeEnrollment.dto.LoginRequestDTO;
import employeeEnrollment.dto.LoginResponseDTO;
import employeeEnrollment.service.AuthService;
import employeeEnrollment.util.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequestDTO request) {

        LoginResponseDTO response = service.login(request);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Login successful",
                        response,
                        LocalDateTime.now()
                )
        );
    }
    
    @PostMapping("/register-admin")
    public ResponseEntity<ApiResponse> registerAdmin(@Valid @RequestBody EmployeeRequestDTO dto) {

        dto.setRole("ADMIN"); // force role to ADMIN

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Admin created successfully",
                        service.enrollEmployee(dto),
                        LocalDateTime.now()
                )
        );
    }
}