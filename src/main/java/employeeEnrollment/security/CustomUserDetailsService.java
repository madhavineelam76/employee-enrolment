package employeeEnrollment.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import employeeEnrollment.entity.EmployeeEntity;
import employeeEnrollment.repository.EmployeeRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final EmployeeRepository repository;
	
	public CustomUserDetailsService(EmployeeRepository repository) {
		this.repository = repository;
	}

	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EmployeeEntity employee = repository
                .findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(employee);
    }

}
