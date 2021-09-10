package com.event.analyzer.reader;

import com.event.analyzer.repository.Event;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.LineMapper;


public class EventLineMapper implements LineMapper<Event> {

    protected static Logger logger = LoggerFactory.getLogger(EventLineMapper.class);

    public Event mapLine(String line, int lineNumber) throws Exception {
        // logging progress after every 100th lines
        if(lineNumber % 100 == 0)
            logger.info("[{}] lines processed.",lineNumber);

        ObjectMapper objectMapper = new ObjectMapper();
        Event ev = new Event();
        try {
            ev = objectMapper.readValue(line, Event.class);
        } catch (JsonParseException | JsonMappingException jmEx) {
            logger.error("Error in parsing record : [{}], Line number : [{}]", line, lineNumber);
        }

        if (!ev.validate())
            logger.info("Invalid event record : [{}] , Line number : [{}]", line, lineNumber);
        return ev;
    }
}
