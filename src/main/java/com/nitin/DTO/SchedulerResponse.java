package com.nitin.DTO;

import java.io.Serializable;

public class SchedulerResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String message;

	
	public SchedulerResponse(Long id) {
		this.setId(id);
	}
	
	public SchedulerResponse(long id, String message) {
		this.setId(id);
		this.setMessage(message);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
