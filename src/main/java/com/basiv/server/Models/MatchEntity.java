package com.basiv.server.Models;

import java.util.Date;
import java.util.List;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

/**
 * @author Henriksveen
 */
@Entity("matches")
public class MatchEntity {

    @Id
    private String id;
    @Property("created")
    private Date dateCreated;
    private Date liveStart;
    private boolean isLive;
    private String matchName;
    private TeamEntity[] teams;
    @Embedded
    private List<ScoreEntity> score;
    private String category;
    private String twitchUrl;
    private String description;
    private String imageSrc;
    private String eventEntitiesId;

    public MatchEntity() {

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

    public TeamEntity[] getTeams() {
        return teams;
    }

    public void setTeams(TeamEntity[] teams) {
        this.teams = teams;
    }

    public List<ScoreEntity> getScore() {
        return score;
    }

    public void setScore(List<ScoreEntity> score) {
        this.score = score;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTwitchUrl() {
        return twitchUrl;
    }

    public void setTwitchUrl(String twitchUrl) {
        this.twitchUrl = twitchUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getEventEntitiesId() {
        return eventEntitiesId;
    }

    public void setEventEntitiesId(String eventEntitiesId) {
        this.eventEntitiesId = eventEntitiesId;
    }
}
