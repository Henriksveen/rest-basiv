package com.basiv.server.Services;

import com.basiv.server.Exceptions.DataNotFoundException;
import com.basiv.server.Models.Match;
import com.basiv.server.Models.Team;
import com.basiv.server.config.MongoDB;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
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

    public Match getMatch(String id) {
        Match match = mongoDatastore.createQuery(Match.class).filter("id =", id).get();
        if (match == null) {
            throw new DataNotFoundException("Match with id " + id + " notfound.");
        }
        return match;
    }

    public List<Match> getMatches() {
        return mongoDatastore.createQuery(Match.class).asList();
    }

    public Match addMatch(Match match) {
        Team[] helpArray = new Team[match.getTeams().length];
        int counter = 0;
        for (Team t : match.getTeams()) {
            if (t.getLocation() == null && t.getCoach() == null && t.getName() == null) {
                Team getTeam = mongoDatastore.createQuery(Team.class).filter("id =", t.getId()).get();
                helpArray[counter] = getTeam;
            } else {
                t.setId(UUID.randomUUID().toString());
                mongoDatastore.save(t);
                helpArray[counter] = t;
            }
            counter++;
        }
        match.setTeams(helpArray);
        match.setId(UUID.randomUUID().toString());
        Key k = mongoDatastore.save(match);
        if (k == null) {
            return null;
        }
        return match;
    }

    public Match updateMatch(Match match) {
        mongoDatastore.save(match);
        return match;
    }

    public void deleteMatch(String id) {
        Match match = mongoDatastore.createQuery(Match.class).filter("id =", id).get();
        if (match == null) {
            throw new DataNotFoundException("Match with id " + id + " notfound.");
        }
        mongoDatastore.delete(match);
    }
}
