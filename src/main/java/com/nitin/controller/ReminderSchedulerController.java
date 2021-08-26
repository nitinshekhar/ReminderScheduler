package com.nitin.controller;

import java.text.ParseException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.nitin.DTO.SchedulerRequest;
import com.nitin.DTO.SchedulerResponse;
import com.nitin.component.JobSchedulerCreator;
import com.nitin.model.Reminder;
import com.nitin.repository.ReminderRepository;
import com.nitin.util.ApplicationUtil;

@RestController
@RequestMapping("/api")
public class ReminderSchedulerController {
	private Logger log = LoggerFactory.getLogger(ReminderSchedulerController.class);

	@Autowired
	private ReminderRepository reminderRepository;
	
	@Autowired
	private JobSchedulerCreator jobSchedulerCreator;

	@PostMapping("/register")
	public @ResponseBody ResponseEntity<SchedulerResponse> create(@RequestBody SchedulerRequest request) {
		try {
			Reminder reminder = convertToEntity(request);
			log.info("ReminderSchedulerController : create() : Saving Reminder Object : " + reminder.toString());
			
			// Save the reminder in the repository
			reminderRepository.save(reminder);
			
			//Register the reminder using Quartz
			jobSchedulerCreator.scheduleNewJob(reminder);
			return new ResponseEntity<>(convertToDTO(reminder), HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "CANNOT CREATE REMINDER", e);
		}
	}

	// Create Reminder using GET Method

	@GetMapping("/create")
	public @ResponseBody ResponseEntity<SchedulerResponse> greeting(
			@RequestParam(value = "JobName", defaultValue = "Job1") String jobName,
			@RequestParam(value = "JobGroup", defaultValue = "Test") String jobGroup,
			@RequestParam(value = "StartDateTime", defaultValue = "2021/07/16 09:00:00") String startDateTime,
			@RequestParam(value = "EndDateTime", defaultValue = "2021/07/20 11:00:00") String endDateTime,
			@RequestParam(value = "CronExpression", defaultValue = "0 0/1 * 1/1 * ? *") String cronExpression,
			@RequestParam(value = "action", defaultValue = "http://localhost:8181/test") String action,
			@RequestParam(value = "notifyEmail", defaultValue = "test@test.com") String notifyEmail,
			@RequestParam(value = "CreatedBy", defaultValue = "System") String createdBy) {
		try {
			Reminder reminder = new Reminder(jobName, jobGroup, ApplicationUtil.convertToDate(startDateTime),
					ApplicationUtil.convertToDate(endDateTime), cronExpression, action, notifyEmail, createdBy);
			log.info("ReminderSchedulerController : Reminder Object : " + reminder.toString());
			
			//Save the reminder in the repository
			reminderRepository.save(reminder);
			
			// Register the reminder using Quartz
			jobSchedulerCreator.scheduleNewJob(reminder);
			return new ResponseEntity<>(convertToDTO(reminder), HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "CANNOT CREATE REMINDER", e);
		}
	}

	// Retrieve all Reminders

	@GetMapping
	public ResponseEntity<List<Reminder>> getAllReminder() {
		try {
			List<Reminder> reminders = reminderRepository.findAll();
			return new ResponseEntity<List<Reminder>>(reminders, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NO REGISTERED REMINDERS", e);
		}
	}

	// Retrieve a specific reminder

	@GetMapping("/{reminderId}")
	public ResponseEntity<Reminder> getReminder(@PathVariable("reminderId") long id) {
		try {
			Reminder reminder = reminderRepository.findById(id);
			return new ResponseEntity<Reminder>(reminder, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID NOT FOUND", e);
		}
	}

	@GetMapping("/pauseall")
	public void pauseAllReminder() {
		try {
			List<Reminder> reminders = reminderRepository.findAll();
			if (reminders != null) {
				reminders.forEach(reminder-> {
					jobSchedulerCreator.pauseJob(reminder);
					log.info("Reminder Paused : " + reminder.toString());
				}); 
			} else {
				log.info("No Reminder active to Pause");
			}				
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "Pause Failed", e);
		}
	}
	
	@GetMapping("/resume/{reminderId}")
	public ResponseEntity<SchedulerResponse>  resumeReminder(@PathVariable("reminderId") long id) {
		try {
			Reminder reminder = reminderRepository.findById(id);
			if (reminder != null) {
				jobSchedulerCreator.resumeJob(reminder);
				log.info("Reminder resumed : " + reminder.toString()); 
			} else {
				log.info("No Paused Reminder Found"); 
			}
			return new ResponseEntity<>(convertToDTO(reminder), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "Pause Failed", e);
		}
	}	
	/*
	 *  Delete a Reminder based on the id passed
	 */
	
	@DeleteMapping("/delete/{reminderId}") 
	public @ResponseBody ResponseEntity<SchedulerResponse> delete(@PathVariable("reminderId") long id) {
		try {
			Reminder reminder = reminderRepository.findById(id);
			// Delete from the Table
			reminderRepository.delete(reminder);
			
			// Remove the reminder from Quartz
			jobSchedulerCreator.deleteJob(reminder);
			return new ResponseEntity<>(convertToDTO(reminder), HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Error deleting job",ex);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No reminder found for :", ex);
		}	
	}

	// Convert the DTO into the entity
	private Reminder convertToEntity(SchedulerRequest request) throws ParseException {
		Reminder reminder = new Reminder();
		reminder.setJobName(request.getJobName());
		reminder.setJobGroup(request.getJobGroup());
		reminder.setCreatedBy(request.getCreatedBy());
		reminder.setCreatedOn(request.getCreatedOn());
		reminder.setCronExpression(request.getCronExpression());
		reminder.setAction(request.getAction());
		reminder.setNotifyEmail(request.getNotify());
		reminder.setStartDateTime(request.getDateTime());
		reminder.setEndDateTime(request.getEndDateTime());
		return reminder;
	}
	// Convert the entity to DTO

	private SchedulerResponse convertToDTO(Reminder reminder) {
		return new SchedulerResponse(reminder.getReminderId());
	}
}
