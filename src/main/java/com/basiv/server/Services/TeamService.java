package com.basiv.server.Services;

import com.basiv.server.Exceptions.DataNotFoundException;
import com.basiv.server.Models.Team;
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

    public Team getTeam(String id) {
        Team team = mongoDatastore.createQuery(Team.class).filter("id =", id).get();
        if (team == null) {
            throw new DataNotFoundException("Team with id " + id + " notfound.");
        }
        return team;
    }

    public List<Team> getTeams() {
        return mongoDatastore.createQuery(Team.class).asList();
    }

    public Team addTeam(Team team) {
        team.setId(UUID.randomUUID().toString());
        Key k = mongoDatastore.save(team);
        if (k == null) {
            return null;
        }
        return team;
    }

    public Team updateTeam(Team team) {
        mongoDatastore.save(team);
        return team;
    }

    public void deleteTeam(String id) {
        Team team = mongoDatastore.createQuery(Team.class).filter("id =", id).get();
        if (team == null) {
            throw new DataNotFoundException("Team with id " + id + " notfound.");
        }
        mongoDatastore.delete(team);
    }
}
