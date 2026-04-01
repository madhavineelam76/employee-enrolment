package employeeEnrollment.entity;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass
public abstract class BaseEntity {
	
	@Column(name= "created_date")
	private LocalDateTime createdDate;
	
	@Column(name="updated_date")
	private LocalDateTime updatedDate;
	
	@PrePersist
	public void onCreate() {
		this.createdDate=LocalDateTime.now();
	}
	
	@PreUpdate
	public void onUpdate() {
		this.updatedDate=LocalDateTime.now();
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}
}
