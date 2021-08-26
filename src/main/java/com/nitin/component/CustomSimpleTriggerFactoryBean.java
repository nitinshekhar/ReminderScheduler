package com.nitin.component;

import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.nitin.util.AppConstant;


// An exception is caused by the fact that SimpleTriggerFactoryBean stores a reference to the JobDetail 
// in the JobDataMap. The JobDetail cannot be represented as a property, hence it is refused by Quartz.
// Work-around is to remove the JobDetail reference from the JobDataMap before returning the created SimpleTrigger. 
// For this we subclass the SimpleTriggerFactoryBean and remove it, before returning the Trigger.

public class CustomSimpleTriggerFactoryBean extends SimpleTriggerFactoryBean {
	
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		// Remove the JobDetail element
		getJobDataMap().remove(AppConstant.JOB_DETAIL_KEY);
	}

}
