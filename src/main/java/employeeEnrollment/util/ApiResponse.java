package employeeEnrollment.util;

import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiResponse {

	private int statusCode;
	private String message;
	private Object data;
	private LocalDateTime timestamp;
	
	public ApiResponse() {
		
	}

	public ApiResponse(int statusCode, String message, Object data, LocalDateTime timestamp) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.data = data;
		this.timestamp = timestamp;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
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

	public String toJson() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException ex) {
			return "{\"statusCode\":" + statusCode + ",\"message\":\"" + message + "\"}";
		}
	}
	
}
