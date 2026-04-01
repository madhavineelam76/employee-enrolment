package employeeEnrollment.service;

import java.util.List;

import employeeEnrollment.dto.EmployeeRequestDTO;
import employeeEnrollment.dto.EmployeeResponseDTO;
import employeeEnrollment.entity.Status;

public interface EmployeeService {

	EmployeeResponseDTO enrollEmployee(EmployeeRequestDTO dto);

    List<EmployeeResponseDTO> getAllEmployee();

	EmployeeResponseDTO getEmployeeById(Long id);
	
	EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto);
	
	EmployeeResponseDTO updateStatus(Long id, Status status);
	
	EmployeeResponseDTO getByUsername(String username);
	
	EmployeeResponseDTO updateByUsername(String username, EmployeeRequestDTO dto);
}
