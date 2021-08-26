package com.nitin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

import com.nitin.model.Reminder;

@RepositoryRestResource
public interface ReminderRepository extends JpaRepository<Reminder, Long>{
	List<Reminder> findAll();
	Reminder findByJobName(@Param ("jobName") String Name);
	Reminder findById(@Param ("reminderId") long id);
}
