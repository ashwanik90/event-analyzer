package com.event.analyzer.writer;

import com.event.analyzer.repository.Event;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class NoOpItemWriter implements ItemWriter<Event> {

    @Override
    public void write(List<? extends Event> list) throws Exception {
        // do nothing
    }
}