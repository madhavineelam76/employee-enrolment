CREATE DATABASE IF NOT EXISTS employee_enrollment;
USE employee_enrollment;

CREATE TABLE IF NOT EXISTS employee (
    emp_id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(10) NOT NULL,
    department VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    salary DOUBLE NOT NULL,
    joining_date DATE NOT NULL,
    address VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_date TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_employee PRIMARY KEY (emp_id),
    CONSTRAINT uk_employee_email UNIQUE (email),
    CONSTRAINT uk_employee_username UNIQUE (username),
    CONSTRAINT chk_employee_salary CHECK (salary > 0),
    CONSTRAINT chk_employee_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE INDEX idx_employee_department ON employee (department);
CREATE INDEX idx_employee_status ON employee (status);
CREATE INDEX idx_employee_joining_date ON employee (joining_date);

-- Optional starter admin account placeholder.
-- Password must be a BCrypt hash if inserted manually through SQL.
-- INSERT INTO employee
-- (first_name, last_name, email, phone, department, role, salary, joining_date, address, username, password, status)
-- VALUES
-- ('System', 'Admin', 'admin@example.com', '9876543210', 'Administration', 'ADMIN', 95000, '2026-03-01', 'Head Office', 'admin1', '$2a$10$replace_with_bcrypt_hash', 'ACTIVE');
