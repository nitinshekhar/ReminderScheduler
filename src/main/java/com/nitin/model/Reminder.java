package com.nitin.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Reminder {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long reminderId;
	private String jobName;
	private String jobGroup;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startDateTime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endDateTime;
	private String cronExpression;
	private String action;
	private String notifyEmail;
	private String createdBy;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createdOn;
	
	public Reminder() { }
	
	public Reminder(long id){
		this.reminderId = id;
	}
	
	public Reminder(String jobName, String jobGroup, Date startDateTime, Date endDateTime, String cronExpression, String returnURL, String notify, String createdBy){
		super();
//		this.reminderId = id;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.cronExpression = cronExpression;
		this.action = returnURL;
		this.notifyEmail = notify;
		this.createdBy = createdBy;
		this.createdOn =  new Date(Calendar.getInstance().getTime().getTime());
	}

	public long getReminderId() {
		return reminderId;
	}

	public void setReminderId(long reminderId) {
		this.reminderId = reminderId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String Name) {
		this.jobName = Name;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
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

	public String getNotifyEmail() {
		return notifyEmail;
	}

	public void setNotifyEmail(String notify) {
		this.notifyEmail = notify;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	@Override
	public String toString() {
		return "Reminder [JobName="+ jobName + 
				"Job Group="+ jobGroup +
				"Start DateTime=" +startDateTime+
				"End DateTime ="+ endDateTime+
				"Expression ="+ cronExpression+
				"Action"+ action + 
				"Notify"+ notifyEmail + 
				"Created By ="+ createdBy+
				"Created On ="+  new Date(Calendar.getInstance().getTime().getTime())+
				"]";
	}
}
