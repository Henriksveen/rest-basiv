package com.basiv.server.Services;

import com.basiv.server.Models.MatchEntity;
import com.basiv.server.Models.ProfileEntity;
import com.basiv.server.Models.SearchEntity;
import com.basiv.server.config.MongoDB;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.mongodb.morphia.Datastore;

/**
 * @author Henriksveen
 */
public class SearchService {

    private static final Logger LOG = Logger.getLogger(MatchService.class.getName());
    private final Datastore mongoDatastore;

    public SearchService() {
        this.mongoDatastore = MongoDB.instance().getDatabase();
    }

    public SearchEntity getSearchObjects(String name) {
        Pattern regexp = Pattern.compile(name, Pattern.CASE_INSENSITIVE); //Implementes regular expression for filtering
        List<ProfileEntity> profileList = mongoDatastore.createQuery(ProfileEntity.class).filter("name", regexp).asList();
        List<MatchEntity> matchList = mongoDatastore.createQuery(MatchEntity.class).filter("matchName", regexp).asList();
        SearchEntity se = new SearchEntity();
        se.setMatchList(matchList);
        se.setProfileList(profileList);
        return se;
    }
}
