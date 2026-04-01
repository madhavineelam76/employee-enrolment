package employeeEnrollment.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class EmployeeRequestDTO {

	@NotBlank(message = "first name is required")
	private String firstName;
	
	private String lastName;
	
	@Email(message = "invalid email format")
	@NotBlank(message = "email is required")
	private String email;
	
	@Pattern(regexp = "\\d{10}" , message = "phone number must be 10 digits")
	private String phone;
	
	@NotBlank(message = "department is required")
	private String department;
	
	@NotBlank(message = "role is required")
	private String role;
	
	@Positive(message = "salary must be greater than 0")
	private Double salary;
	
	@PastOrPresent(message = "joining date cannot be in future")
	private LocalDate joiningDate;
	
	 @NotBlank(message = "address is required")
	private String address;
	
	@NotBlank(message = "username is required")
	private String username;
	
	@Size(min=8,message = "password must be at least 8 characters")
	private String password;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	
	
	
}
