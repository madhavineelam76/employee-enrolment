package employeeEnrollment.dto;

import employeeEnrollment.entity.Status;
import jakarta.validation.constraints.NotNull;

public class EmployeeStatusUpdateRequestDTO {

    @NotNull(message = "status is required")
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
