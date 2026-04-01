package employeeEnrollment.serviceImplementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import employeeEnrollment.dto.EmployeeRequestDTO;
import employeeEnrollment.dto.EmployeeResponseDTO;
import employeeEnrollment.entity.EmployeeEntity;
import employeeEnrollment.entity.Status;
import employeeEnrollment.mapper.EmployeeMapper;
import employeeEnrollment.repository.EmployeeRepository;
import employeeEnrollment.service.EmployeeService;

@Transactional
@Service
public class EmployeeServiceImplementation implements EmployeeService {

	private final EmployeeRepository repository ;

    

    public EmployeeServiceImplementation(EmployeeRepository repository) {
		this.repository = repository;
	}

	@Override
    public EmployeeResponseDTO enrollEmployee(EmployeeRequestDTO dto) {
        EmployeeEntity entity = EmployeeMapper.toEntity(dto);
        repository.save(entity);
        return EmployeeMapper.toDTO(entity);
    }

    @Override
    public List<EmployeeResponseDTO> getAllEmployee() {
        return repository.findAll()
                .stream()
                .map(EmployeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {
        EmployeeEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return EmployeeMapper.toDTO(entity);
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
        EmployeeEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

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

        repository.save(entity);
        return EmployeeMapper.toDTO(entity);
    }

    @Override
    public String updateStatus(Long id, String status) {
        EmployeeEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        entity.setStatus(Status.valueOf(status.toUpperCase()));
        repository.save(entity);
        return "Status updated to " + status;
    }

    @Override
    public EmployeeResponseDTO getByUsername(String username) {
        EmployeeEntity entity = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return EmployeeMapper.toDTO(entity);
    }

    @Override
    public EmployeeResponseDTO updateByUsername(String username, EmployeeRequestDTO dto) {
        EmployeeEntity entity = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setPassword(dto.getPassword());
        repository.save(entity);

        return EmployeeMapper.toDTO(entity);
    }
}
