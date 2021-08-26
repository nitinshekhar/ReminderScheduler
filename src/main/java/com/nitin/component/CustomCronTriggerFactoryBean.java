package com.nitin.component;

import java.text.ParseException;

import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

import com.nitin.util.AppConstant;

//An exception is caused by the fact that SimpleTriggerFactoryBean stores a reference to the JobDetail 
//in the JobDataMap. The JobDetail cannot be represented as a property, hence it is refused by Quartz.
//Work-around is to remove the JobDetail reference from the JobDataMap before returning the created SimpleTrigger. 
//For this we subclass the SimpleTriggerFactoryBean and remove it, before returning the Trigger.

public class CustomCronTriggerFactoryBean extends CronTriggerFactoryBean {

	@Override
	public void afterPropertiesSet() throws ParseException {
		super.afterPropertiesSet();
		// Remove the JobDetail element
		getJobDataMap().remove(AppConstant.JOB_DETAIL_KEY);
	}
}
