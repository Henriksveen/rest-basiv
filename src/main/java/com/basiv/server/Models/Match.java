package com.basiv.server.Models;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;

/**
 * @author Henriksveen
 */
@Entity("matches")
public class Match {

    @Id
    private String id;
    @Property("created")
    private Date dateCreated;
    private Date liveStart;
    private boolean isLive;
    private String matchName;
    private Team[] teams;

    public Match() {

    }

    public Match(String id, Date liveStart, String matchName, Team[] team) {
        this.id = id;
        this.dateCreated = new Date();
        this.liveStart = liveStart;
        this.isLive = false;
        this.matchName = matchName;
        this.teams = team;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLiveStart() {
        return liveStart;
    }

    public void setLiveStart(Date liveStart) {
        this.liveStart = liveStart;
    }

    public boolean isIsLive() {
        return isLive;
    }

    public void setIsLive(boolean isLive) {
        this.isLive = isLive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Team[] getTeams() {
        return teams;
    }

    public void setTeams(Team[] teams) {
        this.teams = teams;
    }

    @Override
    public String toString() {
        return "Match{" + "id=" + id + ", dateCreated=" + dateCreated + ", liveStart=" + liveStart + ", isLive=" + isLive + ", matchName=" + matchName + '}';
    }
}
