package employeeEnrollment.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import employeeEnrollment.entity.EmployeeEntity;

public class CustomUserDetails implements UserDetails {

    private final EmployeeEntity employee;

    public CustomUserDetails(EmployeeEntity employee) {
        this.employee = employee;
    }

   
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + employee.getRole())
        );
    }

    @Override public String getPassword() { return employee.getPassword(); }
    @Override public String getUsername() { return employee.getUsername(); }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}