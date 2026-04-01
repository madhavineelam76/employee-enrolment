package employeeEnrollment.service;

import employeeEnrollment.dto.EmployeeRequestDTO;
import employeeEnrollment.dto.LoginRequestDTO;
import employeeEnrollment.dto.LoginResponseDTO;
import jakarta.validation.Valid;

public interface AuthService {

	LoginResponseDTO login(LoginRequestDTO request);

	Object enrollEmployee(@Valid EmployeeRequestDTO dto);
	
}
