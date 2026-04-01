package employeeEnrollment.serviceImplementation;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import employeeEnrollment.dto.EmployeeRequestDTO;
import employeeEnrollment.dto.LoginRequestDTO;
import employeeEnrollment.dto.LoginResponseDTO;
import employeeEnrollment.entity.EmployeeEntity;
import employeeEnrollment.repository.EmployeeRepository;
import employeeEnrollment.security.JwtUtil;
import employeeEnrollment.service.AuthService;
import jakarta.validation.Valid;

@Service
public class AuthServiceImplementation implements AuthService {

	private  AuthenticationManager authManager;
    private  JwtUtil jwtUtil;
    private  EmployeeRepository repository;
    private  PasswordEncoder passwordEncoder;

    

    public AuthServiceImplementation(AuthenticationManager authManager, JwtUtil jwtUtil, EmployeeRepository repository,
			PasswordEncoder passwordEncoder) {
		super();
		this.authManager = authManager;
		this.jwtUtil = jwtUtil;
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}



    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        // Authenticate user
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        // Fetch employee entity
        EmployeeEntity employee = repository.findByUsernameOrEmail(
                request.getUsernameOrEmail(),
                request.getUsernameOrEmail())
        .orElseThrow(() -> new RuntimeException("User not found"));


        // Generate JWT token
        String token = jwtUtil.generateToken(employee.getUsername(), employee.getRole());

        return new LoginResponseDTO(token, employee.getUsername(), employee.getRole());
    }



    @Override
    public Object enrollEmployee(EmployeeRequestDTO dto) {

        // Create entity
        EmployeeEntity employee = new EmployeeEntity();

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());
        employee.setJoiningDate(dto.getJoiningDate());
        employee.setAddress(dto.getAddress());
        employee.setUsername(dto.getUsername());

        // Encode password
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Normalize role (IMPORTANT)
        String role = dto.getRole();

        if (role == null) {
            role = "EMPLOYEE"; // default
        }

        // Remove ROLE_ if user sent it
        role = role.replace("ROLE_", "");

        employee.setRole(role);

        // Save to DB
        EmployeeEntity saved = repository.save(employee);

        return saved;
    }}
    
    

