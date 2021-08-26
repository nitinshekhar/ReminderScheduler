package com.nitin.component;

import java.text.ParseException;
import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import com.nitin.model.Reminder;
import com.nitin.repository.ReminderRepository;
import com.nitin.job.ReminderJob;
import com.nitin.service.ReminderService;

@Component
public class JobSchedulerCreator {
	private static Logger log = LoggerFactory.getLogger(JobSchedulerCreator.class);

	@Autowired
	private ReminderService reminderService;

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Autowired
	private ApplicationContext context;

	@Autowired
	private JobSchedulerCreator scheduleCreator;

	
	@Autowired
	private ReminderRepository reminderRepository;
	
	public void startAllSchedulers() {
		List<Reminder> jobInfoList = reminderService.findAll();
		log.info("JobSchedulerCreator : startAllSchedulers() : No.of Job to Schedule : "+jobInfoList.size());
		
		if (jobInfoList != null) {			
			jobInfoList.forEach(jobInfo -> {
				try {
					log.info("JobSchedulerCreator : startAllSchedulers() : Reminder is " +jobInfo.toString());
				} catch (Exception e) {
					log.error("ERROR");
				}
			});
		} 
	}
	  
    public void scheduleNewJob(Reminder jobInfo) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

//            @SuppressWarnings("unchecked")
//			Class<? extends QuartzJobBean> name = (Class<? extends QuartzJobBean>) Class.forName("ReminderJob");
//			JobDetail jobDetail = JobBuilder.newJob(name)
//                    .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
            String jobId = Long.toString(jobInfo.getReminderId());
            if (jobId != null) {
				JobDataMap jobDataMap = new JobDataMap();
				JobDetail jobDetail = JobBuilder.newJob(ReminderJob.class)
						.withIdentity(jobId)
						.withDescription(jobInfo.getJobName()).usingJobData(jobDataMap).storeDurably().build();
				
				log.info("JobSchedulerCreator : scheduleNewJob() : Job Detail : "+jobDetail.getKey());
	            if (!scheduler.checkExists(jobDetail.getKey())) {
					jobDetail = scheduleCreator.createJob(
							ReminderJob.class, false, context,
							jobInfo.getJobName(), jobInfo.getJobGroup(), jobId);
	
	                Trigger trigger;
	                
	                // Check for CronExpression is null or empty
//	                if (jobInfo.getCronJob()) {
	                	trigger = scheduleCreator.createCronTrigger(jobDetail, jobInfo);
//	                } else {
//	                	trigger = scheduleCreator.createSimpleTrigger(jobDetail, jobInfo);
//	                }
	
	                scheduler.scheduleJob(jobDetail, trigger);
	                reminderRepository.save(jobInfo);
	                log.info("JobSchedulerCreator : scheduleNewJob() : New Job Saved & Scheduled "+ jobInfo.getJobName());
	            } else {
	                log.error("JobSchedulerCreator : scheduleNewJob() : Job Already Exist");
	            }
            } else {
            	log.error("JobSchedulerCreator : scheduleNewJob() : Job ID is NULL");
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }


    public void updateScheduleJob(Reminder jobInfo) {
        try {
        	@SuppressWarnings("unchecked")
			Class<? extends QuartzJobBean> name = (Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobName());

        	String jobId = Long.toString(jobInfo.getReminderId());

        	JobDetail jobDetail = JobBuilder.newJob(name)
                  .withIdentity(jobId).build();
//                .withIdentity(jobInfo.getJobName(),jobInfo.getJobGroup()).build();
        	Trigger newTrigger;
//        	if (jobInfo.getCronJob()) {
        		newTrigger = scheduleCreator.createCronTrigger(jobDetail, jobInfo);
//        	} else {
//        		newTrigger = scheduleCreator.createSimpleTrigger(jobDetail, jobInfo);
//        	} 
            	schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);
            	reminderRepository.save(jobInfo);
                log.info("JobSchedulerCreator : updateScheduleJob() : Job Updated Saved & Scheduled "+ jobInfo.getJobName());
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
    }



    public boolean unScheduleJob(String jobName) {
        try {
            return schedulerFactoryBean.getScheduler().unscheduleJob(new TriggerKey(jobName));
        } catch (SchedulerException e) {
            log.error("Failed to un-schedule job - {}", jobName, e);
            return false;
        }
    }

 
    public boolean deleteJob(Reminder jobInfo) {
        try {
            return schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
        } catch (SchedulerException e) {
            log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

 
    public boolean pauseJob(Reminder jobInfo) {
        try {
            schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to pause job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

  
    public boolean resumeJob(Reminder jobInfo) {
        try {
            schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to resume job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

 
    public boolean startJobNow(Reminder jobInfo) {
        try {
            schedulerFactoryBean.getScheduler().triggerJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to start new job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }
	
	/*
     * Creates the Job Detail Object for given job
     */
    public JobDetail createJob(Class<? extends QuartzJobBean> jobClass, boolean isDurable,
            ApplicationContext context, String jobName, String groupName, String jobId) {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(jobClass);
        jobDetailFactory.setDurability(isDurable);
        jobDetailFactory.setApplicationContext(context);
        jobDetailFactory.setName(jobName);
        jobDetailFactory.setGroup(groupName); 

        // set job data map
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("JOBCLASSNAME", jobClass.getName());
        jobDataMap.put("JOBNAME", jobName);
        jobDataMap.put("JobId", jobId);
        jobDetailFactory.setJobDataMap(jobDataMap);
        jobDetailFactory.afterPropertiesSet();
        log.info("JobSchedulerCreator:createJob() : Creating New Job "+jobDetailFactory.getObject().toString());
        return jobDetailFactory.getObject();
    }

    /*
     * Creates Simple Trigger for job to start at specific time and repeat n number of time  with 
     * a possible delay between each execution
     * 
     */
    public SimpleTrigger createSimpleTrigger(JobDetail jobDetail, Reminder job) {
		CustomSimpleTriggerFactoryBean trigger =  new CustomSimpleTriggerFactoryBean();
		trigger.setName(job.getJobName());
		trigger.setJobDetail(jobDetail);
		trigger.setDescription("Simple Schedule");
		trigger.setStartTime(job.getStartDateTime());
		trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
		// Set the repeat interval
		// trigger.setRepeatInterval(0);
		trigger.afterPropertiesSet();
		log.info("JobSchedulerCreator:createSimpleTrigger() : Creating Simple Trigger "+trigger.getObject().toString());
		return trigger.getObject();
	}

    /*
     * Creates a Cron Trigger for recurring job by setting job details and expression
     */
	public CronTrigger createCronTrigger(JobDetail jobDetail, Reminder job) {
    	CustomCronTriggerFactoryBean trigger = new CustomCronTriggerFactoryBean();
    	trigger.setName(job.getJobName	());
       	trigger.setJobDetail(jobDetail);
    	trigger.setStartTime(job.getStartDateTime());
    	trigger.setCronExpression(job.getCronExpression());
    	trigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
    	trigger.setDescription("Cron Schedule");
        try {
        	trigger.afterPropertiesSet();
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        log.info("JobSchedulerCreator:createCronTrigger() : Creating Cron Trigger "+trigger.getObject().toString());
        return trigger.getObject();
	}
}
