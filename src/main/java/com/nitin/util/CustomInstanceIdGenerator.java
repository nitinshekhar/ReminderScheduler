package com.nitin.util;

import java.util.UUID;

import org.quartz.SchedulerException;
import org.quartz.spi.InstanceIdGenerator;

public class CustomInstanceIdGenerator implements InstanceIdGenerator {
    @Override
    public String generateInstanceId() throws SchedulerException {
        try {
            return UUID.randomUUID().toString();
        } catch (Exception ex) {
            throw new SchedulerException("Couldn't generate UUID!", ex);
        }
    }
}
