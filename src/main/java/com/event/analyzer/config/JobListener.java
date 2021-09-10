package com.event.analyzer.config;

import com.event.analyzer.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;


public class JobListener implements JobExecutionListener {

    protected static Logger logger = LoggerFactory.getLogger(JobListener.class);

    @Autowired
    EventRepository eventRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        logger.info("Total matched events: [{}]", eventRepository.countWhereDurationIsNotNull());
        logger.info("Total events that have alerts: [{}]", eventRepository.countWhereAlertIsTrue());
    }
}