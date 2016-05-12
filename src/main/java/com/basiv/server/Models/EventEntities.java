package com.basiv.server.Models;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * @author Henriksveen
 */
@Entity("events")
public class EventEntities {

    @Id
    private String id;
    private EventEntity[] entities;

    public EventEntities() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EventEntity[] getEntities() {
        return entities;
    }

    public void setEntities(EventEntity[] entities) {
        this.entities = entities;
    }
}
