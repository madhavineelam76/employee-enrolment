package employeeEnrollment.util;

import java.time.LocalDateTime;

public class ApiResponse {

	private int status;
	private String message;
	private Object data;
	private LocalDateTime timestamp;
	
	public ApiResponse() {
		
	}

	public ApiResponse(int status, String message, Object data, LocalDateTime timestamp) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
}
