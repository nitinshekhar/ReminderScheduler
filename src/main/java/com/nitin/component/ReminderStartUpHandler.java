package com.nitin.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ReminderStartUpHandler implements ApplicationRunner {

	private static Logger log = LoggerFactory.getLogger(JobSchedulerCreator.class);

	@Autowired
	private JobSchedulerCreator jobSchedulerCreator;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("ReminderStartUpHandler : run() : Schedule all new scheduler jobs at app startup - starting");
		try {
			jobSchedulerCreator.startAllSchedulers();
            log.info("ReminderStartUpHandler : run() : Schedule all new scheduler jobs at app startup - complete");
        } catch (Exception ex) {
            log.error("ReminderStartUpHandler : run() : Schedule all new scheduler jobs at app startup - error", ex);
        }		
	}

}
