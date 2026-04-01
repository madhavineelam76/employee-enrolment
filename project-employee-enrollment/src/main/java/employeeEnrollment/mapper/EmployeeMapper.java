package employeeEnrollment.mapper;

import employeeEnrollment.dto.EmployeeRequestDTO;
import employeeEnrollment.dto.EmployeeResponseDTO;
import employeeEnrollment.entity.EmployeeEntity;

public class EmployeeMapper {

    public static EmployeeResponseDTO toDTO(EmployeeEntity employee) {
        if (employee == null) return null;
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setEmpId(employee.getEmp_id());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setDepartment(employee.getDepartment());
        dto.setRole(employee.getRole());
        dto.setSalary(employee.getSalary());
        dto.setJoiningDate(employee.getJoiningDate());
        dto.setAddress(employee.getAddress());
        dto.setUsername(employee.getUsername());
        dto.setStatus(employee.getStatus());
        return dto;
    }

    public static EmployeeEntity toEntity(EmployeeRequestDTO dto) {
        if (dto == null) return null;
        EmployeeEntity entity = new EmployeeEntity();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setDepartment(dto.getDepartment());
        entity.setRole(dto.getRole());
        entity.setSalary(dto.getSalary());
        entity.setJoiningDate(dto.getJoiningDate());
        entity.setAddress(dto.getAddress());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setStatus(employeeEnrollment.entity.Status.ACTIVE);
        return entity;
    }
}
