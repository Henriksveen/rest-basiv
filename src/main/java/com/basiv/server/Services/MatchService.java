package com.basiv.server.Services;

import com.basiv.server.Exceptions.DataNotFoundException;
import com.basiv.server.Models.Match;
import com.basiv.server.config.MongoDB;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import org.mongodb.morphia.Datastore;

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
        LOG.info("getMatch called.");
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
        match.setId(UUID.randomUUID().toString());
        mongoDatastore.save(match);
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
