package employeeEnrollment.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import employeeEnrollment.entity.Status;
import employeeEnrollment.repository.EmployeeRepository;

@Configuration
public class EmployeeBootstrapConfig {

    @Bean
    public CommandLineRunner reactivateKnownUsers(EmployeeRepository repository) {
        return args -> {
            activateIfPresent(repository, "ram");
            activateIfPresent(repository, "madhavi");
        };
    }

    private void activateIfPresent(EmployeeRepository repository, String username) {
        repository.findByUsername(username).ifPresent(employee -> {
            if (employee.getStatus() == null || Status.INACTIVE.equals(employee.getStatus())) {
                employee.setStatus(Status.ACTIVE);
                repository.save(employee);
            }
        });
    }
}
