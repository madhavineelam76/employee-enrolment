package employeeEnrollment.service;

import employeeEnrollment.dto.EmployeeRequestDTO;
import employeeEnrollment.dto.EmployeeResponseDTO;
import employeeEnrollment.dto.LoginRequestDTO;
import employeeEnrollment.dto.LoginResponseDTO;
import jakarta.validation.Valid;

public interface AuthService {

	LoginResponseDTO login(LoginRequestDTO request);

	EmployeeResponseDTO enrollEmployee(@Valid EmployeeRequestDTO dto);
	
}
