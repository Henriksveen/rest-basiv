package com.basiv.server.Services;

import com.basiv.server.Exceptions.DataNotFoundException;
import com.basiv.server.Models.MatchEntity;
import com.basiv.server.Models.ScoreEntity;
import com.basiv.server.Models.TeamEntity;
import com.basiv.server.config.MongoDB;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;

/**
 * @author Henriksveen
 */
public class MatchService {

    private static final Logger LOG = Logger.getLogger(MatchService.class.getName());
    private final Datastore mongoDatastore;
    public MatchService() {
        this.mongoDatastore = MongoDB.instance().getDatabase();
    }

    public MatchEntity getMatch(String id) {
        MatchEntity match = mongoDatastore.get(MatchEntity.class, id);
        if (match == null) {
            throw new DataNotFoundException("Match with id " + id + " notfound.");
        }
        return match;
    }

    public List<MatchEntity> getMatches() {
        return mongoDatastore.createQuery(MatchEntity.class).asList();
    }

    public MatchEntity addMatch(MatchEntity match) {
        TeamEntity[] helpArray = new TeamEntity[match.getTeams().length];
        int counter = 0;
        for (TeamEntity t : match.getTeams()) {
            if (t.getLocation() == null && t.getCoach() == null && t.getName() == null) {
                TeamEntity getTeam = mongoDatastore.get(TeamEntity.class, t.getId());
                helpArray[counter] = getTeam;
            } else {
                t.setId(UUID.randomUUID().toString());
                mongoDatastore.save(t);
                helpArray[counter] = t;
            }
            counter++;
        }
        match.setScore(new ScoreEntity());
        match.setTeams(helpArray);
        match.setId(UUID.randomUUID().toString());
        match.setDateCreated(new Date());
        Key k = mongoDatastore.save(match);
        if (k == null) {
            return null;
        }
        return match;
    }

    public MatchEntity updateMatch(MatchEntity match) {
        mongoDatastore.save(match);
        return match;
    }

    public void deleteMatch(String id) {
        MatchEntity match = mongoDatastore.createQuery(MatchEntity.class).filter("id =", id).get();
        if (match == null) {
            throw new DataNotFoundException("Match with id " + id + " notfound.");
        }
        mongoDatastore.delete(match);
    }
}
