package com.event.analyzer.repository;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;

@Getter
@Setter
@Entity
public class Event {
    @Id
    private String id;
    private String state;
    private String type;
    private String host;
    private BigInteger timestamp;
    private BigInteger duration;
    private boolean alert;
    public static final String STARTED_STATE = "STARTED";
    public static final String FINISHED_STATE = "FINISHED";

    protected static Logger logger = LoggerFactory.getLogger(Event.class);

    public Event() {
        // default constructor
    }

    public Event(String id, String state, String type, String host, BigInteger timestamp) {
        this.id = id;
        this.state = state;
        this.type = type;
        this.host = host;
        this.timestamp = timestamp;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(getId());
        return hcb.toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Event)) {
            return false;
        }

        Event that = (Event) o;
        return new EqualsBuilder()
                .append(this.id, that.id)
                .isEquals();
    }

    /**
     * @return true if all required fields has values and
     */
    public boolean validate() {
        if (StringUtils.isBlank(id)) return false;
        if (timestamp == null || timestamp.compareTo(BigInteger.ZERO) != 1) return false;
        if (StringUtils.isBlank(state)) return false;
        if (!state.equals(STARTED_STATE) && !state.equals(FINISHED_STATE)) return false;

        return true;
    }

    public boolean isStartedEvent() {
        return STARTED_STATE.equalsIgnoreCase(state);
    }

    public boolean isFinishedEvent() {
        return FINISHED_STATE.equalsIgnoreCase(state);
    }

    /**
    Calculate duration of this event and otherEvent
    Perform validation if one is started and other is finished event,
    finished event timestamp should not be less than started event
     @param otherEvent The other event that has same id.
     */
    public void calcDuration(Event otherEvent) {
        Event startedEvent = null;
        Event finishedEvent = null;
        // finding started and finished event by state
        if (this.isStartedEvent())
            startedEvent = this;
        else if (otherEvent.isStartedEvent())
            startedEvent = otherEvent;

        if (this.isFinishedEvent())
            finishedEvent = this;
        else if (otherEvent.isFinishedEvent())
            finishedEvent = otherEvent;


        if (startedEvent == null || finishedEvent == null)
            return;

        if (finishedEvent.getTimestamp().compareTo(startedEvent.getTimestamp()) < 0) {
            logger.error("Started event has bigger timestamp than Finished event, id - [{}] , started event timestamp - [{}] , finished event timestamp - [{}]", startedEvent.getId(), startedEvent.getTimestamp(), finishedEvent.getTimestamp());
            return;
        }

        duration = finishedEvent.getTimestamp().subtract(startedEvent.getTimestamp());
    }

    /**
     * Set alert tru if duration > threshold
     * @param threshold - duration threshold
     */
    public void calcAlert(long threshold) {
        setAlert(duration != null && duration.longValue() > threshold);
    }

}

