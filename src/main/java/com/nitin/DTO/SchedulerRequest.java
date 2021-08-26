package com.nitin.DTO;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SchedulerRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	private String jobName;
	private String jobGroup;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date dateTime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endDateTime;
	private String cronExpression;
	private String action;
	private String notify;
	private String createdBy;
	private Date createdOn;

	public SchedulerRequest(String name, Date startDateTime, Date endDateTime, String cronExpression, String action, String notify, String createdBy){
		super();
		this.setJobName(name);
		this.dateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.setCronExpression(cronExpression);
		this.setAction(action);
		this.setNotify(notify);
		this.setCreatedBy(createdBy);
		this.setCreatedOn(new Date(Calendar.getInstance().getTime().getTime()));
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String name) {
		this.jobName = name;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getNotify() {
		return notify;
	}

	public void setNotify(String notify) {
		this.notify = notify;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
}
