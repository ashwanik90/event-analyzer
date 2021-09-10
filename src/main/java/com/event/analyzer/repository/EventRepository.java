package com.event.analyzer.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, String> {

    @Query("select count(ev) from Event ev where ev.duration is not null")
    public long countWhereDurationIsNotNull();

    @Query("select count(ev) from Event ev where ev.alert  is true")
    public long countWhereAlertIsTrue();

}
