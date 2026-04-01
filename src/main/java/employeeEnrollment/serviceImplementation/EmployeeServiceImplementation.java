package employeeEnrollment.serviceImplementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import employeeEnrollment.dto.EmployeeRequestDTO;
import employeeEnrollment.dto.EmployeeResponseDTO;
import employeeEnrollment.entity.EmployeeEntity;
import employeeEnrollment.entity.Status;
import employeeEnrollment.exception.DuplicateResourceException;
import employeeEnrollment.exception.ResourceNotFoundException;
import employeeEnrollment.mapper.EmployeeMapper;
import employeeEnrollment.repository.EmployeeRepository;
import employeeEnrollment.service.EmployeeService;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class EmployeeServiceImplementation implements EmployeeService {

    private static final double DEFAULT_SALARY = 30000.0;

    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeServiceImplementation(EmployeeRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public EmployeeResponseDTO enrollEmployee(EmployeeRequestDTO dto) {
        validateUniqueFields(dto.getEmail(), dto.getUsername(), null);

        EmployeeEntity entity = EmployeeMapper.toEntity(dto);
        entity.setRole(normalizeRole(dto.getRole()));
        entity.setSalary(resolveSalary(dto.getSalary(), null));
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setStatus(Status.ACTIVE);

        return EmployeeMapper.toDTO(repository.save(entity));
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
        return EmployeeMapper.toDTO(findById(id));
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
        EmployeeEntity entity = findById(id);
        validateUniqueFields(dto.getEmail(), dto.getUsername(), id);

        applyFullUpdate(entity, dto);
        return EmployeeMapper.toDTO(repository.save(entity));
    }

    @Override
    public EmployeeResponseDTO updateStatus(Long id, Status status) {
        EmployeeEntity entity = findById(id);
        entity.setStatus(status);
        return EmployeeMapper.toDTO(repository.save(entity));
    }

    @Override
    public EmployeeResponseDTO getByUsername(String username) {
        return EmployeeMapper.toDTO(findByUsername(username));
    }

    @Override
    public EmployeeResponseDTO updateByUsername(String username, EmployeeRequestDTO dto) {
        EmployeeEntity entity = findByUsername(username);
        validateUniqueFields(dto.getEmail(), dto.getUsername(), entity.getEmp_id());
        String existingRole = entity.getRole();
        Status existingStatus = entity.getStatus();

        applyFullUpdate(entity, dto);
        entity.setRole(normalizeRole(existingRole));
        entity.setStatus(existingStatus == null ? Status.ACTIVE : existingStatus);

        return EmployeeMapper.toDTO(repository.save(entity));
    }

    private void applyFullUpdate(EmployeeEntity entity, EmployeeRequestDTO dto) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setDepartment(dto.getDepartment());
        entity.setRole(normalizeRole(dto.getRole()));
        entity.setSalary(resolveSalary(dto.getSalary(), entity.getSalary()));
        entity.setJoiningDate(dto.getJoiningDate());
        entity.setAddress(dto.getAddress());
        entity.setUsername(dto.getUsername());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setStatus(entity.getStatus() == null ? Status.ACTIVE : entity.getStatus());
    }

    private EmployeeEntity findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    private EmployeeEntity findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with username: " + username));
    }

    private void validateUniqueFields(String email, String username, Long currentEmployeeId) {
        repository.findByEmail(email)
                .filter(employee -> !employee.getEmp_id().equals(currentEmployeeId))
                .ifPresent(employee -> {
                    throw new DuplicateResourceException("Email already exists");
                });

        repository.findByUsername(username)
                .filter(employee -> !employee.getEmp_id().equals(currentEmployeeId))
                .ifPresent(employee -> {
                    throw new DuplicateResourceException("Username already exists");
                });
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "EMPLOYEE";
        }
        return role.replace("ROLE_", "").trim().toUpperCase();
    }

    private Double resolveSalary(Double incomingSalary, Double existingSalary) {
        if (incomingSalary != null && incomingSalary > 0) {
            return incomingSalary;
        }
        if (existingSalary != null && existingSalary > 0) {
            return existingSalary;
        }
        return DEFAULT_SALARY;
    }
}
