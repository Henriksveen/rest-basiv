package com.basiv.server.Services;

import com.basiv.server.Exceptions.DataNotFoundException;
import com.basiv.server.Models.TeamEntity;
import com.basiv.server.config.MongoDB;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;

/**
 * @author Henriksveen
 */
public class TeamService {
     private static final Logger LOG = Logger.getLogger(TeamService.class.getName());
    private final Datastore mongoDatastore;

    public TeamService() {
        this.mongoDatastore = MongoDB.instance().getDatabase();
    }

    public TeamEntity getTeam(String id) {
        TeamEntity team = mongoDatastore.createQuery(TeamEntity.class).filter("id =", id).get();
        if (team == null) {
            throw new DataNotFoundException("Team with id " + id + " notfound.");
        }
        return team;
    }

    public List<TeamEntity> getTeams() {
        return mongoDatastore.createQuery(TeamEntity.class).asList();
    }

    public TeamEntity addTeam(TeamEntity team) {
        team.setId(UUID.randomUUID().toString());
        Key k = mongoDatastore.save(team);
        if (k == null) {
            return null;
        }
        return team;
    }

    public TeamEntity updateTeam(TeamEntity team) {
        mongoDatastore.save(team);
        return team;
    }

    public void deleteTeam(String id) {
        TeamEntity team = mongoDatastore.createQuery(TeamEntity.class).filter("id =", id).get();
        if (team == null) {
            throw new DataNotFoundException("Team with id " + id + " notfound.");
        }
        mongoDatastore.delete(team);
    }
}
