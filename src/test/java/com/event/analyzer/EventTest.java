package com.event.analyzer;

import com.event.analyzer.repository.Event;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;


public class EventTest {

    @Test
    public void testValidate() {
        // blank event
        Event ev = new Event();
        assertFalse(ev.validate());

        // test null state
        ev = new Event("dummy",null,null,null,BigInteger.valueOf(100l));
        assertFalse(ev.validate());

        // test invalid state
        ev = new Event("dummy","dummystate",null,null,BigInteger.valueOf(256));
        assertFalse(ev.validate());

        // test null timestamp
        ev = new Event("dummy","dummystate",null,null,null);
        assertFalse(ev.validate());

        // test invalid timestamp
        ev = new Event("dummy","dummystate",null,null,BigInteger.valueOf(-1));
        assertFalse(ev.validate());

        // test valid started event
        ev = new Event("dummy",Event.STARTED_STATE,null,null,BigInteger.valueOf(12));
        assertTrue(ev.validate());

        // test valid finished event
        ev = new Event("dummy",Event.FINISHED_STATE,null,null,BigInteger.valueOf(12));
        assertTrue(ev.validate());
    }
    @Test
    public void testCalcDuration(){

        // both events are started event
        Event ev1 = new Event("dummy",Event.STARTED_STATE,null,null,BigInteger.valueOf(12));
        Event ev2 = new Event("dummy",Event.STARTED_STATE,null,null,BigInteger.valueOf(12));
        ev1.calcDuration(ev2);
        assertNull(ev1.getDuration());

        // both events are finished event
        ev1 = new Event("dummy",Event.STARTED_STATE,null,null,BigInteger.valueOf(12));
        ev2 = new Event("dummy",Event.STARTED_STATE,null,null,BigInteger.valueOf(12));
        ev1.calcDuration(ev2);
        assertNull(ev1.getDuration());

        // both started event timestamp greater than finished event
        ev1 = new Event("dummy",Event.STARTED_STATE,null,null,BigInteger.valueOf(13));
        ev2 = new Event("dummy",Event.FINISHED_STATE,null,null,BigInteger.valueOf(12));
        ev1.calcDuration(ev2);
        assertNull(ev1.getDuration());

        // started event and finished event has same timestamp
        ev1 = new Event("dummy",Event.STARTED_STATE,null,null,BigInteger.valueOf(13));
        ev2 = new Event("dummy",Event.FINISHED_STATE,null,null,BigInteger.valueOf(13));
        ev1.calcDuration(ev2);
        assertEquals(BigInteger.ZERO,ev1.getDuration());

        // valid started event and finished event
        ev1 = new Event("dummy",Event.STARTED_STATE,null,null,BigInteger.valueOf(13));
        ev2 = new Event("dummy",Event.FINISHED_STATE,null,null,BigInteger.valueOf(20));
        ev1.calcDuration(ev2);
        assertEquals(BigInteger.valueOf(7l),ev1.getDuration());
    }
}