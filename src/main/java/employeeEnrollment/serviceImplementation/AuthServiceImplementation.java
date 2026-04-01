package employeeEnrollment.serviceImplementation;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import employeeEnrollment.dto.EmployeeRequestDTO;
import employeeEnrollment.dto.EmployeeResponseDTO;
import employeeEnrollment.dto.LoginRequestDTO;
import employeeEnrollment.dto.LoginResponseDTO;
import employeeEnrollment.entity.EmployeeEntity;
import employeeEnrollment.entity.Status;
import employeeEnrollment.exception.DuplicateResourceException;
import employeeEnrollment.mapper.EmployeeMapper;
import employeeEnrollment.repository.EmployeeRepository;
import employeeEnrollment.security.JwtUtil;
import employeeEnrollment.service.AuthService;
import jakarta.validation.Valid;

@Service
public class AuthServiceImplementation implements AuthService {

    private static final double DEFAULT_SALARY = 30000.0;

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImplementation(AuthenticationManager authManager, JwtUtil jwtUtil,
            EmployeeRepository repository, PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsernameOrEmail(),
                request.getPassword()));

        EmployeeEntity employee = repository.findByUsernameOrEmail(
                request.getUsernameOrEmail(),
                request.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(employee.getUsername(), employee.getRole());
        return new LoginResponseDTO(token, employee.getUsername(), employee.getRole());
    }

    @Override
    public EmployeeResponseDTO enrollEmployee(@Valid EmployeeRequestDTO dto) {
        validateUniqueFields(dto.getEmail(), dto.getUsername());

        EmployeeEntity employee = new EmployeeEntity();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary() != null && dto.getSalary() > 0 ? dto.getSalary() : DEFAULT_SALARY);
        employee.setJoiningDate(dto.getJoiningDate());
        employee.setAddress(dto.getAddress());
        employee.setUsername(dto.getUsername());
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        employee.setRole(normalizeRole(dto.getRole()));
        employee.setStatus(Status.ACTIVE);

        return EmployeeMapper.toDTO(repository.save(employee));
    }

    private void validateUniqueFields(String email, String username) {
        if (repository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already exists");
        }
        if (repository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username already exists");
        }
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "EMPLOYEE";
        }
        return role.replace("ROLE_", "").trim().toUpperCase();
    }
}
