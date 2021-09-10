package com.event.analyzer.processor;

import com.event.analyzer.repository.Event;
import com.event.analyzer.repository.EventRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;


public class EventProcessor implements ItemProcessor<Event, Event> {

    @Autowired
    EventRepository eventRepository;

    @Value("${event.duration.threshold:4}")
    public long eventDurationThreshold;

    @Override
    public Event process(Event event) throws Exception {
        // blank event record sent by reader
        if (event.getId().isEmpty())
            return null;

        // finding existing started/finished event from database for same id
        Optional<Event> existingEventHolder = eventRepository.findById(event.getId());

        if (existingEventHolder.isPresent()) {
            Event existingEvent = existingEventHolder.get();

            // calculating duration and alert
            existingEvent.calcDuration(event);
            existingEvent.calcAlert(eventDurationThreshold);
            eventRepository.save(existingEvent);
            return existingEvent;
        } else {
            eventRepository.save(event);
            return event;
        }
    }
}