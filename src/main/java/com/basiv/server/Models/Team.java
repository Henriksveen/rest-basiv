package com.basiv.server.Models;

import java.util.List;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * @author Henriksveen
 */
@Entity("teams")
public class Team {

    @Id
    private String id;
    private String name;
    private String coach;
    private String location;
    private String[] members;

    public Team() {

    }

    public Team(String name, String coach, String location,String[] members) {
        this.name = name;
        this.coach = coach;
        this.location = location;
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Team{" + "name=" + name + ", coach=" + coach + ", location=" + location + ", members=" + members + '}';
    }
}
