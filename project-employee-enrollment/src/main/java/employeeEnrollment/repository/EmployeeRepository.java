package employeeEnrollment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import employeeEnrollment.entity.EmployeeEntity;


@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

	Optional<EmployeeEntity> findByEmail(String email);
	
	Optional<EmployeeEntity> findByUsername(String username);
	
	Optional<EmployeeEntity> findByUsernameOrEmail(String username, String email);
	
	boolean existsByEmail(String email);
	
	boolean existsByUsername(String username);
	
	
}
