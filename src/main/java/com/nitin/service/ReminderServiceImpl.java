package com.nitin.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nitin.model.Reminder;
import com.nitin.repository.ReminderRepository;

@Service
public class ReminderServiceImpl implements ReminderService {
	private static final Logger log = LoggerFactory.getLogger(ReminderServiceImpl.class);

	@Autowired
	private ReminderRepository reminderRepository;

	@Override
	public List<Reminder> findAll() {
		return reminderRepository.findAll();
	}
	@Override
	public Reminder findByJobName(String name) {
		return reminderRepository.findByJobName(name);
	}

	public void writeDataToLog(String dataToWrite) {
		log.info("The return URL is : " + dataToWrite);
	}
	@Override
	public Reminder findById(long id) {
		return reminderRepository.findById(id);
	}
}

