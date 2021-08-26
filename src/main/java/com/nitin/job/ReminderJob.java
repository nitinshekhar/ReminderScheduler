package com.nitin.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.nitin.model.Reminder;
import com.nitin.service.ReminderServiceImpl;

@Component
public class ReminderJob extends QuartzJobBean {
	private static final Logger log = LoggerFactory.getLogger(ReminderJob.class);
	
	@Autowired
	private ReminderServiceImpl reminderService;
		
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("ReminderJob : executeInternal() : Starting the Job "+context.getFireTime());
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		String jobName = jobDataMap.getString("JOBNAME");
		String jobId = jobDataMap.getString("JobId");
		log.info("ReminderJob : executeInternal() : Job Name is : "+jobName);
		log.info("ReminderJob : executeInternal() : Job Key is : "+jobDetail.getKey());
		log.info("ReminderJob : executeInternal() : Next Fire TIme is  "+context.getNextFireTime());
		
		JobKey jobKey = context.getJobDetail().getKey();
		log.info("ReminderJob : executeInternal() : Job Key is : "+jobKey);

		Reminder reminder = reminderService.findById(Long.parseLong(jobId));
		log.info("ReminderJob : executeInternal() : Reminder Data: "+reminder.toString());
//TODO	Add the logic of actual activity
		log.info("ReminderJob : executeInternal() : Ending the Job ");
	}
 
}
