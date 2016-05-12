package com.basiv.server.Models;

import java.util.Date;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * @author Henriksveen
 */
public class EventEntity {

    private String event;
    private Date time;

    public EventEntity() {

    }

    public String getComments() {
        return event;
    }

    public void setComments(String event) {
        this.event = event;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
